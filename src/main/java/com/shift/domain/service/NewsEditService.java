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


	private void selectRecordNews() {

		List<NewsEntity> newsRecordDbList = new ArrayList<>();
		String nowYmd = new CommonLogic().getNowDateToYmd();
		newsRecordDbList = newsRepository.findByYmdGreaterThanOrderByYmd(nowYmd);
		this.newsRecordDbList = newsRecordDbList;
	}


	private List<NewsBean> changeDisplayRecordNews() {

		List<NewsBean> newsRecordList = new ArrayList<>();

		//お知らせがないとき
		if (this.newsRecordDbList.isEmpty()) {

			//お知らせがないことを表示
			NewsBean newsbean = new NewsBean();
			newsbean.setYmd("お知らせはありません");
			newsRecordList.add(newsbean);
			return newsRecordList;
		}

		//newsRecordDbListの回数分newsRecordListに詰め替える
		for (NewsEntity newsEntity:this.newsRecordDbList) {
			newsRecordList.add(new NewsBean(newsEntity));
		}
		return newsRecordList;
	}


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
