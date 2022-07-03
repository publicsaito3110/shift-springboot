package com.shift.domain.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmTalkInterface;
import com.shift.domain.repositry.DmRepositry;
import com.shift.entity.DmEntity;

@Component
public class DmTalkImpl implements DmTalkInterface {


	//フィールド
	private String id;

	@Autowired
	DmRepositry dmRepositry;


	@Override
	public void getSessionByLoginUser(ModelAndView modelAndView) {

		String id = "A001";
		this.id = id;

	}
	@Override
	public void getTalkHistoryByReceiveUser(ModelAndView modelAndView, String receiveUser) {

		List<DmEntity> talkHistoryList = new ArrayList<>();
		talkHistoryList = dmRepositry.selectTalkHistoryByLoginUserReceiveUser(this.id, receiveUser);

		modelAndView.addObject("talkHistoryList", talkHistoryList);
	}
}
