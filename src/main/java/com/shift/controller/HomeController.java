package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.service.HomeService;

@Controller
public class HomeController extends BaseController {

	@Autowired
	HomeService homeService;


	@RequestMapping("/home")
	public ModelAndView home(ModelAndView modelAndView) {

		homeService.home(modelAndView);
		modelAndView.setViewName("home");
		return modelAndView;
	}
}
