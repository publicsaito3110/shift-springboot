package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.model.bean.NewsBean;
import com.shift.domain.model.bean.NewsEditAddBean;
import com.shift.domain.model.bean.NewsEditBean;
import com.shift.domain.model.bean.NewsEditModifyBean;
import com.shift.domain.model.entity.NewsEntity;
import com.shift.domain.repository.NewsRepository;
import com.shift.form.NewsEditModifyForm;

/**
 * @author saito
 *
 */
@Service
public class NewsEditService extends BaseService {

	@Autowired
	private HomeService homeService;

	@Autowired
	private NewsRepository newsRepository;


	/**
	 * [Service] (/news-edit)
	 *
	 * @param void
	 * @return NewsEditBean
	 */
	public NewsEditBean newsEdit() {

		List<NewsEntity> newsRecordDbList = selectRecordNews();
		List<NewsBean> newsRecordList = changeDisplayRecordNews(newsRecordDbList);
		String[] newsRecordableMaxMinDateArray = calcRecordableDateRangeNews();
		//homeServiceの処理結果を取得
		HomeBean homeBean = homeService.home();

		//Beanにセット
		NewsEditBean newsEditBean = new NewsEditBean(homeBean.getNewsList(), newsRecordList, newsRecordableMaxMinDateArray[0], newsRecordableMaxMinDateArray[1]);
		return newsEditBean;
	}


	/**
	 * [Service] (/news-edit/modify)
	 *
	 * @param NewsEditModifyForm Request Param
	 * @return NewsEditBean
	 */
	public NewsEditModifyBean newsEditModify(NewsEditModifyForm newsEditModifyForm) {

		updateRecordedNews(newsEditModifyForm);
		List<NewsEntity> newsRecordDbList = selectRecordNews();
		List<NewsBean> newsRecordList = changeDisplayRecordNews(newsRecordDbList);
		String[] newsRecordableMaxMinDateArray = calcRecordableDateRangeNews();
		//homeServiceの処理結果を取得
		HomeBean homeBean = homeService.home();

		//Beanにセット
		NewsEditModifyBean newsEditModifyBean = new NewsEditModifyBean(homeBean.getNewsList(), newsRecordList, newsRecordableMaxMinDateArray[0], newsRecordableMaxMinDateArray[1]);
		return newsEditModifyBean;
	}


	/**
	 * [Service] (/news-edit/add)
	 *
	 * @param title Request Param
	 * @param date Request Param
	 * @param category Request Param
	 * @param content Request Param
	 * @return NewsEditBean
	 */
	public NewsEditAddBean newsEditAdd(String title, String date, String category, String content) {

		insertNews(title, date, category, content);
		List<NewsEntity> newsRecordDbList = selectRecordNews();
		List<NewsBean> newsRecordList = changeDisplayRecordNews(newsRecordDbList);
		String[] newsRecordableMaxMinDateArray = calcRecordableDateRangeNews();
		//homeServiceの処理結果を取得
		HomeBean homeBean = homeService.home();

		//Beanにセット
		NewsEditAddBean newsEditAddBean = new NewsEditAddBean(homeBean.getNewsList(), newsRecordList, newsRecordableMaxMinDateArray[0], newsRecordableMaxMinDateArray[1]);
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
	 * ただし、idとymdは修正できない
	 * </p>
	 *
	 * @param newsEditModifyForm Request Param
	 * @return void
	 */
	private void updateRecordedNews(NewsEditModifyForm newsEditModifyForm) {

		//newsEntityに値をセットし、更新
		NewsEntity newsEntity = new NewsEntity();
		newsEntity.setId(newsEditModifyForm.getId());
		newsEntity.setYmd(newsEditModifyForm.getYmd());
		newsEntity.setTitle(newsEditModifyForm.getTitle());
		newsEntity.setCategory(newsEditModifyForm.getCategory());
		newsEntity.setContent(newsEditModifyForm.getContent());
		newsRepository.save(newsEntity);
	}


	/**
	 * [DB]お知らせ新規追加処理
	 *
	 * <p>修正後のお知らせを更新する<br>
	 * ただし、idとymdは修正できない
	 * </p>
	 *
	 * @param newsEditForm Request Param
	 * @return void
	 */
	private void insertNews(String title, String date, String category, String content) {

		//dateをymd(YYYYMMDD)に変換
		String ymd = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);

		//newsEntityに値をセットし、追加
		NewsEntity newsEntity = new NewsEntity();
		newsEntity.setYmd(ymd);
		newsEntity.setTitle(title);
		newsEntity.setCategory(category);
		newsEntity.setContent(content);
		newsRepository.save(newsEntity);
	}
}
