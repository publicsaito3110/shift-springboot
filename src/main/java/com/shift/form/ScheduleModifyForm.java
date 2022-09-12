package com.shift.form;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class ScheduleModifyForm {

	private String[] dayArray = new String[31];

	// TODO
	//インスタンス化のとき、登録したスケジュール通りになるようにコンストラクタを修正する(th:field)
	public ScheduleModifyForm() {
		dayArray[0] = "1";
	}
}
