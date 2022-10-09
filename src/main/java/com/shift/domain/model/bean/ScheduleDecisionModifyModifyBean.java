package com.shift.domain.model.bean;

import java.util.List;

import com.shift.domain.model.dto.SchedulePreDayDto;
import com.shift.domain.model.dto.ScheduleDayDto;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduleDecisionModifyModifyBean {

	//フィールド
	private String year;

	private String month;

	private String day;

	private List<SchedulePreDayDto> schedulePreDayList;

	private List<ScheduleDayDto> scheduleDayList;

	private List<ScheduleTimeEntity> scheduleTimeList;

	private List<UserEntity> userList;
}
