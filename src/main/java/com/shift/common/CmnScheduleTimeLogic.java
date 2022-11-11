package com.shift.common;

import java.util.List;

import com.shift.domain.model.bean.ScheduleTimeBean;
import com.shift.domain.model.entity.ScheduleTimeEntity;

/**
 * @author saito
 *
 */
public class CmnScheduleTimeLogic {


	/**
	 * スケジュール時間区分勤務時間変換処理
	 *
	 * <p>scheduleTimeEntityからそれぞれのスケジュール時間区分の勤務時間を算出し、Longの配列に格納して返す<br>
	 * それぞれのスケジュール時間区分の勤務時間は"勤務終了時間 - 勤務開始時間 - 休憩時間"で計算される<br>
	 * ただし、scheduleTimeEntityがnullまたはスケジュール時間区分が1つも登録されていないときは0が返される
	 * </p>
	 *
	 * @param scheduleTimeEntity スケジュール時間区分<br>
	 * ただし、nullまたはスケジュール時間区分が1つも登録されていないときは0が返される
	 * @return Long[] それぞれのスケジュール時間区分の労働時間
	 */
	public Long[] toWorkTimeMsArrayByScheduleTime(ScheduleTimeEntity scheduleTimeEntity) {

		//scheduleTimeEntityがnullのとき
		if (scheduleTimeEntity == null) {

			//スケジュール時間区分の労働時間に0を格納し、返す
			Long[] workTime0MsArray = new Long[1];
			workTime0MsArray[0] = 0L;
			return workTime0MsArray;
		}

		//スケジュール時間区分をListで取得
		List<ScheduleTimeBean> scheduleTimeList = scheduleTimeEntity.scheduleTimeFormatList();

		//スケジュール時間区分が1つも登録されていないとき
		if (scheduleTimeList.size() <= 0) {

			//スケジュール時間区分の労働時間に0を格納し、返す
			Long[] workTime0MsArray = new Long[1];
			workTime0MsArray[0] = 0L;
			return workTime0MsArray;
		}

		//スケジュール時間区分ごとの労働時間(ミリ秒)を格納する配列
		Long[] workTimeMsArray = new Long[scheduleTimeList.size()];

		//スケジュール時間を計算する共通クラス
		CommonLogic commonLogic = new CommonLogic();

		//スケジュール時間区分の数だけループする
		for (int i = 0; i < scheduleTimeList.size(); i++) {

			//現在のスケジュール時間区分を取得
			ScheduleTimeBean scheduleTimeBean = scheduleTimeList.get(i);

			//登録されているそれぞれの時間をミリ秒で取得
			long startHmMs = commonLogic.chengeHmMsByHourMinutes(scheduleTimeBean.getStartHmForHour(), scheduleTimeBean.getStartHmForMinutes());
			long endHmMs = commonLogic.chengeHmMsByHourMinutes(scheduleTimeBean.getEndHmForHour(), scheduleTimeBean.getEndHmForMinutes());
			long restHmMs = commonLogic.chengeHmMsByHourMinutes(scheduleTimeBean.getRestHmForHour(), scheduleTimeBean.getRestHmForMinutes());

			//勤務時間を算出
			long workTimeMs = endHmMs - startHmMs - restHmMs;

			//勤務時間をworkTimeMsArrayに格納
			workTimeMsArray[i] = workTimeMs;
		}

		return workTimeMsArray;
	}
}
