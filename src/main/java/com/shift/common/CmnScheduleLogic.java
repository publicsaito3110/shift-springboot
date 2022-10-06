package com.shift.common;

import java.util.List;

import com.shift.domain.model.entity.ScheduleEntity;
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


	/**
	 * 確定スケジュール登録済みユーザArray処理
	 *
	 * <p>scheduleEntityListとscheduleTimeListから登録済みのスケジュールとスケジュール時間区分を取得し、登録されているユーザをArrayで取得する<br>
	 * ただし、scheduleTimeListの要素数だけ判別する(最大7まで)<br>
	 * 日付とスケジュール時間区分に登録しているユーザが存在するとき、ユーザー名を格納する(ユーザ毎に改行タグ&lt;br&gt;が追加される)
	 * </p>
	 *
	 * @param scheduleEntityList DBから取得したList<ScheduleEntity> (List&lt;ScheduleEntity&gt;)
	 * @param scheduleTimeList DBから取得したList<ScheduleTimeEntity> (List&lt;ScheduleTimeEntity&gt;)
	 * @return String[][]<br>
	 * エレメント(String[日付(31固定)][スケジュール時間(スケジュール登録可能数)])<br>
	 * 日付とスケジュール時間区分に登録しているユーザ名が順次格納される
	 */
	public String[][] calcUserALLScheduleArrayBySchedule(List<ScheduleEntity> scheduleEntityList, List<ScheduleTimeEntity> scheduleTimeList) {

		//スケジュールに登録されているユーザを格納するための変数(要素[日付][スケジュール時間区分])
		String[][] userScheduleAllArray = new String[31][Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

		//スケジュールの判定を行うための共通クラス
		CmnScheduleLogic cmnScheduleLogic = new CmnScheduleLogic();

		//scheduleEntityListの要素数(ユーザ数)だけループする
		for (ScheduleEntity scheduleEntity: scheduleEntityList) {

			//登録されているユーザ名を取得
			String userName = scheduleEntity.getUser();

			//ユーザの1ヵ月分のスケジュールを日付ごとに取得
			List<String> scheduleDayList = scheduleEntity.getDayList();

			//scheduleDayList(日付の回数)だけループする
			for (int i = 0; i < scheduleDayList.size(); i++) {

				//日付ごとのスケジュールを取得し、スケジュール時間ごとにスケジュールが登録されているか判定する
				String scheduleDay = scheduleDayList.get(i);
				Boolean[] isScheduleRecordedArray = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(scheduleDay, scheduleTimeList);

				//isScheduleRecordedArray(スケジュール時間の区分)だけループする
				for (int j = 0; j < isScheduleRecordedArray.length; j++) {

					//isScheduleRecordedArray[j]がnull(スケジュール時間区分がない)とき、何もせずisScheduleRecordedArrayのループに戻る
					if (isScheduleRecordedArray[j] == null) {
						continue;
					}

					//スケジュールが登録されているとき
					if (isScheduleRecordedArray[j]) {

						//スケジュールに登録されているユーザに現在のユーザを改行タグ(<br>)と共に追加する
						String userSchedule = CommonUtil.changeEmptyByNull(userScheduleAllArray[i][j]) +  userName + Const.HTML_TAG_BR;
						userScheduleAllArray[i][j] = userSchedule;
					}
				}
			}
		}

		return userScheduleAllArray;
	}
}
