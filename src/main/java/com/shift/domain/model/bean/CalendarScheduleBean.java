package com.shift.domain.model.bean;

import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class CalendarScheduleBean {


	//フィールド
	private String ymd;

	private String user;

	private Boolean[] isScheduleRecordedArray = new Boolean[Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

	private String day;

	private String htmlClass;
}
