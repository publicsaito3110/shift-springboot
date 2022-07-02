package com.shift.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmInterface;
import com.shift.domain.Interface.DmTalkInterface;

@Component
public class DmService{

	@Autowired
	DmInterface dmIntarface;

	@Autowired
	DmTalkInterface dmTalkInterface;


	public DmService(DmInterface dmIntarface, DmTalkInterface dmTalkInterface) {
		super();
		this.dmIntarface = dmIntarface;
		this.dmTalkInterface = dmTalkInterface;
	}


	public void dmMenue(ModelAndView modelAndView) {

		dmIntarface.getUserIdBySession(modelAndView);
		dmIntarface.getDmHistory(modelAndView);
	}

	public void talkHistory(ModelAndView modelAndView, String receiveUser) {

		dmTalkInterface.getgetSessionByLoginUser(modelAndView);
		dmTalkInterface.getTalkHistoryByReceiveUser(modelAndView, receiveUser);
	}
}
