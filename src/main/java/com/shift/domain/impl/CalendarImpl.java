package com.shift.domain.impl;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.shift.domain.Interface.CalendarIntarface;

@Component
public class CalendarImpl implements CalendarIntarface {

	//フィールド
	private int year;
	private int month;


	@Override
	public void getMonth(HttpServletRequest request, HttpServletResponse response) {

		//パラメーターを受け取る
		String ym = request.getParameter("ym");

		//ym(年月)が指定されていたとき
		if (ym != null) {

			//ymからyear(4文字)month(2文字)を取得
			this.year  = Integer.parseInt(ym.substring(0, 4));
			this.month = Integer.parseInt(ym.substring(4, 6));

			// 引き渡す値を設定
			request.setAttribute("year", this.year);
			request.setAttribute("month", this.month);
			return;
		}

		//現在の日付を取得
		LocalDate now = LocalDate.now();
		this.year = now.getYear();
		this.month = now.getMonthValue();

		// 引き渡す値を設定
		request.setAttribute("year", this.year);
		request.setAttribute("month", this.month);
	}


	@Override
	public void getSchedule(HttpServletRequest request, HttpServletResponse response) {

	}
	@Override
	public void generateCalendar(HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("carlendar", this.year + "年" + this.month + "月");
	}
}
