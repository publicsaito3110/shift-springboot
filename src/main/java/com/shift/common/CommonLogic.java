package com.shift.common;

import java.time.LocalDate;

/**
 * @author saito
 *
 */
public class CommonLogic {


	/**
	 * 改行処理
	 *
	 * <p>引数に改行コード(\n)があるとき改行タグ(br)に変換して返す<br>
	 * ただし、nullのときは何もせず返す
	 * </p>
	 *
	 * @param value 全てのString
	 * @return String 改行済みの値(nullのときは何もしない)
	 */
	public String changeAfterbreakLine(String value) {

		//nullまたは""のとき
		if (value == null || value.isEmpty()) {
			return value;
		}
		//改行対応
		return value.replaceAll("\n", "<br>");
	}


	/**
	 * 現在の日付取得処理
	 *
	 * <p>現在の日付をString(YYYYMMDD)の8桁で返す</p>
	 *
	 * @param void
	 * @return String 現在の日付(YYYYMMDD)
	 */
	public String getNowDateToYmd() {

		//現在の日付をLocalDateで取得し、ymd(YYYYMMDD)に変換する
		LocalDate nowDateLd = LocalDate.now();
		int nowYear = nowDateLd.getYear();
		int nowMonth = nowDateLd.getMonthValue();
		int nowDay = nowDateLd.getDayOfMonth();
		return String.valueOf(nowYear) + String.format("%02d", nowMonth) + String.format("%02d", nowDay);
	}


	/**
	 * LocalDate変換処理
	 *
	 * <p>ymd(YYYYMMDD)をLocalDateで返す</p>
	 *
	 * @param ymd
	 * @return LocalDate ymdから変換されたLocalDate
	 */
	public LocalDate getLocalDateByYmd(String ymd) {

		//ymdをLocalDateに変換する
		String ymdDate = ymd.substring(0, 4) + "-" + ymd.substring(4, 6) + "-" + ymd.substring(6, 8);
		LocalDate ymdLd = LocalDate.parse(ymdDate);
		return ymdLd;
	}
}
