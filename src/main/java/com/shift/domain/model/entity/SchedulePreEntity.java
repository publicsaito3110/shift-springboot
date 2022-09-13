package com.shift.domain.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shift.common.Const;
import com.shift.form.ScheduleModifyForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author saito
 *
 */
@Entity
@Table(name="schedule_pre")
@Data
@EqualsAndHashCode(callSuper = true)
public class SchedulePreEntity extends BaseEntity {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "ym")
	private String ym;

	@Column(name = "user")
	private String user;

	@Column(name = "day1")
	private String day1;

	@Column(name = "day2")
	private String day2;

	@Column(name = "day3")
	private String day3;

	@Column(name = "day4")
	private String day4;

	@Column(name = "day5")
	private String day5;

	@Column(name = "day6")
	private String day6;

	@Column(name = "day7")
	private String day7;

	@Column(name = "day8")
	private String day8;

	@Column(name = "day9")
	private String day9;

	@Column(name = "day10")
	private String day10;

	@Column(name = "day11")
	private String day11;

	@Column(name = "day12")
	private String day12;

	@Column(name = "day13")
	private String day13;

	@Column(name = "day14")
	private String day14;

	@Column(name = "day15")
	private String day15;

	@Column(name = "day16")
	private String day16;

	@Column(name = "day17")
	private String day17;

	@Column(name = "day18")
	private String day18;

	@Column(name = "day19")
	private String day19;

	@Column(name = "day20")
	private String day20;

	@Column(name = "day21")
	private String day21;

	@Column(name = "day22")
	private String day22;

	@Column(name = "day23")
	private String day23;

	@Column(name = "day24")
	private String day24;

	@Column(name = "day25")
	private String day25;

	@Column(name = "day26")
	private String day26;

	@Column(name = "day27")
	private String day27;

	@Column(name = "day28")
	private String day28;

	@Column(name = "day29")
	private String day29;

	@Column(name = "day30")
	private String day30;

	@Column(name = "day31")
	private String day31;


	//メソッド
	public List<String> getDayList() {

		//日付のフィールドをListで全て取得
		List<String> dayList = new ArrayList<>();
		dayList.add(day1);
		dayList.add(day2);
		dayList.add(day3);
		dayList.add(day4);
		dayList.add(day5);
		dayList.add(day6);
		dayList.add(day7);
		dayList.add(day8);
		dayList.add(day9);
		dayList.add(day10);
		dayList.add(day11);
		dayList.add(day12);
		dayList.add(day13);
		dayList.add(day14);
		dayList.add(day15);
		dayList.add(day16);
		dayList.add(day17);
		dayList.add(day18);
		dayList.add(day19);
		dayList.add(day20);
		dayList.add(day21);
		dayList.add(day22);
		dayList.add(day23);
		dayList.add(day24);
		dayList.add(day25);
		dayList.add(day26);
		dayList.add(day27);
		dayList.add(day28);
		dayList.add(day29);
		dayList.add(day30);
		dayList.add(day31);

		return dayList;
	}


	//ScheduleModifyFormから値をセットするSetter
	public void setDayByScheduleModifyForm(ScheduleModifyForm scheduleModifyForm) {

		//dayの値全てをリセットする
		resetValueByAllDay();

		//ymをセット
		ym = scheduleModifyForm.getYm();

		//dayArrayを取得
		String[][] scheduleDayArray2 = scheduleModifyForm.getDayArray();

		//セットするdayを指定するための変数
		int setDay = 1;

		//1次配列目(日付)だけループする
		for (String[] scheduleDayArray: scheduleDayArray2) {

			//2次配列目(スケジュール時間区分)だけループする
			for (String scheduleValue: scheduleDayArray) {

				//指定した日付のスケジュール時間区分にスケジュールが登録されているとき
				if (Const.SCHEDULE_PRE_DAY_RECORDED.equals(scheduleValue)) {
					setDayValue(Const.SCHEDULE_PRE_DAY_RECORDED, setDay);
					continue;
				}

				//スケジュールが登録されていないときは0を代入する
				setDayValue("0", setDay);
			}

			setDay++;
		}
	}


	//dayの値全てをリセットするメソッド
	private void resetValueByAllDay() {

		day1= "";
		day2= "";
		day3= "";
		day4= "";
		day5= "";
		day6= "";
		day7= "";
		day8= "";
		day9= "";
		day10= "";
		day11= "";
		day12= "";
		day13= "";
		day14= "";
		day15= "";
		day16= "";
		day17= "";
		day18= "";
		day19= "";
		day20= "";
		day21= "";
		day22= "";
		day23= "";
		day24= "";
		day25= "";
		day26= "";
		day27= "";
		day28= "";
		day29= "";
		day30= "";
		day31= "";
	}


	//dayの値に応じた日付にvalueを追加する
	private void setDayValue(String value, int day) {

		if (day == 1) {
			day1 += value;
			return;
		}
		if (day == 2) {
			day2 += value;
			return;
		}
		if (day == 3) {
			day3 += value;
			return;
		}
		if (day == 4) {
			day4 += value;
			return;
		}
		if (day == 5) {
			day5 += value;
			return;
		}
		if (day == 6) {
			day6 += value;
			return;
		}
		if (day == 7) {
			day7 += value;
			return;
		}
		if (day == 8) {
			day8 += value;
			return;
		}
		if (day == 9) {
			day9 += value;
			return;
		}
		if (day == 10) {
			day10 += value;
			return;
		}
		if (day == 11) {
			day11 += value;
			return;
		}
		if (day == 12) {
			day12 += value;
			return;
		}
		if (day == 13) {
			day13 += value;
			return;
		}
		if (day == 14) {
			day14 += value;
			return;
		}
		if (day == 15) {
			day15 += value;
			return;
		}
		if (day == 16) {
			day16 += value;
			return;
		}
		if (day == 17) {
			day17 += value;
			return;
		}
		if (day == 18) {
			day18 += value;
			return;
		}
		if (day == 19) {
			day19 += value;
			return;
		}
		if (day == 20) {
			day20 += value;
			return;
		}
		if (day == 21) {
			day21 += value;
			return;
		}
		if (day == 22) {
			day22 += value;
			return;
		}
		if (day == 23) {
			day23 += value;
			return;
		}
		if (day == 24) {
			day24 += value;
			return;
		}
		if (day == 25) {
			day25 += value;
			return;
		}
		if (day == 26) {
			day26 += value;
			return;
		}
		if (day == 27) {
			day27 += value;
			return;
		}
		if (day == 28) {
			day28 += value;
			return;
		}
		if (day == 29) {
			day29 += value;
			return;
		}
		if (day == 30) {
			day30 += value;
			return;
		}
		if (day == 31) {
			day31 += value;
			return;
		}
	}
}
