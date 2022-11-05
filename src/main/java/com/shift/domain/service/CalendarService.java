package com.shift.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
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
	 * ログインユーザの確定スケジュール表示機能<br>
	 * [Service] (/calendar)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return CalendarBean
	 */
	public CalendarBean calendar(String ym, String loginUser) {

		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//ログインユーザの1ヵ月分の確定スケジュールを取得
		ScheduleEntity scheduleEntity = selectSchedule(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth(), loginUser);
		//スケジュール時間区分を取得
		ScheduleTimeEntity scheduleTimeEntity = selectScheduleTime(cmnScheduleCalendarBean.getLastDateYmd());
		//スケジュールから登録済みスケジュールの判定をBooleanの2次元配列で取得
		Boolean[][] isScheduleDisplayArray = calcIsScheduleRecordedArrayBySchedule(scheduleEntity, scheduleTimeEntity);

		//Beanにセット
		CalendarBean calendarBean = new CalendarBean();
		calendarBean.setYear(cmnScheduleCalendarBean.getYear());
		calendarBean.setMonth(cmnScheduleCalendarBean.getMonth());
		calendarBean.setNowYm(cmnScheduleCalendarBean.getNowYm());
		calendarBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		calendarBean.setIsScheduleDisplayArray(isScheduleDisplayArray);
		calendarBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		calendarBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		calendarBean.setScheduleTimeEntity(scheduleTimeEntity);
		return calendarBean;
	}


	/**
	 * 全てのユーザの確定スケジュール表示機能<br>
	 * [Service] (/calendar/all)
	 *
	 * @param ym RequestParameter
	 * @return CalendarAllBean
	 */
	public CalendarAllBean calendarAll(String ym) {

		//CmnScheduleCalendarServiceからカレンダー, 年月, 最終日を取得
		CmnScheduleCalendarBean cmnScheduleCalendarBean = cmnScheduleCalendarService.generateCalendarYmByYm(ym);
		//CmnScheduleUserNameServiceから2次元配列の確定スケジュール, スケジュール時間区分を取得
		CmnScheduleUserNameBean cmnScheduleUserNameBean = cmnScheduleUserNameService.generateScheduleRecordedUserNameByYm(cmnScheduleCalendarBean.getYear(), cmnScheduleCalendarBean.getMonth(), cmnScheduleCalendarBean.getLastDateYmd());

		//Beanにセット
		CalendarAllBean calendarBean = new CalendarAllBean();
		calendarBean.setYear(cmnScheduleCalendarBean.getYear());
		calendarBean.setMonth(cmnScheduleCalendarBean.getMonth());
		calendarBean.setCalendarList(cmnScheduleCalendarBean.getCalendarList());
		calendarBean.setUserScheduleAllArray(cmnScheduleUserNameBean.getUserScheduleAllArray());
		calendarBean.setAfterYm(cmnScheduleCalendarBean.getNextYm());
		calendarBean.setBeforeYm(cmnScheduleCalendarBean.getBeforeYm());
		calendarBean.setScheduleTimeEntity(cmnScheduleUserNameBean.getScheduleTimeEntity());
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
	 * @param scheduleTime DBから取得したScheduleTimeEntity
	 * @return Boolean[][]<br>
	 * エレメント(Boolean[][])<br>
	 * true: スケジュール登録済み, false: スケジュール未登録<br>
	 * ただし、要素はBoolean[日付(31固定)][スケジュール時間(スケジュール登録可能数)]
	 */
	private Boolean[][] calcIsScheduleRecordedArrayBySchedule(ScheduleEntity scheduleEntity, ScheduleTimeEntity scheduleTime) {

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
			Boolean[] isScheduleRecordedArray = cmnScheduleLogic.toIsScheduleRecordedArrayBySchedule(scheduleList.get(i), scheduleTime);

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
	 * [DB]スケジュール時間区分取得処理
	 *
	 * <p>取得したい日付(ymd)から該当するスケジュール時間区分を取得する<br>
	 * また、現在日(ymd)に該当するスケジュール時間区分が複数登録されているときは最新のスケジュール時間区分が取得される<br>
	 * ただし、スケジュール時間区分が何も登録されていないときはnullとなる
	 * </p>
	 *
	 * @param ymd 取得したいスケジュール時間区分の日付(YYYYMMDD)
	 * @return ScheduleTimeEntity<br>
	 * フィールド(ScheduleTimeEntity)<br>
	 * id, endYmd, name1, startHm1, endHM1, restHm1... startHm7, endHM7, restHm7
	 */
	private ScheduleTimeEntity selectScheduleTime(String ymd) {

		ScheduleTimeEntity scheduleTimeEntity = scheduleTimeRepository.selectScheduleTimeByYmd(ymd);
		return scheduleTimeEntity;
	}
}
