package com.shift.domain.model.bean;

import java.util.List;

import com.shift.domain.model.entity.ScheduleTimeEntity;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class CalendarAllBean {

	//フィールド
	private int year;

	private int month;

	private List<Integer> calendarList;

	private String[][] userScheduleAllArray;

	private String afterYm;

	private String beforeYm;

	private List<ScheduleTimeEntity> scheduleTimeList;
}
