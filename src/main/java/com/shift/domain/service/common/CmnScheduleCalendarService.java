package com.shift.domain.service.common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.domain.model.bean.CmnScheduleCalendarBean;
import com.shift.domain.service.BaseService;

/**
 * @author saito
 *
 */
@Service
public class CmnScheduleCalendarService extends BaseService {


	/**
	 * [共通Service] カレンダー, YM作成処理
	 *
	 * @param ym
	 * @return CmnScheduleCalendarBean<br>
	 * フィールド(CmnScheduleCalendarBean)<br>
	 * year, month, calendarList, nowYm, nextYm, beforeYm
	 */
	public CmnScheduleCalendarBean generateCalendarYmByYm(String ym) {

		int[] yearMonthArray = changeYearMonthArray(ym);
		List<Integer> calendarList = generateCalendar(yearMonthArray[0], yearMonthArray[1]);
		String[] nowNextBeforeYmArray = calcNextBeforYmArray(yearMonthArray[0], yearMonthArray[1]);

		//Beanにセット
		CmnScheduleCalendarBean cmnScheduleCalendarBean = new CmnScheduleCalendarBean();
		cmnScheduleCalendarBean.setYear(yearMonthArray[0]);
		cmnScheduleCalendarBean.setMonth(yearMonthArray[1]);
		cmnScheduleCalendarBean.setCalendarList(calendarList);
		cmnScheduleCalendarBean.setNowYm(nowNextBeforeYmArray[0]);
		cmnScheduleCalendarBean.setNextYm(nowNextBeforeYmArray[1]);
		cmnScheduleCalendarBean.setBeforeYm(nowNextBeforeYmArray[2]);
		return cmnScheduleCalendarBean;
	}


	/**
	 * 翌前月に取得処理
	 *
	 * <p>翌月と前月を計算して返す<br>
	 * ym(YYYYMM)に変換した現在の月[0], 翌月[1]と前月[2]
	 * </p>
	 *
	 * @param year LocalDateから取得した年(int)
	 * @param month LocalDateから取得した月(int)
	 * @return String[] 現在の月[0], 翌月のym[1]と前月のym[2]<br>
	 * String[0]が現在の月, String[1]が翌月, String[2]が前月
	 */
	private String[] calcNextBeforYmArray(int year, int month) {

		//year, monthから現在のLocalDateを取得し、nowYmに代入
		LocalDate nowLd = getLocalDateByYearMonth(year, month);
		int nowYear = nowLd.getYear();
		int nowMonth = nowLd.getMonthValue();

		//共通ロジックをインスタンス化
		CommonLogic CommonLogic = new CommonLogic();

		//nowYear, nowMonthをym(YYYYMM)に変換
		String nowYm = CommonLogic.toStringYmFormatSixByYearMonth(nowYear, nowMonth);

		//nowLdから前月のLocalDateを取得し、beforeYmに代入
		LocalDate beforeMonthLd = nowLd.minusMonths(1);
		int beforeYear = beforeMonthLd.getYear();
		int beforeMonth = beforeMonthLd.getMonthValue();

		//beforeYear, beforeMonthをym(YYYYMM)に変換
		String beforeYm = CommonLogic.toStringYmFormatSixByYearMonth(beforeYear, beforeMonth);

		//nowLdから翌月のymをafterYmに代入
		LocalDate afterMonthLd = nowLd.plusMonths(1);
		int afterYear = afterMonthLd.getYear();
		int afterMonth = afterMonthLd.getMonthValue();

		//afterYear, afterMonthをym(YYYYMM)に変換
		String afterYm = CommonLogic.toStringYmFormatSixByYearMonth(afterYear, afterMonth);

		//nowYm, beforeYm, afterYmをString[]に格納し、返す
		String[] nowNextBeforeYmArray = {nowYm, afterYm, beforeYm};
		return nowNextBeforeYmArray;
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
			int[] yearMonthArray = {year, month};
			return yearMonthArray;
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
