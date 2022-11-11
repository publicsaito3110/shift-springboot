package com.shift.domain.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDecisionReportSearchTimeBean {

	private int year;

	private int month;

	private String[] scheduleWorkTimeMonthArray;
}
