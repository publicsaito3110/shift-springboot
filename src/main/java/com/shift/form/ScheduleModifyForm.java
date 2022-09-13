package com.shift.form;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.shift.common.Const;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
public class ScheduleModifyForm {

	//フィールド
	@Pattern(regexp = Const.PATTERN_SCHEDULE_PRE_YM_INPUT, message = "入力値が不正です")
	private String ym;

	private String[][] dayArray = new String[31][Const.SCHEDULE_RECORDABLE_MAX_DIVISION];


	//コンストラクタ
	public ScheduleModifyForm(List<Boolean[]> isScheduleRecordedArrayList, String ym) {

		//登録するスケジュールの年月をセット
		this.ym = ym;

		//isScheduleRecordedArrayListがnullまたはEmptyのとき
		if (isScheduleRecordedArrayList == null || isScheduleRecordedArrayList.isEmpty()) {
			return;
		}

		//isScheduleRecordedArrayListの要素数が31より大きいとき
		if (31 < isScheduleRecordedArrayList.size()) {
			return;
		}

		//----------------------------------------------------------
		//登録したスケジュール通りになるようにdayArrayに値を代入する
		//----------------------------------------------------------

		//isScheduleRecordedArrayListの要素数(1ヵ月分の日付)の回数だけループする
		for (int i = 0; i < isScheduleRecordedArrayList.size(); i++) {

			//isScheduleRecordedArrayListからi日目の登録情報を取得
			Boolean[] isScheduleRecordedArray = isScheduleRecordedArrayList.get(i);

			//isScheduleRecordedArrayの要素数が規定より大きいときは値を代入せず最初のループに戻る
			if (Const.SCHEDULE_RECORDABLE_MAX_DIVISION < isScheduleRecordedArray.length) {
				continue;
			}

			//スケジュール時間区分だけループする
			for (int j = 0; j < isScheduleRecordedArray.length; j++) {

				//isScheduleRecordedArray[j]がnullのとき最初のループに戻る
				if (isScheduleRecordedArray[j] == null) {
					break;
				}

				//i日目のスケジュール時間区分jがtrueのとき
				if (isScheduleRecordedArray[j]) {

					dayArray[i][j] = Const.SCHEDULE_PRE_DAY_RECORDED;
				}
			}
		}
	}

}
