package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.HomeBean;
import com.shift.domain.service.HomeService;

/**
 * @author saito
 *
 */
@Controller
public class HomeController extends BaseController {

	@Autowired
	private HomeService homeService;


	@RequestMapping("/home")
	public ModelAndView home(ModelAndView modelAndView) {

		HomeBean homeBean = homeService.home();
		modelAndView.addObject("newsList", homeBean.getNewsList());

		modelAndView.setViewName("home");
		return modelAndView;
	}
}
