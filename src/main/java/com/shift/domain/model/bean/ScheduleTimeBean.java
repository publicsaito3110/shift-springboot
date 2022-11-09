package com.shift.domain.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTimeBean {

	//フィールド
	private String name;

	private String startHm;

	private String endHm;

	private String restHm;


	/**
	 * startHm時間フォーマット変換処理<br>
	 * 時間フォーマット(HH:MM)に変換
	 *
	 * @param void
	 * @return String 時間フォーマット(HH:MM)
	 */
	public String startHmsFormatTime() {
		return chengeHmFormatTime(startHm);
	}


	/**
	 * startHm時間取得処理<br>
	 * startHmの時間(hour)を取得する
	 *
	 * @param void
	 * @return String startHmの時間(hour)
	 */
	public String getStartHmForHour() {
		return startHm.substring(0, 2);
	}


	/**
	 * startHm分取得処理<br>
	 * startHmの分(minutes)を取得する
	 *
	 * @param void
	 * @return String startHmの分(minutes)
	 */
	public String getStartHmForMinutes() {
		return startHm.substring(2, 4);
	}


	/**
	 * endHm時間フォーマット変換処理<br>
	 * 時間フォーマット(HH:MM)に変換
	 *
	 * @param void
	 * @return String 時間フォーマット(HH:MM)
	 */
	public String endHmsFormatTime() {
		return chengeHmFormatTime(endHm);
	}


	/**
	 * endHm時間取得処理<br>
	 * endHmの時間(hour)を取得する
	 *
	 * @param void
	 * @return String endHmの時間(hour)
	 */
	public String getEndHmForHour() {
		return endHm.substring(0, 2);
	}


	/**
	 * endHm分取得処理<br>
	 * endHmの分(minutes)を取得する
	 *
	 * @param void
	 * @return String endHmの分(minutes)
	 */
	public String getEndHmForMinutes() {
		return endHm.substring(2, 4);
	}


	/**
	 * restHm時間フォーマット変換処理<br>
	 * 時間フォーマット(HH:MM)に変換
	 *
	 * @param void
	 * @return String 時間フォーマット(HH:MM)
	 */
	public String restHmsFormatTime() {
		return chengeHmFormatTime(restHm);
	}


	/**
	 * restHm時間取得処理<br>
	 * restHmの時間(hour)を取得する
	 *
	 * @param void
	 * @return String restHmの時間(hour)
	 */
	public String getRestHmForHour() {
		return restHm.substring(0, 2);
	}


	/**
	 * restHm分取得処理<br>
	 * restHmの分(minutes)を取得する
	 *
	 * @param void
	 * @return String restHmの分(minutes)
	 */
	public String getRestHmForMinutes() {
		return restHm.substring(2, 4);
	}



	/**
	 * [private共通処理] 時間フォーマット変換処理<br>
	 * 時間フォーマット(HH:MM)に変換
	 *
	 * @param hm 変換したい値
	 * @return String 時間フォーマット(HH:MM)
	 */
	private String chengeHmFormatTime(String hm) {

		//フォーマットをhh:mmに変換し、返す
		return hm.substring(0, 2) + ":" + hm.substring(2, 4);
	}
}
