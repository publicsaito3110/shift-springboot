package com.shift.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.HomeInterface;

@Component
public class HomeService extends BaseService {

	@Autowired
	HomeInterface homeInterface;


	public void home(ModelAndView modelAndView) {

		homeInterface.getDate(modelAndView);
		homeInterface.getNews(modelAndView);
		homeInterface.chengeDisplayNews(modelAndView);
	}
}
