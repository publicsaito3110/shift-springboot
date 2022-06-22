package com.shift.domain.impl;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.shift.domain.Interface.CalendarIntarface;

@Component
public class CalendarImpl implements CalendarIntarface {

	//現在の日付を取得
	private int year;
	private int month;


	@Override
	public void getDay(HttpServletRequest request, HttpServletResponse response) {

		//現在の日付を取得
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();

		this.year = 20220601;

	}
	@Override
	public void getSchedule(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("schedule", "OK");
	}
	@Override
	public void generateCalendar(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("carlendar", this.year);
	}
}
