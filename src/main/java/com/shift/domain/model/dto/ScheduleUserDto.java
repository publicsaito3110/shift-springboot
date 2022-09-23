package com.shift.domain.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Data
public class ScheduleUserDto {


	//フィールド
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "user")
	private String user;

	@Column(name = "schedule1", unique = true)
	private String schedule1;

	@Column(name = "schedule2", unique = true)
	private String schedule2;

	@Column(name = "schedule3", unique = true)
	private String schedule3;

	@Column(name = "schedule4", unique = true)
	private String schedule4;

	@Column(name = "schedule5", unique = true)
	private String schedule5;

	@Column(name = "schedule6", unique = true)
	private String schedule6;

	@Column(name = "schedule7", unique = true)
	private String schedule7;


	//メソッド
	public Boolean[] scheduleFormatIsRecordedArray() {

		//scheduleをArrayに格納して返す
		Boolean[] isScheduleArray = new Boolean[Const.SCHEDULE_RECORDABLE_MAX_DIVISION];
		isScheduleArray[0] = isRecordedSchedule(schedule1);
		isScheduleArray[1] = isRecordedSchedule(schedule2);
		isScheduleArray[2] = isRecordedSchedule(schedule3);
		isScheduleArray[3] = isRecordedSchedule(schedule4);
		isScheduleArray[4] = isRecordedSchedule(schedule5);
		isScheduleArray[5] = isRecordedSchedule(schedule6);
		isScheduleArray[6] = isRecordedSchedule(schedule7);
		return isScheduleArray;
	}


	//private 共通メソッド
	private boolean isRecordedSchedule(String value) {

		//登録されている(valueが1)のとき
		if (Const.SCHEDULE_PRE_DAY_RECORDED.equals(value)) {
			return true;
		}

		//登録されていないときはfalseを返す
		return false;
	}
}
