package com.shift.domain.model.bean;

import com.shift.domain.model.entity.ScheduleTimeEntity;

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
public class ShiftEditBean {

	//フィールド
	private ScheduleTimeEntity scheduleTimeEntity;
}
