package com.shift.form;

import java.util.List;

import javax.validation.constraints.Pattern;

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

	//フィールド
	@Pattern(regexp = Const.PATTERN_SCHEDULE_YM_INPUT, message = "入力値が不正です")
	private String ym;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_DAY_INPUT, message = "入力値が不正です")
	private String day;

	private String[][] userArray;

	private String[][] scheduleArray;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_UNIQUE_ADD_USER_ID_INPUT, message = "入力値が不正です")
	private String addUserId;

	private String[] addScheduleArray = new String[Const.SCHEDULE_RECORDABLE_MAX_DIVISION];


	//コンストラクタ
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


	//メソッド
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
