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


	//メソッド
	public String startHmsFormatTime() {
		return hmFormatTime(startHm);
	}

	public String endHmsFormatTime() {
		return hmFormatTime(endHm);
	}

	public String restHmsFormatTime() {
		return hmFormatTime(restHm);
	}

	//private共通処理
	private String hmFormatTime(String hm) {

		//フォーマットをhh:mmに変換する
		return hm.substring(0, 2) + ":" + hm.substring(2, 4);
	}
}
