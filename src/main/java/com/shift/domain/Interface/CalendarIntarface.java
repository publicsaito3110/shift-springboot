package com.shift.domain.Interface;

import org.springframework.web.servlet.ModelAndView;


public interface CalendarIntarface extends BaseInterface {

	void getMonth(ModelAndView modelAndView, String ym);

	void getSchedule(ModelAndView modelAndView);

	void generateCalendar(ModelAndView modelAndView);

	void getNextBeforMonth(ModelAndView modelAndView);
}
