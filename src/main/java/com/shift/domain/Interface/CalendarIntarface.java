package com.shift.domain.Interface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface CalendarIntarface extends BaseInterface {

	void getMonth(HttpServletRequest request, HttpServletResponse response);

	void getSchedule(HttpServletRequest request, HttpServletResponse response);

	void generateCalendar(HttpServletRequest request, HttpServletResponse response);

	void getNextBeforMonth(HttpServletRequest request, HttpServletResponse response);
}
