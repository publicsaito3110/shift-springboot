package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.NewsEditBean;
import com.shift.domain.service.NewsEditService;

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

		modelAndView.setViewName("news-edit");
		return modelAndView;
	}
}
