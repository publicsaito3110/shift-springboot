package com.shift.domain.model.bean;

import java.util.List;

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
public class CmnScheduleCalendarBean {

	private int year;

	private int month;

	private List<Integer> calendarList;

	private String nowYm;

	private String nextYm;

	private String beforeYm;
}
