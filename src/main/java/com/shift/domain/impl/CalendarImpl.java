package com.shift.domain.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shift.bean.ScheduleBean;
import com.shift.domain.Interface.CalendarIntarface;
import com.shift.domain.repositry.ScheduleRepositry;
import com.shift.entity.ScheduleEntity;

@Component
public class CalendarImpl implements CalendarIntarface {

	//フィールド
	private int year;
	private int month;
	private List<ScheduleEntity> scheduleList;
	private LocalDate localDate;

	@Autowired
	ScheduleRepositry scheduleRepositry;


	@Override
	public void getMonth(HttpServletRequest request, HttpServletResponse response) {

		//パラメーターを受け取る
		String ym = request.getParameter("ym");

		//ym(年月)が指定されていたとき
		if (ym != null) {

			//ymからyear(4文字)month(2文字)を取得
			this.year  = Integer.parseInt(ym.substring(0, 4));
			this.month = Integer.parseInt(ym.substring(4, 6));

			// 引き渡す値を設定
			request.setAttribute("year", this.year);
			request.setAttribute("month", this.month);
			return;
		}

		//現在の日付を取得し、フィールドにセット
		LocalDate now = LocalDate.now();
		this.year = now.getYear();
		this.month = now.getMonthValue();

		// 引き渡す値を設定
		request.setAttribute("year", this.year);
		request.setAttribute("month", this.month);
	}


	@Override
	public void getSchedule(HttpServletRequest request, HttpServletResponse response) {

		//カレンダーの年月をym(YYYYMM)で取得
		String ym = this.toStringYmFormatSixByIntYm(this.year, this.month);

		//フィールドにセット
		this.scheduleList = scheduleRepositry.selectScheduleMonthByYm(ym);

		// 引き渡す値を設定
		request.setAttribute("scheduleList", this.scheduleList);
	}
	@Override
	public void generateCalendar(HttpServletRequest request, HttpServletResponse response) {

		//------------------------------------
		// 第1週目の日曜日～初日までを設定
		//------------------------------------

		//LocalDateから1日目の情報を取得
		LocalDate localDate = LocalDate.of(this.year, this.month, 1);

		// 第1週目の初日の曜日を取得（月：1, 火：2.....日:7）
		int firstWeek = localDate.getDayOfWeek().getValue();

		//日付けを格納するArrayListを取得
		List<ScheduleBean> calendarList = new ArrayList<>();

		//firstWeekが日曜日でないとき
		if (firstWeek != 7) {

			//曜日の取得 7 1 2 3 4 5 6 なので、初日が日曜を除く取得した曜日の回数分null代入し揃える
			for (int i = 1; i <= firstWeek; i ++) {
				calendarList.add(null);
			}
		}


		//---------------------------
		// 日付とスケジュールを設定
		//---------------------------

		//最終日をLocalDateから取得
		int lastDay = localDate.lengthOfMonth();

		//dbListの要素を指定するための変数
		int youso = 0;

		//日付と登録されたスケジュールをdayListに格納
		for (int i = 1; i <= lastDay; i++) {

			ScheduleBean bean = new ScheduleBean();
			bean.setDay(String.valueOf(i));

			//指定したカレンダーに登録されたスケジュールが1つもないとき
			if (this.scheduleList.isEmpty()) {
				calendarList.add(bean);
				continue;
			}

			//yousoがdbListの要素数を超えたとき
			if (this.scheduleList.size() <= youso) {
				calendarList.add(bean);
				continue;
			}

			//iが1桁のとき2桁の文字列に変換する
			String day = String.format("%02d", i);

			//DAOの戻り値のdayと実際の日付(day)が同じだったときdbListの値をsetする
			if ((this.scheduleList.get(youso).getFormatDay()).equals(day)) {

				//dbListから登録されている情報を取得し、dayListに格納する
				bean.setUser1(this.scheduleList.get(youso).getUser1());
				bean.setUser2(this.scheduleList.get(youso).getUser2());
				bean.setUser3(this.scheduleList.get(youso).getUser3());
				bean.setMemo1(this.scheduleList.get(youso).getMemo1());
				bean.setMemo2(this.scheduleList.get(youso).getMemo2());
				bean.setMemo3(this.scheduleList.get(youso).getMemo3());
				calendarList.add(bean);

				//dbListを参照するyouso(要素)に+1する
				youso++;
				continue;
			}

			calendarList.add(bean);
		}


		//------------------------------------
		// 最終週の終了日～土曜日までを設定
		//------------------------------------

		//[カレンダー]最終日の曜日～土曜日まで null を代入し揃える
		int weekAmari = 7 - (calendarList.size() % 7);

		// weekAmariが7(最終日が土曜日)以外のとき
		if (weekAmari != 7) {

			//曜日の取得 7 1 2 3 4 5 6 なので、//dayListの要素数÷7のあまりの回数分代入する
			for (int i = 1; i <= weekAmari; i ++) {
				calendarList.add(null);
			}
		}

		//フィールドにセット
		this.localDate = localDate;

		// 引き渡す値を設定
		request.setAttribute("dayList", calendarList);
	}


	@Override
	public void getNextBeforMonth(HttpServletRequest request, HttpServletResponse response) {

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

		// 引き渡す値を設定
		request.setAttribute("beforeYm", beforeYm);
		request.setAttribute("afterYm", afterYm);
	}


	/**
	 * 年月に変換処理
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
