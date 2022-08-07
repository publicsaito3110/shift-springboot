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

	/**
	 * [Service] (/news-edit)
	 *
	 * @param void
	 * @return NewsEditBean
	 */
	public NewsEditBean newsEdit() {

		HomeBean homeBean = homeService.home();
		this.selectRecordNews();
		List<NewsBean> newsRecordList = this.changeDisplayRecordNews();
		this.calcRecordableDateRangeNews();

		//Beanにセット
		NewsEditBean newsEditBean = new NewsEditBean(homeBean.getNewsList(), newsRecordList, this.newsRecordableMaxDate, this.newsRecordableMinDate);
		return newsEditBean;
	}


	/**
	 * [Service] (/news-edit/modify)
	 *
	 * @param NewsEditModifyForm Request Param
	 * @return NewsEditBean
	 */
	public NewsEditModifyBean newsEditModify(NewsEditModifyForm newsEditModifyForm) {

		this.updateRecordedNews(newsEditModifyForm);
		HomeBean homeBean = homeService.home();
		this.selectRecordNews();
		List<NewsBean> newsRecordList = this.changeDisplayRecordNews();
		this.calcRecordableDateRangeNews();

		//Beanにセット
		NewsEditModifyBean newsEditModifyBean = new NewsEditModifyBean(homeBean.getNewsList(), newsRecordList, this.newsRecordableMaxDate, this.newsRecordableMinDate);
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

		this.insertNews(title, date, category, content);
		HomeBean homeBean = homeService.home();
		this.selectRecordNews();
		List<NewsBean> newsRecordList = this.changeDisplayRecordNews();
		this.calcRecordableDateRangeNews();

		//Beanにセット
		NewsEditAddBean newsEditAddBean = new NewsEditAddBean(homeBean.getNewsList(), newsRecordList, this.newsRecordableMaxDate, this.newsRecordableMinDate);
		return newsEditAddBean;
	}


	@Autowired
	private NewsRepository newsRepository;


	//フィールド
	private List<NewsEntity> newsRecordDbList;
	private String newsRecordableMaxDate;
	private String newsRecordableMinDate;


	/**
	 * [DB]登録済みお知らせ検索処理
	 *
	 * <p>現在の日付以降のお知らせを全て取得する</p>
	 *
	 * @param void
	 * @return void
	 */
	private void selectRecordNews() {

		List<NewsEntity> newsRecordDbList = new ArrayList<>();
		String nowYmd = new CommonLogic().getNowDateToYmd();
		newsRecordDbList = newsRepository.findByYmdGreaterThanOrderByYmd(nowYmd);
		this.newsRecordDbList = newsRecordDbList;
	}


	/**
	 * 前後ページ計算処理
	 *
	 * <p>現在のページから前後のページを計算する<br>
	 * ただし、検索結果ページ
	 * </p>
	 *
	 * @param page Request Param
	 * @return void
	 */
	private List<NewsBean> changeDisplayRecordNews() {

		//お知らせがないとき
		if (this.newsRecordDbList.isEmpty()) {
			return new ArrayList<>();
		}

		//お知らせを格納するための変数
		List<NewsBean> newsRecordList = new ArrayList<>();

		//newsRecordDbListの回数分newsRecordListに詰め替える
		for (NewsEntity newsEntity:this.newsRecordDbList) {
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
	 * @return void
	 */
	private void calcRecordableDateRangeNews() {

		//現在の日付をLocalDateで取得
		CommonLogic commonLogic = new CommonLogic();
		String nowYmd = commonLogic.getNowDateToYmd();
		LocalDate nowLd = commonLogic.getLocalDateByYmd(nowYmd);

		//nowLdから最大登録可能日を取得
		LocalDate newsRecordableMaxDateLd = nowLd.plusMonths(Const.NEWS_RECORDABLE_MAX_MONTH);

		//フィールドにセット
		this.newsRecordableMinDate = nowLd.toString();
		this.newsRecordableMaxDate = newsRecordableMaxDateLd.toString();
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

		NewsEntity newsEntity = new NewsEntity();
		newsEntity.setId(newsEditModifyForm.getId());
		newsEntity.setYmd(newsEditModifyForm.getYmd());
		newsEntity.setTitle(newsEditModifyForm.getTitle());
		newsEntity.setCategory(newsEditModifyForm.getCategory());
		newsEntity.setContent(newsEditModifyForm.getContent());
		this.newsRepository.save(newsEntity);
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

		NewsEntity newsEntity = new NewsEntity();
		newsEntity.setYmd(ymd);
		newsEntity.setTitle(title);
		newsEntity.setCategory(category);
		newsEntity.setContent(content);
		this.newsRepository.save(newsEntity);
	}
}
