package com.shift.calendar;

import org.springframework.ui.Model;

import com.shift.base.BaseInterface;

public interface CalendarIntarface extends BaseInterface{

	void getDay(Model model);

	void getSchedule(Model model);

	void generateCalendar(Model model);
}
