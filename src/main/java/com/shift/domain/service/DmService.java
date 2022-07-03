package com.shift.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmInterface;
import com.shift.domain.Interface.DmSendInterface;
import com.shift.domain.Interface.DmTalkInterface;

@Component
public class DmService{

	@Autowired
	DmInterface dmIntarface;

	@Autowired
	DmTalkInterface dmTalkInterface;

	@Autowired
	DmSendInterface dmSendInterface;


	public DmService(DmInterface dmIntarface, DmTalkInterface dmTalkInterface, DmSendInterface dmSendInterface) {
		super();
		this.dmIntarface = dmIntarface;
		this.dmTalkInterface = dmTalkInterface;
		this.dmSendInterface = dmSendInterface;
	}


	public void dmMenue(ModelAndView modelAndView) {

		dmIntarface.getUserIdBySession(modelAndView);
		dmIntarface.getDmHistory(modelAndView);
	}


	public void talkHistory(ModelAndView modelAndView, String receiveUser) {

		dmTalkInterface.getSessionByLoginUser(modelAndView);
		dmTalkInterface.getTalkHistoryByReceiveUser(modelAndView, receiveUser);
	}


	public void recordChatTalkHistory(ModelAndView modelAndView, String receiveUser, String msg) {

		dmSendInterface.chatRecord(receiveUser, msg);
		dmTalkInterface.getSessionByLoginUser(modelAndView);
		dmTalkInterface.getTalkHistoryByReceiveUser(modelAndView, receiveUser);
	}
}
