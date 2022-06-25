package com.shift.domain.service;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.Interface.CalendarIntarface;

@Component
public class CalendarService{

	@Autowired
	CalendarIntarface calendarIntarface;


	public CalendarService(CalendarIntarface calendarIntarface) {
		super();
		this.calendarIntarface = calendarIntarface;
	}


	public void calendar(ModelAndView modelAndView, String ym)  throws ServletException, IOException {

		calendarIntarface.getMonth(modelAndView, ym);
		calendarIntarface.getSchedule(modelAndView);
		calendarIntarface.generateCalendar(modelAndView);
		calendarIntarface.getNextBeforMonth(modelAndView);
	}
}
