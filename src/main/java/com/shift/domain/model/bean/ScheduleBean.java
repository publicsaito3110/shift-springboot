package com.shift.domain.model.bean;

import java.util.List;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class ScheduleBean {

	//フィールド
	private int year;

	private int month;

	private List<ScheduleCalendarBean> calendarList;

	private String afterYm;

	private String beforeYm;
}
