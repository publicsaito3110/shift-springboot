package com.shift.domain.model.bean;

import java.util.List;

import com.shift.domain.model.entity.ScheduleTimeEntity;

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

	private String nowYm;

	private String afterYm;

	private String beforeYm;

	private List<ScheduleTimeEntity> scheduleTimeList;
}
