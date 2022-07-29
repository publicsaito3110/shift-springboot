package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.DmBean;
import com.shift.domain.model.bean.DmTalkBean;
import com.shift.domain.model.bean.DmTalkSendBean;
import com.shift.domain.service.DmService;

/**
 * @author saito
 *
 */
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
		modelAndView.addObject("receiveUser", dmTalkBean.getReceiveUser());
		modelAndView.addObject("receiveUserName", dmTalkBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkBean.getTalkHistoryList());

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}


	@RequestMapping(value = "/dm/talk/send", method = RequestMethod.POST)
	public ModelAndView dmTalkSend(@RequestParam(value="receiveUser") String receiveUser, @RequestParam(value="msg") String msg, ModelAndView modelAndView) {

		DmTalkSendBean dmTalkSendBean = this.dmService.dmTalkSend(receiveUser, msg);
		modelAndView.addObject("receiveUser", dmTalkSendBean.getReceiveUser());
		modelAndView.addObject("receiveUserName", dmTalkSendBean.getReceiveUserName());
		modelAndView.addObject("talkHistoryList", dmTalkSendBean.getTalkHistoryList());

		modelAndView.setViewName("dm-talk");
		return modelAndView;
	}
}
