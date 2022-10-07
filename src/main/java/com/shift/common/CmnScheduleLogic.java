package com.shift.common;

import java.util.List;

import com.shift.domain.model.entity.ScheduleTimeEntity;

/**
 * @author saito
 *
 */
public class CmnScheduleLogic {


	/**
	 * スケジュール登録済み判別Array処理
	 *
	 * <p>scheduleからスケジュール登録済みかどうかをBooleanのArrayに変換して返す<br>
	 * ただし、scheduleTimeListの要素数だけ判別する(最大7まで)<br>
	 * scheduleは一文字ずつ取得し、1のとき登録済み,それ以外だと未登録と判定される
	 * </p>
	 *
	 * @param schedule スケジュール<br>
	 * ただし、nullまたは文字数がscheduleTimeListの要素数と一致していないときはスケジュール未登録と判定される
	 * @param scheduleTimeList DBから取得したList<ScheduleTimeEntity> (List&lt;ScheduleTimeEntity&gt;)<br>
	 * ただし、scheduleTimeListがnullまたはEmptyのときはBoolean[0]:falseのみが返される
	 * @return Boolean[] スケジュール登録済みか判定したArray(最大要素7)<br>
	 * true: スケジュール登録済み
	 * false: スケジュール未登録
	 */
	public Boolean[] toIsScheduleRecordedArrayBySchedule(String schedule, List<ScheduleTimeEntity> scheduleTimeList) {

		//スケジュールが登録されているかどうかを判別する配列(1日ごとのスケジュールにおいて要素0 -> scheduleTimeList(0), 要素1 -> scheduleTimeList(1)...)
		Boolean[] isScheduleRecordedArray = new Boolean[Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

		//scheduleTimeListがnullまたはEmptyのとき
		if (scheduleTimeList == null || scheduleTimeList.isEmpty()) {

			//要素[0]にfalseをセットし、返す
			isScheduleRecordedArray[0] = false;
			return isScheduleRecordedArray;
		}

		//scheduleTimeListの要素数の回数だけループ
		for (int i = 0; i < scheduleTimeList.size(); i++) {

			//scheduleがnullまたは空文字のとき
			if (schedule == null || schedule.isEmpty()) {
				isScheduleRecordedArray[i] = false;
				continue;
			}

			//scheduleの文字数がiより小さい(1文字取得できない)とき
			if (schedule.length() <= i) {
				isScheduleRecordedArray[i] = false;
				continue;
			}

			//ループの回数から1文字だけ取得
			String scheduleValueChara = String.valueOf(schedule.charAt(i));

			//スケジュールが(scheduleValueCharaが1でない)登録されていないとき
			if (!Const.SCHEDULE_PRE_DAY_RECORDED.equals(scheduleValueChara)) {
				isScheduleRecordedArray[i] = false;
				continue;
			}

			//スケジュールが登録されているときtrueを代入
			isScheduleRecordedArray[i] = true;
		}

		return isScheduleRecordedArray;
	}
}
