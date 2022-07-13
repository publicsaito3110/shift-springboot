package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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
	NewsEditService newsEditService;


	@RequestMapping("/news-edit")
	public ModelAndView newsEdit(ModelAndView modelAndView) {

		NewsEditBean newsEditBean = newsEditService.newsEdit();
		modelAndView.addObject("newsList", newsEditBean.getNewsList());
		modelAndView.addObject("newsRecordList", newsEditBean.getNewsRecordList());

		modelAndView.addObject("newsEditForm", new NewsEditForm());
		modelAndView.addObject("newsCategoryArray", Const.NEWS_CATEGORY_ALL_ARRAY);
		modelAndView.setViewName("news-edit");
		return modelAndView;
	}


	@RequestMapping("/news-edit/test")
	public ModelAndView newsEditTest(@ModelAttribute("sampleForm") NewsEditForm newsEditForm, ModelAndView modelAndView) {


		modelAndView.addObject("ymd", newsEditForm.getYmd());
		modelAndView.addObject("beforeTitle", newsEditForm.getBeforeTitle());
		modelAndView.addObject("beforeCategory", newsEditForm.getBeforeCategory());
		modelAndView.addObject("beforeContent", newsEditForm.getBeforeContent());

		modelAndView.setViewName("/common/test");
		return modelAndView;
	}
}
