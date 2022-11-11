package com.shift.domain.model.bean;

import java.util.List;

import com.shift.domain.model.dto.ScheduleWorkCountMonthDto;

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
public class ScheduleDecisionReportSearchCountBean {

	private int year;

	private int month;

	private List<ScheduleWorkCountMonthDto> scheduleWorkCountMonthList;
}
