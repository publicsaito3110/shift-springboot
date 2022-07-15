package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.shift.common.Const;
import com.shift.domain.model.bean.NewsEditBean;
import com.shift.domain.service.NewsEditService;
import com.shift.form.NewsEditForm;

/**
 * @author saito
 *
 */
@Controller
public class NewsEditController extends BaseController {

	@Autowired
	private NewsEditService newsEditService;


	@RequestMapping(value = "/news-edit", method = RequestMethod.GET)
	public ModelAndView newsEdit(ModelAndView modelAndView) {

		NewsEditBean newsEditBean = newsEditService.newsEdit();
		modelAndView.addObject("newsList", newsEditBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());

		modelAndView.addObject("newsEditForm", new NewsEditForm());
		modelAndView.addObject("isModalResult", false);
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.setViewName("news-edit");
		return modelAndView;
	}


	@RequestMapping(value = "/news-edit", method = RequestMethod.POST)
	public ModelAndView newsEditPost(@ModelAttribute NewsEditForm newsEditForm, ModelAndView modelAndView) {

		NewsEditBean newsEditBean = newsEditService.newsEditPost(newsEditForm);
		modelAndView.addObject("newsList", newsEditBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());

		modelAndView.addObject("newsEditForm", new NewsEditForm());
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "お知らせ修正結果");
		modelAndView.addObject("modalResultContent", "お知らせを修正しました。");
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.setViewName("news-edit");
		return modelAndView;
	}
}
