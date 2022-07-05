package com.shift.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.CalendarIntarface;

@Component
public class CalendarService extends BaseService {

	@Autowired
	CalendarIntarface calendarIntarface;


	public CalendarService(CalendarIntarface calendarIntarface) {
		super();
		this.calendarIntarface = calendarIntarface;
	}


	public void calendar(ModelAndView modelAndView, String ym) {

		calendarIntarface.getMonth(modelAndView, ym);
		calendarIntarface.getSchedule(modelAndView);
		calendarIntarface.generateCalendar(modelAndView);
		calendarIntarface.getNextBeforMonth(modelAndView);
	}
}
