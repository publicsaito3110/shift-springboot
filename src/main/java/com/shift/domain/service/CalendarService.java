package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CmnScheduleLogic;
import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.CalendarBean;
import com.shift.domain.model.entity.ScheduleEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.ScheduleRepository;
import com.shift.domain.repository.ScheduleTimeRepository;

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


	/**
	 * [Service] (/calendar)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return CalendarBean
	 */
	public CalendarBean calendar(String ym, String loginUser) {

		int[] yearMonthArray = changeYearMonthArray(ym);
		ScheduleEntity scheduleEntity = selectSchedule(yearMonthArray[0], yearMonthArray[1], loginUser);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();
		List<Integer> calendarList = generateCalendar(yearMonthArray[0], yearMonthArray[1]);
		Boolean[][] isScheduleDisplayArray = calcIsScheduleRecordedArrayBySchedule(scheduleEntity, scheduleTimeList);
		String[] nextBeforYmArray = changeNextBeforYmArray(yearMonthArray[0], yearMonthArray[1]);

		//Beanにセット
		CalendarBean calendarBean = new CalendarBean();
		calendarBean.setYear(yearMonthArray[0]);
		calendarBean.setMonth(yearMonthArray[1]);
		calendarBean.setCalendarList(calendarList);
		calendarBean.setIsScheduleDisplayArray(isScheduleDisplayArray);
		calendarBean.setAfterYm(nextBeforYmArray[0]);
		calendarBean.setBeforeYm(nextBeforYmArray[1]);
		calendarBean.setScheduleTimeList(scheduleTimeList);
		return calendarBean;
	}


	/**
	 * 年月変換処理
	 *
	 * <p>年と月をint型に変換し、int[]で返す<br>
	 * ただし、パラメーターがない(null)場合またはymがフォーマット通りでないときは現在の年月になる<br>
	 * int[0]が年, int[1]が月
	 * </p>
	 *
	 * @param ym YYYYMM<br>
	 * {RequestParameter}
	 *
	 * @return int[] intに変換した年[0]と月[1]<br>
	 * int[0]が年, int[1]が月
	 */
	private int[] changeYearMonthArray(String ym) {

		//ymをymd(YYMM01)に変換し、LocalDateを取得する
		String ymd = CommonUtil.changeEmptyByNull(ym) + "01";
		LocalDate ymdLd = new CommonLogic().getLocalDateByYmd(ymd);

		//ym(年月)が指定されていないまたはymがYYYYMMでないとき
		if (ymdLd == null) {

			//現在の日付を取得し、年月に変換
			LocalDate nowLd = LocalDate.now();
			int year = nowLd.getYear();
			int month = nowLd.getMonthValue();

			//年月をint[]に格納して返す
			int[] yearMonth = {year, month};
			return yearMonth;
		}

		//ymdLdから年月に変換
		int year = ymdLd.getYear();
		int month = ymdLd.getMonthValue();

		//年月をint[]に格納して返す
		int[] yearMonthArray = {year, month};
		return yearMonthArray;
	}


	/**
	 * カレンダー作成処理
	 *
	 * <p>year, monthから1ヵ月分のカレンダーを作成する<br>
	 * ただし、カレンダーのフォーマット(7×4 or 7×5 or 7×6)にするため、前月, 翌月も含む(前月, 翌月の日付は含まれない)<br>
	 * また、前月, 翌月分の日付はnullが格納される
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return List<Integer> 1ヵ月分のカレンダー<br>
	 * エレメント(Integer)<br>
	 * 日付をInteger型で格納される
	 */
	private List<Integer> generateCalendar(int year, int month) {

		//------------------------------------
		// 第1週目の日曜日～初日までを設定
		//------------------------------------

		//LocalDateから1日目の情報を取得
		LocalDate localDate = getLocalDateByYearMonth(year, month);

		//第1週目の初日の曜日を取得（月:1, 火:2.....日:7）
		int firstWeek = localDate.getDayOfWeek().getValue();

		//日付けとスケジュールを格納する
		List<Integer> calendarList = new ArrayList<>();

		//firstWeekが日曜日でないとき
		if (firstWeek != 7) {

			//初日が日曜を除く取得した曜日の回数分nullを代入してカレンダーのフォーマットに揃える
			for (int i = 1; i <= firstWeek; i ++) {
				calendarList.add(null);
			}
		}

		//-------------
		// 日付を設定
		//-------------

		//最終日をLocalDateから取得
		int lastDay = localDate.lengthOfMonth();

		//lastDayの回数だけループして日付を格納
		for (int i = 1; i <= lastDay; i++) {
			calendarList.add(i);
		}

		//------------------------------------
		// 最終週の終了日～土曜日までを設定
		//------------------------------------

		//calendarListに登録した要素数から残りの最終週の土曜日までの日数を取得
		int remainderWeek = 7 - (calendarList.size() % 7);

		// remainderWeekが7(最終日が土曜日)以外のとき
		if (remainderWeek != 7) {

			//remainderWeekの回数分nullを代入してカレンダーのフォーマットに揃える
			for (int i = 1; i <= remainderWeek; i ++) {
				calendarList.add(null);
			}
		}

		return calendarList;
	}


	/**
	 * スケジュール登録済み判定List取得処理
	 *
	 * <p>schedulePreEntityとscheduleTimeListから登録済みのスケジュールとスケジュール時間区分を取得し、登録されているかを判別する<br>
	 * Listのエレメント(Boolean[])には1日ごとのスケジュール時間区分で登録済みかを判別する
	 * </p>
	 *
	 * @param schedulePreEntity DBから取得したSchedulePreEntity
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
	 * 翌前月に取得処理
	 *
	 * <p>翌月と前月を計算して返す<br>
	 * ym(YYYYMM)に変換した翌月[0]と前月[1]
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return String[] 翌月のym[0]と前月のym[1]<br>
	 * String[0]が翌月, String[1]が前月
	 */
	private String[] changeNextBeforYmArray(int year, int month) {

		//year, monthから現在のLocalDateを取得
		LocalDate nowLd = getLocalDateByYearMonth(year, month);

		//nowLd前月のLocalDateを取得し、beforeYmに代入
		LocalDate beforeMonthLd = nowLd.minusMonths(1);
		int beforeYear = beforeMonthLd.getYear();
		int beforeMonth = beforeMonthLd.getMonthValue();

		//beforeYear, beforeMonthをym(YYYYMM)に変換
		String beforeYm = toStringYmFormatSixByYearMonth(beforeYear, beforeMonth);

		//翌月のymをafterYmに代入
		LocalDate afterMonthLd = nowLd.plusMonths(1);
		int afterYear = afterMonthLd.getYear();
		int afterMonth = afterMonthLd.getMonthValue();

		//afterYear, afterMonthをym(YYYYMM)に変換
		String afterYm = toStringYmFormatSixByYearMonth(afterYear, afterMonth);

		//beforeYm, afterYmをString[]に格納し、返す
		String[] nextBeforeYmArray = {afterYm, beforeYm};
		return nextBeforeYmArray;
	}


	/**
	 * [DB]スケジュール検索処理
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
		String ym = toStringYmFormatSixByYearMonth(year, month);

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


	/**
	 * [privateメソッド共通処理]年月変換処理
	 *
	 * <p>year(int), month(int)をym(YYYYMM)に変換して返す</p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return String ym(YYYYMM)
	 */
	private String toStringYmFormatSixByYearMonth(int year, int month) {

		//ym(YYYYMM)に変換する
		return String.valueOf(year) + String.format("%02d", month);
	}


	/**
	 * [privateメソッド共通処理]LoccalDate取得処理
	 *
	 * <p>year(int), month(int)からLocalDateを返す<br>
	 * ただし、正確な日付は初日となる
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return LocalDate ym(YYYYMM)
	 */
	private LocalDate getLocalDateByYearMonth(int year, int month) {

		//year, monthから初日のLocalDateで取得し、返す
		LocalDate localDate = LocalDate.of(year, month, 1);
		return localDate;
	}
}
