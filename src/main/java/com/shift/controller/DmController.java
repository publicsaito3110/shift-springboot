package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.DmBean;
import com.shift.domain.model.bean.DmSendBean;
import com.shift.domain.model.bean.DmTalkBean;
import com.shift.domain.service.DmService;

@Controller
public class DmController extends BaseController {

	@Autowired
	private DmService dmService;


	@RequestMapping("/dm")
	public ModelAndView dm(ModelAndView modelAndView) {

		DmBean dmBean = this.dmService.dm();
		modelAndView.addObject("dmHistoryList", dmBean.getDmHistoryList());

		modelAndView.setViewName("dm");
		return modelAndView;
	}


	@RequestMapping(value = "/dm/talk", method = RequestMethod.POST)
	public ModelAndView dmTalk(@RequestParam(value="receiveUser") String receiveUser, ModelAndView modelAndView) {

		DmTalkBean dmTalkBean = this.dmService.dmTalk(receiveUser);
		modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}


	@RequestMapping(value = "/dm/talk/send", method = RequestMethod.POST)
	public ModelAndView dmTalkSend(@RequestParam(value="receiveUser") String receiveUser, @RequestParam(value="msg") String msg, ModelAndView modelAndView) {

		DmSendBean dmSendBean = this.dmService.dmTalkSend(receiveUser, msg);
		modelAndView.addObject("talkHistoryList", dmSendBean.getTalkHistoryList());

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}
}
