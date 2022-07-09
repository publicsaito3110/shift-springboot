package com.shift.domain.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmTalkInterface;
import com.shift.domain.model.entity.DmEntity;
import com.shift.domain.repository.DmRepository;

@Component
public class DmTalkImpl extends BaseImple implements DmTalkInterface {


	//フィールド
	private String id;

	@Autowired
	DmRepository dmRepository;


	@Override
	public void getSessionByLoginUser(ModelAndView modelAndView) {

		String id = "A001";
		this.id = id;

	}
	@Override
	public void getTalkHistoryByReceiveUser(ModelAndView modelAndView, String receiveUser) {

		List<DmEntity> talkHistoryList = new ArrayList<>();
		talkHistoryList = dmRepository.selectTalkHistoryByLoginUserReceiveUser(this.id, receiveUser);

		modelAndView.addObject("talkHistoryList", talkHistoryList);
	}
}
