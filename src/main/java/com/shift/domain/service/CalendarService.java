package com.shift.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CmnScheduleLogic;
import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.domain.model.bean.CalendarAllBean;
import com.shift.domain.model.bean.CalendarBean;
import com.shift.domain.model.bean.CmnScheduleCalendarBean;
import com.shift.domain.model.bean.CmnScheduleUserNameBean;
import com.shift.domain.model.entity.ScheduleEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.ScheduleRepository;
import com.shift.domain.repository.ScheduleTimeRepository;
import com.shift.domain.service.common.CmnScheduleCalendarService;
import com.shift.domain.service.common.CmnScheduleUserNameService;

/**
 * @author saito
 *
 */
@Service
public class CalendarService extends BaseService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;

	@Autowired
	private CmnScheduleCalendarService cmnScheduleCalendarService;

	@Autowired
	private CmnScheduleUserNameService cmnScheduleUserNameService;


	/**
	 * [Service] (/calendar)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return CalendarBean
	 */
	public CalendarBean calendar(String ym, String loginUser) {

		//CmnScheduleCalendarService(共通サービス)から処理結果を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//Service内の処理を実行
		ScheduleEntity scheduleEntity = selectSchedule(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth(), loginUser);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		Boolean[][] isScheduleDisplayArray = calcIsScheduleRecordedArrayBySchedule(scheduleEntity, scheduleTimeList);

		//Beanにセット
		CalendarBean calendarBean = new CalendarBean();
		calendarBean.setYear(cmnScheduleCalendarBean.getYear());
		calendarBean.setMonth(cmnScheduleCalendarBean.getMonth());
		calendarBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		calendarBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		calendarBean.setIsScheduleDisplayArray(isScheduleDisplayArray);
		calendarBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		calendarBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		calendarBean.setScheduleTimeList(scheduleTimeList);
		return calendarBean;
	}


	/**
	 * [Service] (/calendar/all)
	 *
	 * @param ym RequestParameter
	 * @return CalendarAllBean
	 */
	public CalendarAllBean calendarAll(String ym) {

		//CmnScheduleCalendarService(共通サービス)から処理結果を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//CmnScheduleUserNameService(共通サービス)から処理結果を取得
		CmnScheduleUserNameBean cmnScheduleUserNameBean = cmnScheduleUserNameService.generateScheduleRecordedUserNameByYm(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth());

		//Beanにセット
		CalendarAllBean calendarBean = new CalendarAllBean();
		calendarBean.setYear(cmnScheduleCalendarBean.getYear());
		calendarBean.setMonth(cmnScheduleCalendarBean.getMonth());
		calendarBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		calendarBean.setUserScheduleAllArray(cmnScheduleUserNameBean.getUserScheduleAllArray());
		calendarBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		calendarBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		calendarBean.setScheduleTimeList(cmnScheduleUserNameBean.getScheduleTimeList());
		return calendarBean;
	}


	/**
	 * スケジュール登録済み判定List取得処理
	 *
	 * <p>scheduleEntityとscheduleTimeListから登録済みのスケジュールとスケジュール時間区分を取得し、登録されているかを判別する<br>
	 * Listのエレメント(Boolean[])には1日ごとのスケジュール時間区分で登録済みかを判別する
	 * </p>
	 *
	 * @param scheduleEntity DBから取得したscheduleEntity
	 * @param scheduleTimeList DBから取得したList<ScheduleTimeEntity> (List&lt;ScheduleTimeEntity&gt;)
	 * @return Boolean[][]<br>
	 * エレメント(Boolean[][])<br>
	 * true: スケジュール登録済み, false: スケジュール未登録<br>
	 * ただし、要素はBoolean[日付(31固定)][スケジュール時間(スケジュール登録可能数)]
	 */
	private Boolean[][] calcIsScheduleRecordedArrayBySchedule(ScheduleEntity scheduleEntity, List<ScheduleTimeEntity> scheduleTimeList) {

		//ScheduleEntityをインスタンス化
		ScheduleEntity trimScheduleEntity = new ScheduleEntity();

		//scheduleEntityがnullでないとき、trimSchedulePreEntityにschedulePreEntityを代入
		if (scheduleEntity != null) {
			trimScheduleEntity = scheduleEntity;
		}

		//スケジュール登録を判別するBoolean[日付][スケジュール時間]の配列
		Boolean[][] isScheduleDisplayArray = new Boolean[31][Const.SCHEDULE_RECORDABLE_MAX_DIVISION];

		//trimSchedulePreEntityに登録されている値(1ヵ月分)をListで取得
		List<String> scheduleList = trimScheduleEntity.getDayList();

		//スケジュール登録済みかを判定する共通Logicクラス
		CmnScheduleLogic cmnScheduleLogic = new CmnScheduleLogic();

		//scheduleListの要素数(1ヵ月の日付)の回数だけループする
		for (int i = 0; i < scheduleList.size(); i++) {

			//スケジュールが登録されているかどうかを判別する配列(1日ごとのスケジュールにおいて要素0 -> scheduleTimeList(0), 要素1 -> scheduleTimeList(1)...)
			Boolean[] isScheduleRecordedArray = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(scheduleList.get(i), scheduleTimeList);

			//isScheduleDisplayArrayの1次元目i(日付)を指定し、2次元目(スケジュール時間区分)にセットする
			isScheduleDisplayArray[i] = isScheduleRecordedArray;
		}

		return isScheduleDisplayArray;
	}


	/**
	 * [DB]ユーザの確定スケジュール検索処理
	 *
	 * <p>year, monthから1ヵ月分のスケジュールを取得する</p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @param loginUser Authenticationから取得したユーザID
	 * @return List<ScheduleEntity> <br>
	 * フィールド(List&lt;ScheduleEntity&gt;)<br>
	 * id, ymd, user, schedule
	 *
	 */
	private ScheduleEntity selectSchedule(int year, int month, String loginUser) {

		//year, monthをym(YYYYMM)に変換
		String ym = new CommonLogic().toStringYmFormatSixByYearMonth(year, month);

		//DBから取得し、返す
		ScheduleEntity scheduleEntity = scheduleRepository.findByYmAndUser(ym, loginUser);
		return scheduleEntity;
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
