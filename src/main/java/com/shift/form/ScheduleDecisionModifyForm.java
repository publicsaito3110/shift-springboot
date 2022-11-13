package com.shift.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.shift.common.Const;
import com.shift.domain.model.dto.ScheduleDayDto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
public class ScheduleDecisionModifyForm {

	@NotBlank(message = "入力値が不正です")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_YM_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_YM_LENGTH_MIN_INPUT, max = Const.PATTERN_SCHEDULE_YM_LENGTH_MAX_INPUT, message = "入力値が不正です")
	private String ym;

	@NotBlank(message = "入力値が不正です")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_DAY_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_DAY_LENGTH_MIN_INPUT, max = Const.PATTERN_SCHEDULE_DAY_LENGTH_MAX_INPUT, message = "入力値が不正です")
	private String day;

	private String[][] userArray;

	private String[][] scheduleArray;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_USER_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_USER_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_USER_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String addUserId;

	private String[] addScheduleArray = new String[Const.SCHEDULE_RECORDABLE_MAX_DIVISION];



	/**
	 * [コンストラクタ] ScheduleDecisionModifyForm
	 *
	 * <p>1日のスケジュール及びスケジュールに新規登録可能ユーザの値をセットする</p>
	 *
	 * @param scheduleDayList 1日のスケジュール
	 * @param year 登録する年
	 * @param month 登録する月
	 * @param day 登録する日
	 * @return ScheduleDecisionModifyForm
	 */
	public ScheduleDecisionModifyForm(List<ScheduleDayDto> scheduleDayList, String year, String month, String day) {

		//登録するスケジュールの年月をセット
		ym = year + month;
		this.day = day;

		//scheduleUserListがnullまたはEmptyのとき
		if (scheduleDayList == null || scheduleDayList.isEmpty()) {
			return;
		}

		//userArrayとscheduleArrayの要素数を確定スケジュールに登録済みのユーザ数で指定
		userArray = new String[scheduleDayList.size()][2];
		scheduleArray = new String[scheduleDayList.size()][Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

		//--------------------------------------------------------------------
		//登録したスケジュール通りになるようにuserScheduleArrayに値を代入する
		//--------------------------------------------------------------------

		//scheduleUserListの要素数(確定スケジュール登録済みユーザ)の回数だけループする
		for (int i = 0; i < scheduleDayList.size(); i++) {

			//確定スケジュールに登録済みのユーザ名とIDをuserArrayに代入
			userArray[i][0] = scheduleDayList.get(i).getUserId();
			userArray[i][1] = scheduleDayList.get(i).getUserName();

			//isScheduleRecordedArrayListからi日目の登録情報を取得
			Boolean[] isScheduleRecordedArray = scheduleDayList.get(i).scheduleFormatIsRecordedArray();

			//isScheduleRecordedArrayの要数だけループする
			for (int j = 0; j < isScheduleRecordedArray.length; j++) {

				//isScheduleRecordedArray[j]を取得
				boolean isScheduleRecorded = isScheduleRecordedArray[j];

				//スケジュール時間区分(j)が登録されていないとき、未登録の情報を代入する
				if (!isScheduleRecorded) {
					scheduleArray[i][j] = Const.SCHEDULE_DAY_NOT_RECORDED;
					continue;
				}

				//スケジュール時間区分(j)が登録されているとき、登録済みの情報を代入する
				scheduleArray[i][j] = Const.SCHEDULE_DAY_RECORDED;
			}
		}
	}



	/**
	 * 新規追加スケジュール取得処理
	 *
	 * <p>登録されたスケジュール(スケジュール時間区分ごとに登録されたaddScheduleArray)をStringの文字列に変換して返す</p>
	 *
	 * @param void
	 * @return String 新規追加スケジュール
	 */
	public String getAddScheduleAll() {

		//addScheduleArrayを文字列で受け取るための変数
		String addSchedule = "";

		for (String schedule: addScheduleArray) {

			//スケジュールが登録されていないとき、スケジュール未登録情報を格納
			if (!Const.SCHEDULE_DAY_RECORDED.equals(schedule)) {
				addSchedule += Const.SCHEDULE_DAY_NOT_RECORDED;
				continue;
			}

			//スケジュールが登録されているとき、スケジュール登録済み情報を格納
			addSchedule += Const.SCHEDULE_DAY_RECORDED;
		}

		return addSchedule;
	}
}
