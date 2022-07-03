package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.service.DmService;

@Controller
public class DmController {

	@Autowired
	DmService dmService;


	@RequestMapping("/dm")
	public ModelAndView dmMenue(ModelAndView modelAndView) {

		dmService.dmMenue(modelAndView);
		modelAndView.setViewName("dm-menue");
		return modelAndView;
	}

	@RequestMapping(value = "/dm/talk", method = RequestMethod.POST)
	public ModelAndView dmTalk(@RequestParam(value="receiveUser") String receiveUser,ModelAndView modelAndView) {

		dmService.talkHistory(modelAndView, receiveUser);
		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}

	@RequestMapping(value = "/dm/talk/send", method = RequestMethod.POST)
	public ModelAndView dmTalkSend(@RequestParam(value="receiveUser") String receiveUser, @RequestParam(value="msg") String msg, ModelAndView modelAndView) {

		dmService.recordChatTalkHistory(modelAndView, receiveUser, msg);
		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}
}
