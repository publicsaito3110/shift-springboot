package com.shift.domain.model.bean;

import java.util.List;

import com.shift.domain.model.dto.ScheduleUserDto;
import com.shift.domain.model.entity.ScheduleTimeEntity;

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
public class ScheduleDecisionModifyBean {

	//フィールド
	private List<ScheduleUserDto> scheduleUserList;

	private List<ScheduleTimeEntity> scheduleTimeList;
}
