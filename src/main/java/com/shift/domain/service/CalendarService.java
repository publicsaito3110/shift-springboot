package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.CalendarBean;
import com.shift.domain.model.bean.CalendarScheduleBean;
import com.shift.domain.model.entity.ScheduleEntity;
import com.shift.domain.repository.ScheduleRepository;

/**
 * @author saito
 *
 */
@Service
public class CalendarService extends BaseService {

	@Autowired
	private ScheduleRepository scheduleRepository;


	/**
	 * [Service] (/calendar)
	 *
	 * @param ym YYYYMM<br>
	 * {RequestParameter}
	 * @return CalendarBean
	 */
	public CalendarBean calendar(String ym) {

		int[] yearMonthArray = changeYearMonthArray(ym);
		List<ScheduleEntity> scheduleDbList = selectSchedule(yearMonthArray[0], yearMonthArray[1]);
		List<CalendarScheduleBean> calendarList = generateCalendar(scheduleDbList, yearMonthArray[0], yearMonthArray[1]);
		String[] nextBeforYmArray = changeNextBeforYmArray(yearMonthArray[0], yearMonthArray[1]);

		//Beanにセット
		CalendarBean calendarBean = new CalendarBean();
		calendarBean.setYear(yearMonthArray[0]);
		calendarBean.setMonth(yearMonthArray[1]);
		calendarBean.setCalendarList(calendarList);
		calendarBean.setAfterYm(nextBeforYmArray[0]);
		calendarBean.setBeforeYm(nextBeforYmArray[1]);
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
	 * [DB]スケジュール検索処理
	 *
	 * <p>year, monthから1ヵ月分のスケジュールを取得する</p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return List<ScheduleEntity> <br>
	 * フィールド(List&lt;ScheduleEntity&gt;)<br>
	 * ymd, user1, user2, user3, memo1, memo2, memo3
	 *
	 */
	private List<ScheduleEntity> selectSchedule(int year, int month) {

		//year, monthをym(YYYYMM)に変換
		String ym = toStringYmFormatSixByYearMonth(year, month);

		//DBから取得し、返す
		List<ScheduleEntity> scheduleList = scheduleRepository.findByYmdLike(ym + Const.CHARACTER_PERCENT);
		return scheduleList;
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
	 * フィールド(List&lt;CalendarScheduleBean&gt;)<br>
	 * ymd, user1, user2, user3, memo1, memo2, memo3, day, htmlClass
	 */
	private List<CalendarScheduleBean> generateCalendar(List<ScheduleEntity> scheduleList, int year, int month) {

		//------------------------------------
		// 第1週目の日曜日～初日までを設定
		//------------------------------------

		//LocalDateから1日目の情報を取得
		LocalDate localDate = getLocalDateByYearMonth(year, month);

		//第1週目の初日の曜日を取得（月：1, 火：2.....日:7）
		int firstWeek = localDate.getDayOfWeek().getValue();

		//日付けとスケジュールを格納する
		List<CalendarScheduleBean> calendarList = new ArrayList<>();

		//firstWeekが日曜日でないとき
		if (firstWeek != 7) {

			//初日が日曜を除く取得した曜日の回数分scheduleBeanを代入してカレンダーのフォーマットに揃える
			for (int i = 1; i <= firstWeek; i ++) {
				calendarList.add(new CalendarScheduleBean());
			}
		}

		//---------------------------
		// 日付とスケジュールを設定
		//---------------------------

		//最終日をLocalDateから取得
		int lastDay = localDate.lengthOfMonth();

		//scheduleListの要素を指定する
		int index = 0;

		//日付と登録されたスケジュールをcalendarListに格納
		for (int i = 1; i <= lastDay; i++) {

			CalendarScheduleBean calendarScheduleBean = new CalendarScheduleBean();
			calendarScheduleBean.setDay(String.valueOf(i));

			//calendarListの現在の要素数が土曜日(あまりが6)のとき
			if (calendarList.size() % 7 == 6) {
				calendarScheduleBean.setHtmlClass(Const.HTML_CLASS_CALENDAR_SAT);
			}

			//calendarListの現在の要素数が日曜日(あまりが0)のとき
			if (calendarList.size() % 7 == 0) {
				calendarScheduleBean.setHtmlClass(Const.HTML_CLASS_CALENDAR_SUN);
			}

			//指定したカレンダーに登録されたスケジュールが1つもないとき
			if (scheduleList.isEmpty()) {
				calendarList.add(calendarScheduleBean);
				continue;
			}

			//indexがcalendarListの要素数を超えた(これ以上登録済みのスケジュールがない)とき
			if (scheduleList.size() <= index) {
				calendarList.add(calendarScheduleBean);
				continue;
			}

			//scheduleListのとi(ループ回数)をday(DD)に変換する
			String day = String.format("%02d", i);
			String scheduleListIndexDay = scheduleList.get(index).getFormatDay();

			//scheduleListIndexDay(現在のindexにおける日付)とday(現在登録しようとしているスケジュール)が同じとき
			if (scheduleListIndexDay.equals(day)) {

				//scheduleListの値を取得し、calendarListに格納する
				calendarScheduleBean.setUser1(scheduleList.get(index).getUser1());
				calendarScheduleBean.setUser2(scheduleList.get(index).getUser2());
				calendarScheduleBean.setUser3(scheduleList.get(index).getUser3());
				calendarScheduleBean.setMemo1(scheduleList.get(index).getMemo1());
				calendarScheduleBean.setMemo2(scheduleList.get(index).getMemo2());
				calendarScheduleBean.setMemo3(scheduleList.get(index).getMemo3());
				calendarList.add(calendarScheduleBean);

				//scheduleListを参照するindex(要素)を+1する
				index++;
				continue;
			}

			calendarList.add(calendarScheduleBean);
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
				calendarList.add(new CalendarScheduleBean());
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
	private String[] changeNextBeforYmArray(int year, int month) {

		//year, monthから現在のLocalDateを取得
		LocalDate nowLd = getLocalDateByYearMonth(year, month);

		//nowLd前月のLocalDateを取得し、beforeYmに代入
		LocalDate beforeMonthLd = nowLd.minusMonths(1);
		int beforeYear = beforeMonthLd.getYear();
		int beforeMonth = beforeMonthLd.getMonthValue();

		//beforeYear, beforeMonthをym(YYYYMM)に変換
		String beforeYm = this.toStringYmFormatSixByYearMonth(beforeYear, beforeMonth);

		//翌月のymをafterYmに代入
		LocalDate afterMonthLd = nowLd.plusMonths(1);
		int afterYear = afterMonthLd.getYear();
		int afterMonth = afterMonthLd.getMonthValue();

		//afterYear, afterMonthをym(YYYYMM)に変換
		String afterYm = this.toStringYmFormatSixByYearMonth(afterYear, afterMonth);

		//beforeYm, afterYmをString[]に格納し、返す
		String[] nextBeforeYmArray = {afterYm, beforeYm};
		return nextBeforeYmArray;
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
