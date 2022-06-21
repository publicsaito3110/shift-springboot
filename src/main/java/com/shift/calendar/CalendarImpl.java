package com.shift.calendar;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class CalendarImpl implements CalendarIntarface {

	//現在の日付を取得
	LocalDate now = LocalDate.now();
	int year = now.getYear();
	int month = now.getMonthValue();


	@Override
	public void getDay(Model model) {

		model.addAttribute("day", "20220601");
	}
	@Override
	public void getSchedule(Model model) {

		model.addAttribute("schedule", "OK");
	}
	@Override
	public void generateCalendar(Model model) {

		model.addAttribute("carlendar", "カレンダー!");
	}
}
