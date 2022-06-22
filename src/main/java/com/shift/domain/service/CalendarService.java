package com.shift.domain.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shift.domain.Interface.CalendarIntarface;

@Component
public class CalendarService{

	@Autowired
	CalendarIntarface calendarIntarface;


	public CalendarService(CalendarIntarface calendarIntarface) {
		super();
		this.calendarIntarface = calendarIntarface;
	}


	public void calendar(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {

		calendarIntarface.executeEncoding(request, response);
		calendarIntarface.getMonth(request, response);
		calendarIntarface.getSchedule(request, response);
		calendarIntarface.generateCalendar(request, response);
	}
}
