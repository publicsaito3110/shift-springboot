package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.service.DmService;

@Controller
public class DmController {

	@Autowired
	DmService dmService;

	@RequestMapping(value="/dm")
	public ModelAndView dmMenue(ModelAndView modelAndView) {

		dmService.dmMenue(modelAndView);
		modelAndView.setViewName("dm-menue");
		return modelAndView;
	}

	@RequestMapping(value="/dm/talk", method=RequestMethod.POST)
	public ModelAndView dmTalk(ModelAndView modelAndView) {

//		dmService.dm(modelAndView);
		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}
}
