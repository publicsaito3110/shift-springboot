package com.shift.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class CalendarService{

	@Autowired
	CalendarIntarface calendarIntarface;


	public CalendarService(CalendarIntarface calendarIntarface) {
		super();
		this.calendarIntarface = calendarIntarface;
	}


	public void calendar(Model model) {

		calendarIntarface.executeEncoding(model);
		calendarIntarface.getDay(model);
		calendarIntarface.getSchedule(model);
		calendarIntarface.generateCalendar(model);
	}
}
