package com.shift.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.DmInterface;

@Component
public class DmService{

	@Autowired
	DmInterface dmIntarface;


	public DmService(DmInterface dmIntarface) {
		super();
		this.dmIntarface = dmIntarface;
	}


	public void dmMenue(ModelAndView modelAndView) {

		dmIntarface.getUserIdBySession(modelAndView);
		dmIntarface.getDmHistory(modelAndView);
	}
}
