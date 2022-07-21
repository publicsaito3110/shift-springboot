package com.shift.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.model.bean.NewsBean;
import com.shift.domain.model.bean.NewsEditBean;
import com.shift.domain.model.entity.NewsEntity;
import com.shift.domain.repository.NewsRepository;
import com.shift.form.NewsEditForm;

/**
 * @author saito
 *
 */
@Service
public class NewsEditService extends BaseService {


	@Autowired
	private HomeService homeService;

	/**
	 * [Service] (/news-edit)Get
	 *
	 * @param void
	 * @return NewsEditBean
	 */
	public NewsEditBean newsEdit() {

		HomeBean homeBean = homeService.home();
		this.selectRecordNews();
		List<NewsBean> newsRecordList = this.changeDisplayRecordNews();

		//Beanにセット
		NewsEditBean newsEditBean = new NewsEditBean(homeBean.getNewsList(), newsRecordList);
		return newsEditBean;
	}


	/**
	 * [Service] (/news-edit) Post
	 *
	 * @param NewsEditForm Request Param
	 * @return NewsEditBean
	 */
	public NewsEditBean newsEditPost(NewsEditForm newsEditForm) {

		this.updateRecordNews(newsEditForm);
		HomeBean homeBean = homeService.home();
		this.selectRecordNews();
		List<NewsBean> newsRecordList = this.changeDisplayRecordNews();

		//Beanにセット
		NewsEditBean newsEditBean = new NewsEditBean(homeBean.getNewsList(), newsRecordList);
		return newsEditBean;
	}


	@Autowired
	private NewsRepository newsRepository;


	//フィールド
	private List<NewsEntity> newsRecordDbList;


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
	 * [DB]お知らせ更新処理
	 *
	 * <p>修正後のお知らせを更新する<br>
	 * ただし、idとymdは修正できない
	 * </p>
	 *
	 * @param newsEditForm Request Param
	 * @return void
	 */
	private void updateRecordNews(NewsEditForm newsEditForm) {

		NewsEntity newsEntity = new NewsEntity();
		newsEntity.setId(newsEditForm.getId());
		newsEntity.setYmd(newsEditForm.getYmd());
		newsEntity.setTitle(newsEditForm.getTitle());
		newsEntity.setCategory(newsEditForm.getCategory());
		newsEntity.setContent(newsEditForm.getContent());
		this.newsRepository.save(newsEntity);
	}
}
