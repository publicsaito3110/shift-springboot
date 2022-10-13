package com.shift.form;

import javax.validation.constraints.Pattern;

import com.shift.common.Const;
import com.shift.common.annotation.NotNgChar;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class ShiftEditAddForm {

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_INPUT, message = "入力値が不正です")
	private String startYm;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_NAME_MUST_INPUT, message = "入力値が不正です")
	private String name1;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_MUST_INPUT, message = "入力値が不正です")
	private String startHm1;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_MUST_INPUT, message = "入力値が不正です")
	private String endHm1;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_MUST_INPUT, message = "入力値が不正です")
	private String restHm1;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_NAME_ELECTIVE_INPUT, message = "入力値が不正です")
	private String name2;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String startHm2;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String endHm2;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String restHm2;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_NAME_ELECTIVE_INPUT, message = "入力値が不正です")
	private String name3;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String startHm3;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String endHm3;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String restHm3;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_NAME_ELECTIVE_INPUT, message = "入力値が不正です")
	private String name4;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String startHm4;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String endHm4;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String restHm4;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_NAME_ELECTIVE_INPUT, message = "入力値が不正です")
	private String name5;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String startHm5;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String endHm5;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String restHm5;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_NAME_ELECTIVE_INPUT, message = "入力値が不正です")
	private String name6;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String startHm6;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String endHm6;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String restHm6;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_NAME_ELECTIVE_INPUT, message = "入力値が不正です")
	private String name7;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String startHm7;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String endHm7;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT, message = "入力値が不正です")
	private String restHm7;



	/**
	 * バリデーションエラー判定(相関チェック)
	 *
	 * @param void
	 * @return boolean<br>
	 * true: 相関チェックでエラーが発生したとき<br>
	 * false: 相関チェックが成功したとき
	 */
	public boolean isErrorValidRelatied() {
		return true;
	}
}
