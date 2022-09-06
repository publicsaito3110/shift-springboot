package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.ScheduleBean;
import com.shift.domain.model.bean.ScheduleCalendarBean;
import com.shift.domain.model.entity.SchedulePreEntity;
import com.shift.domain.model.entity.ScheduleTimeEntity;
import com.shift.domain.repository.SchedulePreRepository;
import com.shift.domain.repository.ScheduleTimeRepository;

/**
 * @author saito
 *
 */
@Service
public class ScheduleService extends BaseService {

	@Autowired
	private SchedulePreRepository schedulePreRepository;

	@Autowired
	private ScheduleTimeRepository scheduleTimeRepository;


	/**
	 * [Service] (/schedule)
	 *
	 * @param ym RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return CalendarBean
	 */
	public ScheduleBean schedule(String ym, String loginUser) {

		int[] yearMonthArray = changeYearMonthArray(ym);
		SchedulePreEntity schedulePreEntity = selectSchedulePre(yearMonthArray[0], yearMonthArray[1], loginUser);
		List<ScheduleCalendarBean> calendarList = generateCalendar(schedulePreEntity, yearMonthArray[0], yearMonthArray[1]);
		String[] nextBeforeYmArray = calcNextBeforYmArray(yearMonthArray[0], yearMonthArray[1]);
		List<ScheduleTimeEntity> scheduleTimeList = selectScheduleTime();

		//Beanにセット
		ScheduleBean scheduleBean = new ScheduleBean();
		scheduleBean.setYear(yearMonthArray[0]);
		scheduleBean.setMonth(yearMonthArray[1]);
		scheduleBean.setCalendarList(calendarList);
		scheduleBean.setAfterYm(nextBeforeYmArray[0]);
		scheduleBean.setBeforeYm(nextBeforeYmArray[1]);
		scheduleBean.setScheduleTimeList(scheduleTimeList);
		return scheduleBean;
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
	 * <p>year, month, scheduleListから1ヵ月分のスケジュール入りのカレンダーを作成する<br>
	 * ただし、カレンダーのフォーマット(7×4または7×5)にするため、前月, 翌月の曜日も含む(前月, 翌月の日付とスケジュールは含まれない)
	 * </p>
	 *
	 * @param scheduleList DBから取得したList
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return List<CalendarScheduleBean> 1ヵ月分のカレンダー<br>
	 * フィールド(List&lt;ScheduleCalendarBean&gt;)<br>
	 * day, schedule, htmlClass
	 */
	private List<ScheduleCalendarBean> generateCalendar(SchedulePreEntity schedulePreEntity, int year, int month) {

		//------------------------------------
		// 第1週目の日曜日～初日までを設定
		//------------------------------------

		//LocalDateから1日目の情報を取得
		LocalDate localDate = getLocalDateByYearMonth(year, month);

		//第1週目の初日の曜日を取得（月：1, 火：2.....日:7）
		int firstWeek = localDate.getDayOfWeek().getValue();

		//日付けとスケジュールを格納する
		List<ScheduleCalendarBean> calendarList = new ArrayList<>();

		//firstWeekが日曜日でないとき
		if (firstWeek != 7) {

			//初日が日曜を除く取得した曜日の回数分ScheduleCalendarBeanを代入してカレンダーのフォーマットに揃える
			for (int i = 1; i <= firstWeek; i ++) {
				calendarList.add(new ScheduleCalendarBean());
			}
		}

		//---------------------------
		// 日付とスケジュールを設定
		//---------------------------

		//最終日をLocalDateから取得
		int lastDay = localDate.lengthOfMonth();

		//schedulePreEntityがnullでないとき、schedulePreEntityからdayに格納されている値をListで取得
		List<String> dayList = new ArrayList<>();
		if (schedulePreEntity != null) {
			dayList = schedulePreEntity.getDayList();
		}

		//dayListの要素を指定
		int index = 0;

		//日付と登録されたスケジュールをcalendarListに格納
		for (int i = 1; i <= lastDay; i++) {

			//scheduleCalendarBeanを初期化し、日付を格納
			ScheduleCalendarBean scheduleCalendarBean = new ScheduleCalendarBean();
			scheduleCalendarBean.setDay(String.valueOf(i));

			//calendarListの現在の要素数が土曜日(あまり6)のとき
			if (calendarList.size() % 7 == 6) {
				scheduleCalendarBean.setHtmlClass(Const.HTML_CLASS_CALENDAR_SAT);
			}

			//calendarListの現在の要素数が日曜日(あまり0)のとき
			if (calendarList.size() % 7 == 0) {
				scheduleCalendarBean.setHtmlClass(Const.HTML_CLASS_CALENDAR_SUN);
			}

			//指定したカレンダーに登録されたスケジュールが登録されていないとき
			if (schedulePreEntity == null) {
				calendarList.add(scheduleCalendarBean);
				continue;
			}

			//要素を指定し、dayListの値を取得
			String dayListValue = CommonUtil.changeEmptyByNull(dayList.get(index));

			//scheduleCalendarBeanをcalendarListにセットし、次の要素へ
			scheduleCalendarBean.setSchedule(dayListValue);
			calendarList.add(scheduleCalendarBean);
			index++;
		}

		//------------------------------------
		// 最終週の終了日～土曜日までを設定
		//------------------------------------

		//calendarListに登録した要素数から残りの最終週の土曜日までの日数を取得
		int remainderWeek = 7 - (calendarList.size() % 7);

		// remainderWeekが7(最終日が土曜日)以外のとき
		if (remainderWeek != 7) {

			//remainderWeekの回数分scheduleBeanを代入する
			for (int i = 1; i <= remainderWeek; i ++) {
				calendarList.add(new ScheduleCalendarBean());
			}
		}

		return calendarList;
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
	private String[] calcNextBeforYmArray(int year, int month) {

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
	 * <p>現在の年月からログインユーザーの1ヵ月分のスケジュール予定を取得する<br>
	 * ただし、登録済みのスケジュールがないときはnullとなる<br>
	 * また、日付が存在しない日(2月 -> 30, 31日etc)は必ず登録されていない
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return SchedulePreEntity <br>
	 * フィールド(SchedulePreEntity)<br>
	 * id, ym, user, 1, 2, 3, 4, 5... 30, 31
	 *
	 */
	private SchedulePreEntity selectSchedulePre(int year, int month, String loginUser) {

		//year, monthをym(YYYYMM)に変換
		String ym = toStringYmFormatSixByYearMonth(year, month);

		//DBから取得し、返す
		SchedulePreEntity schedulePreEntity = schedulePreRepository.findByYmAndUser(ym, loginUser);
		return schedulePreEntity;
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
