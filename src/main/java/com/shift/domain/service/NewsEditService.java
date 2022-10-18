package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.bean.CmnNewsBean;
import com.shift.domain.model.bean.NewsBean;
import com.shift.domain.model.bean.NewsEditAddBean;
import com.shift.domain.model.bean.NewsEditBean;
import com.shift.domain.model.bean.NewsEditModifyBean;
import com.shift.domain.model.entity.NewsEntity;
import com.shift.domain.repository.NewsRepository;
import com.shift.domain.service.common.CmnNewsService;
import com.shift.form.NewsEditAddForm;
import com.shift.form.NewsEditModifyForm;

/**
 * @author saito
 *
 */
@Service
public class NewsEditService extends BaseService {

	@Autowired
	private CmnNewsService cmnNewsService;

	@Autowired
	private NewsRepository newsRepository;


	/**
	 * [Service] (/news-edit)
	 *
	 * @param void
	 * @return NewsEditBean
	 */
	public NewsEditBean newsEdit() {

		//Service内の処理を実行
		List<NewsEntity> newsRecordDbList = selectRecordNews();
		List<NewsBean> newsRecordList = changeDisplayRecordNews(newsRecordDbList);
		String[] newsRecordableMaxMinDateArray = calcRecordableDateRangeNews();
		//CmnNewsService(共通サービス)から処理結果を取得
		CmnNewsBean cmnNewsBean = cmnNewsService.generateDisplayNowNews();

		//Beanにセット
		NewsEditBean newsEditBean = new NewsEditBean(cmnNewsBean.getNewsList(), newsRecordList, newsRecordableMaxMinDateArray[0], newsRecordableMaxMinDateArray[1]);
		return newsEditBean;
	}


	/**
	 * [Service] (/news-edit/modify)
	 *
	 * @param NewsEditModifyForm Request Param
	 * @return NewsEditBean
	 */
	public NewsEditModifyBean newsEditModify(NewsEditModifyForm newsEditModifyForm) {

		//Service内の処理を実行
		boolean isUpdate = updateRecordedNews(newsEditModifyForm);
		List<NewsEntity> newsRecordDbList = selectRecordNews();
		List<NewsBean> newsRecordList = changeDisplayRecordNews(newsRecordDbList);
		String[] newsRecordableMaxMinDateArray = calcRecordableDateRangeNews();
		//CmnNewsService(共通サービス)から処理結果を取得
		CmnNewsBean cmnNewsBean = cmnNewsService.generateDisplayNowNews();

		//Beanにセット
		NewsEditModifyBean newsEditModifyBean = new NewsEditModifyBean(isUpdate, cmnNewsBean.getNewsList(), newsRecordList, newsRecordableMaxMinDateArray[0], newsRecordableMaxMinDateArray[1]);
		return newsEditModifyBean;
	}


	/**
	 * [Service] (/news-edit/add)
	 *
	 * @param newsEditAddForm RequestParameter Form
	 * @return NewsEditBean
	 */
	public NewsEditAddBean newsEditAdd(NewsEditAddForm newsEditAddForm) {

		//Service内の処理を実行
		insertNews(newsEditAddForm);
		List<NewsEntity> newsRecordDbList = selectRecordNews();
		List<NewsBean> newsRecordList = changeDisplayRecordNews(newsRecordDbList);
		String[] newsRecordableMaxMinDateArray = calcRecordableDateRangeNews();
		//CmnNewsService(共通サービス)から処理結果を取得
		CmnNewsBean cmnNewsBean = cmnNewsService.generateDisplayNowNews();

		//Beanにセット
		NewsEditAddBean newsEditAddBean = new NewsEditAddBean(cmnNewsBean.getNewsList(), newsRecordList, newsRecordableMaxMinDateArray[0], newsRecordableMaxMinDateArray[1]);
		return newsEditAddBean;
	}


	/**
	 * 登録済みお知らせ変換処理
	 *
	 * <p>newsRecordDbList(NewsEntity)をnewsRecordList(NewsBean)に詰め替える<br>
	 * ただし、検索結果ページ
	 * </p>
	 *
	 * @param newsRecordDbList DBから取得したList<NewsEntity> (List&lt;NewsEntity&gt;)
	 * @return void
	 */
	private List<NewsBean> changeDisplayRecordNews(List<NewsEntity> newsRecordDbList) {

		//登録済みのお知らせがない(newsRecordDbListがEmpty)とき
		if (newsRecordDbList.isEmpty()) {
			return new ArrayList<>();
		}

		//お知らせを格納するための変数
		List<NewsBean> newsRecordList = new ArrayList<>();

		//newsRecordDbListの回数分だけ詰め替える
		for (NewsEntity newsEntity: newsRecordDbList) {
			newsRecordList.add(new NewsBean(newsEntity));
		}
		return newsRecordList;
	}


	/**
	 * お知らせ登録可能日付計算処理
	 *
	 * <p>現在の日付からお知らせを登録できる日付の範囲を計算する<br>
	 * ただし、登録可能日付は現在の日付からとなる
	 * </p>
	 *
	 * @param void
	 * @return String[] 登録可能最大日[0]と登録可能最小日[1]<br>
	 * String[0]が登録可能最大日, String[1]が登録可能最小日
	 */
	private String[] calcRecordableDateRangeNews() {

		//現在の日付をLocalDateで取得
		LocalDate nowLd = LocalDate.now();

		//nowLdから最大登録可能日をLocalDateで取得
		LocalDate newsRecordableMaxDateLd = nowLd.plusMonths(Const.NEWS_RECORDABLE_MAX_MONTH);

		//登録可能日のmaxとminをYYYY-MM-DDで取得
		String newsRecordableMaxDate = newsRecordableMaxDateLd.toString();
		String newsRecordableMinDate = nowLd.toString();

		//配列に格納し、返す
		String[] newsRecordableMaxMinDateArray = {newsRecordableMaxDate, newsRecordableMinDate};
		return newsRecordableMaxMinDateArray;
	}


	/**
	 * [DB]登録済みお知らせ検索処理
	 *
	 * <p>現在の日付以降のお知らせを全て取得する<br>
	 * ただし、登録済みのお知らせがないときはEmptyとなる
	 * </p>
	 *
	 * @param void
	 * @return List<NewsEntity><br>
	 * フィールド(List&lt;NewsEntity&gt;)<br>
	 * id, ymd, category, title, content
	 */
	private List<NewsEntity> selectRecordNews() {

		//現在日をymdで取得し、現在日以降の登録済みのお知らせを取得
		String nowYmd = new CommonLogic().getNowDateToYmd();
		List<NewsEntity> newsRecordDbList = newsRepository.findByYmdGreaterThanOrderByYmd(nowYmd);
		return newsRecordDbList;
	}


	/**
	 * [DB]お知らせ更新処理
	 *
	 * <p>修正後のお知らせを更新する<br>
	 * ただし、idとymdは修正されない
	 * </p>
	 *
	 * @param newsEditModifyForm RequestParam Form
	 * @return boolean<br>
	 * true: お知らせの更新が成功したとき<br>
	 * false: お知らせの更新が失敗したとき
	 */
	private boolean updateRecordedNews(NewsEditModifyForm newsEditModifyForm) {


		//idが存在するか検索
		Optional<NewsEntity> newsEntityOpt = newsRepository.findById(Integer.valueOf(newsEditModifyForm.getId()));

		//レコードが存在しないとき、何もせずfalseを返す
		if (!newsEntityOpt.isPresent()) {
			return false;
		}

		//お知らせを更新し、trueを返す
		NewsEntity newsEntity = newsEntityOpt.get();
		newsEntity.setTitle(newsEditModifyForm.getTitle());
		newsEntity.setCategory(newsEditModifyForm.getCategory());
		newsEntity.setContent(newsEditModifyForm.getContent());
		newsRepository.save(newsEntity);
		return true;
	}


	/**
	 * [DB]お知らせ新規追加処理
	 *
	 * <p>新規のお知らせを追加する</p>
	 *
	 * @param newsEditAddForm RequestParameter Form
	 * @return boolean<br>
	 * true: お知らせの新規追加が成功したとき<br>
	 * false: お知らせの新規追加が失敗したとき
	 */
	private boolean insertNews(NewsEditAddForm newsEditAddForm) {

		//dateをymd(YYYYMMDD)に変換
		String date = newsEditAddForm.getDate();
		String ymd = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);

		//お知らせを追加し、trueを返す
		NewsEntity newsEntity = new NewsEntity();
		newsEntity.setYmd(ymd);
		newsEntity.setTitle(newsEditAddForm.getTitle());
		newsEntity.setCategory(newsEditAddForm.getCategory());
		newsEntity.setContent(newsEditAddForm.getContent());
		newsRepository.save(newsEntity);
		return true;
	}
}
