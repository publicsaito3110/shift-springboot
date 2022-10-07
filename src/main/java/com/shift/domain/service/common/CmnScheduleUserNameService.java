package com.shift.domain.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CmnScheduleLogic;
import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.CmnScheduleUserNameBean;
import com.shift.domain.model.dto.ScheduleUserNameDto;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.repository.ScheduleUserNameRepository;
import com.shift.domain.service.BaseService;

/**
 * @author saito
 *
 */
@Service
public class CmnScheduleUserNameService extends BaseService {

	@Autowired
	private ScheduleUserNameRepository scheduleUserNameRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;


	/**
	 * [共通Service] 確定スケジュール登録済み全ユーザ処理
	 *
	 * @param year 年
	 * @param month 月
	 * @return CmnScheduleUserNameBean<br>
	 * フィールド(CmnScheduleUserNameBean)<br>
	 * scheduleEntityList, scheduleTimeList
	 */
	public CmnScheduleUserNameBean generateScheduleRecordedUserNameByYm(int year, int month) {

		List<ScheduleUserNameDto> scheduleUserNameDtoList = selectScheduleAll(year, month);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		String[][] userScheduleAllArray = calcUserALLScheduleArrayBySchedule(scheduleUserNameDtoList, scheduleTimeList);

		//Beanにセット
		CmnScheduleUserNameBean cmnScheduleUserNameBean = new CmnScheduleUserNameBean(scheduleTimeList, userScheduleAllArray);
		return cmnScheduleUserNameBean;
	}


	/**
	 * 確定スケジュール登録済みユーザArray処理
	 *
	 * <p>scheduleEntityListとscheduleTimeListから登録済みのスケジュールとスケジュール時間区分を取得し、登録されているユーザをArrayで取得する<br>
	 * ただし、scheduleTimeListの要素数だけ判別する(最大7まで)<br>
	 * 日付とスケジュール時間区分に登録しているユーザが存在するとき、ユーザー名を格納する(ユーザ毎に改行タグ&lt;br&gt;が追加される)
	 * </p>
	 *
	 * @param scheduleUserNameDtoList DBから取得したList<ScheduleUserNameDto> (List&lt;ScheduleUserNameDto&gt;)
	 * @param scheduleTimeList DBから取得したList<ScheduleTimeEntity> (List&lt;ScheduleTimeEntity&gt;)
	 * @return String[][]<br>
	 * エレメント(String[日付(31固定)][スケジュール時間(スケジュール登録可能数)])<br>
	 * 日付とスケジュール時間区分に登録しているユーザ名が順次格納される
	 */
	private String[][] calcUserALLScheduleArrayBySchedule(List<ScheduleUserNameDto> scheduleUserNameDtoList, List<ScheduleTimeEntity> scheduleTimeList) {

		//スケジュールに登録されているユーザを格納するための変数(要素[日付][スケジュール時間区分])
		String[][] userScheduleAllArray = new String[31][Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

		//スケジュールの判定を行うための共通クラス
		CmnScheduleLogic cmnScheduleLogic = new CmnScheduleLogic();

		//scheduleUserNameDtoの要素数(ユーザ数)だけループする
		for (ScheduleUserNameDto scheduleUserNameDto: scheduleUserNameDtoList) {

			//登録されているユーザ名を取得
			String userName = scheduleUserNameDto.getUserName();

			//ユーザの1ヵ月分のスケジュールを日付ごとに取得
			List<String> scheduleDayList = scheduleUserNameDto.getDayList();

			//scheduleDayList(日付の回数)だけループする
			for (int i = 0; i < scheduleDayList.size(); i++) {

				//日付ごとのスケジュールを取得し、スケジュール時間ごとにスケジュールが登録されているかどうかを判定したBooleanの配列を取得
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


	/**
	 * [DB]確定スケジュール登録済みユーザ検索処理
	 *
	 * <p>year, monthから1ヵ月分のスケジュールを取得する</p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return List<ScheduleUserNameDto> <br>
	 * フィールド(List&lt;ScheduleUserNameDto&gt;)<br>
	 * id, ymd, userName, day1, day2, day3... day30, day31
	 *
	 */
	private List<ScheduleUserNameDto> selectScheduleAll(int year, int month) {

		//year, monthをym(YYYYMM)に変換
		String ym = new CommonLogic().toStringYmFormatSixByYearMonth(year, month);

		//DBから取得し、返す
		List<ScheduleUserNameDto> scheduleUserNameDtoList = scheduleUserNameRepository.selectScheduleUserNameByYm(ym);
		return scheduleUserNameDtoList;
	}


	/**
	 * [DB]シフト時間取得処理
	 *
	 * <p>管理者が設定したシフト時間を取得する</p>
	 *
	 * @return List<ScheduleTimeEntity> <br>
	 * フィールド(List&lt;ScheduleTimeEntity&gt;)<br>
	 * id, name, startHms, endHms, restHms
	 *
	 */
	private List<ScheduleTimeEntity> selectScheduleTime() {

		List<ScheduleTimeEntity> scheduleTimeEntityList = scheduleTimeRepository.findAll();
		return scheduleTimeEntityList;
	}
}
