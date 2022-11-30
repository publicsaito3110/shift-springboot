package com.shift.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author saito
 *
 */
public class CommonLogic {


	/**
	 * 改行スペース処理
	 *
	 * <p>引数に改行コード(\n)があるとき改行タグ&lt;br&gt;に変換して返す<br>
	 * また、スペースが存在するときは"&nbsp"が変換される<br>
	 * ただし、nullのときは何もしない
	 * </p>
	 *
	 * @param value 全てのString
	 * @return String 改行済みの値<br>
	 * ただし、nullのときは何もしない
	 */
	public String changeAfterBreakLine(String value) {

		//nullまたは""のとき
		if (value == null || value.isEmpty()) {
			return value;
		}

		//改行及びスペース対応
		String afterBreakLine = value.replaceAll(Const.CHARACTER_CODE_BREAK_LINE_ALL, "<br>");
		return afterBreakLine.replaceAll(Const.CHARACTER_SPACE, Const.HTML_SPACE);
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
	 * 最終日の日付取得処理
	 *
	 * <p>year, monthからその年月の最終日の日付をString(YYYYMMDD)の8桁で返す</p>
	 *
	 * @param void
	 * @return String 最終日の日付(YYYYMMDD)
	 */
	public String getLastDateYmd(int year, int month) {

		try {

			//現在の日付をLocalDateで取得し、ymd(YYYYMMDD)に変換する
			LocalDate localDate = LocalDate.of(year, month, 1);
			int lastDay = localDate.lengthOfMonth();
			return String.valueOf(year) + String.format("%02d", month) + String.format("%02d", lastDay);
		} catch (Exception e) {
			//例外発生時、nullを返す
			return null;
		}
	}


	/**
	 * LocalDate変換処理
	 *
	 * <p>ymd(YYYYMMDD)をLocalDateで返す<br>
	 * ただし、ymdがYYYYMMDDでない又は存在しない日付のときはnullを返す
	 * </p>
	 *
	 * @param ymd (YYYYMMDD)
	 * @return LocalDate ymdから変換されたLocalDate<br>
	 * ただし、ymdがフォーマット通りでないときはnullとなる
	 */
	public LocalDate getLocalDateByYmd(String ymd) {

		//ymdがnullのとき
		if (ymd == null) {
			return null;
		}

		//ymdが8桁でないとき
		if (ymd.length() != 8) {
			return null;
		}

		try {

			//ymdをLocalDateに変換する
			String ymdDate = ymd.substring(0, 4) + "-" + ymd.substring(4, 6) + "-" + ymd.substring(6, 8);
			LocalDate ymdLd = LocalDate.parse(ymdDate);
			return ymdLd;
		} catch (Exception e) {

			//例外発生時、nullを返す
			return null;
		}
	}


	/**
	 * 年月変換処理
	 *
	 * <p>year(int), month(int)をString型ym(YYYYMM)に変換して返す</p>
	 *
	 * @param year 年(int)
	 * @param month 月(int)
	 * @return String ym(YYYYMM)
	 */
	public String toStringYmFormatSixByYearMonth(int year, int month) {

		//ym(YYYYMM)に変換する
		return String.valueOf(year) + String.format("%02d", month);
	}


	/**
	 * 日付変換処理
	 *
	 * <p>year(int), month(int), day(int)をString型ymd(YYYYMMDD)に変換して返す</p>
	 *
	 * @param year 年(int)
	 * @param month 月(int)
	 * @param day 日(int)
	 * @return String 日付ymd(YYYYMM)
	 */
	public String toStringYmdByYearMonthDay(int year, int month, int day) {

		//ymd(YYYYMMDD)に変換する
		return String.valueOf(year) + String.format("%02d", month) + String.format("%02d", day);
	}


	/**
	 * 時間フォーマット変換処理
	 *
	 * <p>ミリ秒に変換された時間から時間フォーマット(HH.M)に変換して返す<br>
	 * ex) 360000ミリ秒(1時間00分) -> 1.0, 2160000ミリ秒(1時間30分) -> 1.3
	 * </p>
	 *
	 * @param hmMs ミリ秒換算された時間
	 * @return String 時間フォーマットに変換された値(HH.M)
	 */
	public String toStringFormatTime(long hmMs) {

		//時間換算するための数字をBigDecimalで取得
		BigDecimal num3600000Bd = new BigDecimal(String.valueOf("3600000"));
		BigDecimal num60000Bd = new BigDecimal(String.valueOf("60000"));
		BigDecimal num60Bd = new BigDecimal(String.valueOf("60"));

		//ミリ秒をBigDecimalで取得
		BigDecimal hmMsBd = new BigDecimal(String.valueOf(String.valueOf(hmMs)));

		//ミリ秒から時間(hour)を計算
		BigDecimal hourBd = hmMsBd.divide(num3600000Bd, 0, RoundingMode.DOWN);

		//ミリ秒から分(minutes)を計算
		BigDecimal minuresBd = hmMsBd.divide(num60000Bd, 0, RoundingMode.DOWN).remainder(num60Bd);

		//時間フォーマットに変換して、返す
		BigDecimal hmTimeBd = hourBd.add(minuresBd.divide(num60Bd, 1, RoundingMode.DOWN));
		return hmTimeBd.toString();
	}


	/**
	 * ミリ秒換算処理
	 *
	 * <p>hm(HHMM)をミリ秒に換算して返す<br>
	 * ただし、時間がlongの取得可能数値を超えてミリ秒換算できないまたは引数が指定フォーマット以外のときは必ずlongの最小値(-9223372036854775808)が返される
	 * </p>
	 *
	 * @param hour 時間(hour)
	 * @param minutes 分(minutes)
	 * @return long ミリ秒換算した時間
	 */
	public long chengeHmMsByHourMinutes(String hour, String minutes) {


		try {

			//時間フォーマット(HH:MM)に変換
			String hmTime = hour + ":" + minutes;

			//hmフォーマットの文字列をDate型に変換するクラス
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

			//それぞれの時間をDate型に変換
			Date hmDate = simpleDateFormat.parse(hmTime);

			//SimpleDateFormat標準時刻差分を差し引いた時間をミリ秒で取得し、返す
			long hmTimeMs = hmDate.getTime() - Const.SIMPLE_DATE_FORMAT_SERVER_TIME_ZONE_JP_DISTANCE;
			return hmTimeMs;
		} catch (Exception e) {

			//例外発生時、longの最小値を返す
			return Long.MIN_VALUE;
		}
	}
}
