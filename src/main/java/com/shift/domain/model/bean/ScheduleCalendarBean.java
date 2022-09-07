package com.shift.domain.model.bean;

import java.util.List;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class ScheduleCalendarBean {

	//フィールド
	private String day;

	private String schedule;

	private String htmlClass;

	private List<Boolean> isScheduleDisplayList;
}
