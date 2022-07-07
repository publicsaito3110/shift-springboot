package com.shift.domain.Interface;

import org.springframework.web.servlet.ModelAndView;

public interface HomeInterface extends BaseInterface {

	void getDate(ModelAndView modelAndView);

	void getNews(ModelAndView modelAndView);

	void chengeDisplayNews(ModelAndView modelAndView);
}
