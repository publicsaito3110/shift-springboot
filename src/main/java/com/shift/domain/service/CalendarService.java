package com.shift.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.Const;
import com.shift.domain.model.bean.CalendarBean;
import com.shift.domain.model.bean.ScheduleBean;
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

	//フィールド
	private int year;

	private int month;

	private List<ScheduleEntity> scheduleList;

	private LocalDate localDate;


	/**
	 * [Service] (/calendar)
	 *
	 * @param ym RequestParameter
	 * @return CalendarBean
	 */
	public CalendarBean calendar(String ym) {

		int[] yearMonth = this.changeYearMonth(ym);
		this.selectSchedule();
		List<ScheduleBean> calendarList = this.generateCalendar();
		String[] nextBeforMonth = this.getNextBeforMonth();

		//Beanにセット
		CalendarBean calendarBean = new CalendarBean();
		calendarBean.setYear(yearMonth[0]);
		calendarBean.setMonth(yearMonth[1]);
		calendarBean.setCalendarList(calendarList);
		calendarBean.setAfterYm(nextBeforMonth[0]);
		calendarBean.setBeforeYm(nextBeforMonth[1]);
		return calendarBean;
	}


	/**
	 * 年月変換処理
	 *
	 * <p>年と月をint型に変換し、int[]で返す<br>
	 * ただし、パラメーターがない(null)場合は現在の日付になる<br>
	 * int[0]が年, int[1]が月
	 * </p>
	 *
	 * @param ym RequestParameter
	 * @return int[] intに変換した年[0]と月[1]
	 */
	private int[] changeYearMonth(String ym) {

		//ym(年月)が指定されていたとき
		if (ym != null) {

			//ymからyear(4文字)month(2文字)を取得
			int year = Integer.parseInt(ym.substring(0, 4));
			int month = Integer.parseInt(ym.substring(4, 6));

			//フィールドにセット
			this.year = year;
			this.month = month;

			int[] yearMonth = {year, month};
			return yearMonth;
		}

		//現在の日付を取得
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		int month = now.getMonthValue();

		//フィールドにセット
		this.year = year;
		this.month = month;

		int[] yearMonth = {year, month};
		return yearMonth;
	}


	/**
	 * [DB]スケジュール検索処理
	 *
	 * <p>year, monthから1ヵ月分のスケジュールを取得する</p>
	 *
	 * @param void
	 * @return void
	 */
	private void selectSchedule() {

		//カレンダーの年月をym(YYYYMM)で取得
		String ym = this.toStringYmFormatSixByIntYm(this.year, this.month);

		//フィールドにセット
		this.scheduleList = this.scheduleRepository.findByYmdLike(ym + "%");
	}


	/**
	 * カレンダー作成処理
	 *
	 * <p>year, month, scheduleListから1ヵ月分のスケジュール入りのカレンダーを作成する<br>
	 * ただし、カレンダーのフォーマット(7×4または7×5)にするため、前月, 翌月の曜日も含む(前月, 翌月の日付とスケジュールは含まれない)
	 * </p>
	 *
	 * @param void
	 * @return List<ScheduleBean> 1ヵ月分のカレンダー
	 */
	private List<ScheduleBean> generateCalendar() {

		//------------------------------------
		// 第1週目の日曜日～初日までを設定
		//------------------------------------

		//LocalDateから1日目の情報を取得
		LocalDate localDate = LocalDate.of(this.year, this.month, 1);

		// 第1週目の初日の曜日を取得（月：1, 火：2.....日:7）
		int firstWeek = localDate.getDayOfWeek().getValue();

		//日付けとスケジュールを格納する
		List<ScheduleBean> calendarList = new ArrayList<>();

		//firstWeekが日曜日でないとき
		if (firstWeek != 7) {

			//曜日の取得 7 1 2 3 4 5 6 なので、初日が日曜を除く取得した曜日の回数分scheduleBeanを代入し揃える
			for (int i = 1; i <= firstWeek; i ++) {
				calendarList.add(new ScheduleBean());
			}
		}


		//---------------------------
		// 日付とスケジュールを設定
		//---------------------------

		//最終日をLocalDateから取得
		int lastDay = localDate.lengthOfMonth();

		//scheduleListの要素を指定するための変数
		int youso = 0;

		//日付と登録されたスケジュールをcalendarListに格納
		for (int i = 1; i <= lastDay; i++) {

			ScheduleBean scheduleBean = new ScheduleBean();
			scheduleBean.setDay(String.valueOf(i));

			//calendarListの現在の要素数が土曜日(あまりが6)のとき
			if (calendarList.size() % 7 == 6) {
				scheduleBean.setHtmlClass(Const.HTML_CLASS_CALENDAR_SAT);
			}

			//calendarListの現在の要素数が日曜日(あまりが0)のとき
			if (calendarList.size() % 7 == 0) {
				scheduleBean.setHtmlClass(Const.HTML_CLASS_CALENDAR_SUN);
			}

			//指定したカレンダーに登録されたスケジュールが1つもないとき
			if (this.scheduleList.isEmpty()) {
				calendarList.add(scheduleBean);
				continue;
			}

			//yousoがcalendarListの要素数を超えたとき
			if (this.scheduleList.size() <= youso) {
				calendarList.add(scheduleBean);
				continue;
			}

			//現在のiからymd(YYYYMMDD)に変換する
			String day = String.format("%02d", i);

			//DAOの戻り値のdayと実際の日付(day)が同じだったときscheduleListの値をsetする
			if ((this.scheduleList.get(youso).getFormatDay()).equals(day)) {

				//dbListから登録されている情報を取得し、dayListに格納する
				scheduleBean.setUser1(this.scheduleList.get(youso).getUser1());
				scheduleBean.setUser2(this.scheduleList.get(youso).getUser2());
				scheduleBean.setUser3(this.scheduleList.get(youso).getUser3());
				scheduleBean.setMemo1(this.scheduleList.get(youso).getMemo1());
				scheduleBean.setMemo2(this.scheduleList.get(youso).getMemo2());
				scheduleBean.setMemo3(this.scheduleList.get(youso).getMemo3());
				calendarList.add(scheduleBean);

				//scheduleListを参照するyouso(要素)を+1する
				youso++;
				continue;
			}

			calendarList.add(scheduleBean);
		}


		//------------------------------------
		// 最終週の終了日～土曜日までを設定
		//------------------------------------

		//calendarListに登録した回数から残りの最終週の土曜日までの日数を取得
		int weekAmari = 7 - (calendarList.size() % 7);

		// weekAmariが7(最終日が土曜日)以外のとき
		if (weekAmari != 7) {

			//曜日の取得 7 1 2 3 4 5 6 なので、//dayListの要素数÷7のあまりの回数分scheduleBeanを代入する
			for (int i = 1; i <= weekAmari; i ++) {
				calendarList.add(new ScheduleBean());
			}
		}

		//フィールドにセット
		this.localDate = localDate;
		return calendarList;
	}


	/**
	 * 翌前月に取得処理
	 *
	 * <p>翌月と前月を計算して返す</p>
	 *
	 * @param void
	 * @return String[] <br>
	 * {翌月, 前月}
	 */
	private String[] getNextBeforMonth() {

		//前月のymをbeforeYmに代入
		LocalDate localDateBeforeMonth = this.localDate.minusMonths(1);
		int beforeYear = localDateBeforeMonth.getYear();
		int beforeMonth = localDateBeforeMonth.getMonthValue();

		//year,monthをString6桁(YYYYMM)に変える
		String beforeYm = this.toStringYmFormatSixByIntYm(beforeYear, beforeMonth);

		//翌月のymをafterYmに代入
		LocalDate localDateAfterMonth = this.localDate.plusMonths(1);
		int afterYear = localDateAfterMonth.getYear();
		int afterMonth = localDateAfterMonth.getMonthValue();

		//year,monthをString6桁(YYYYMM)に変える
		String afterYm = this.toStringYmFormatSixByIntYm(afterYear, afterMonth);

		String[] nextBeforeMonth = {afterYm, beforeYm};
		return nextBeforeMonth;
	}


	/**
	 * [共通処理]年月に変換処理
	 *
	 * <p>year(int), month(int)をym(YYYYMM)に変換して返す</p>
	 *
	 * @param year LocalDateから取得したint型の年
	 * @param month LocalDateから取得したint型の月
	 * @return String 年月(YYYYMM)
	 */
	private String toStringYmFormatSixByIntYm(int year, int month) {

		//monthが1桁のとき2桁の文字列に変換する
		String ym = year + String.format("%02d", month);

		return ym;
	}
}
