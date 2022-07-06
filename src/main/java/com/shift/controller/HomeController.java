package com.shift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {



	@RequestMapping("/home")
	public ModelAndView home(ModelAndView modelAndView) {

//		dmService.dmMenue(modelAndView);
		modelAndView.setViewName("home");
		return modelAndView;
	}
}
