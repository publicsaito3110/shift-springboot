package com.shift.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.shift.common.Const;
import com.shift.common.annotation.NotNgChar;
import com.shift.common.annotation.NotOnlyWhitespace;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class ShiftEditAddForm {

	@NotBlank(message = "入力が不正です")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_LENGTH_MIN_INPUT, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_LENGTH_MAX_INPUT, message = "入力値が不正です")
	private String startYm;

	@NotBlank(message = "必須入力です")
	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT, max = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT, message = "20文字以内で入力してください")
	private String name1;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT, message = "入力値が不正です")
	private String startHm1;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT, message = "入力値が不正です")
	private String endHm1;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT, message = "入力値が不正です")
	private String restHm1;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT_OPTIONAL, message = "20文字以内で入力してください")
	private String name2;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String startHm2;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String endHm2;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String restHm2;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT_OPTIONAL, message = "20文字以内で入力してください")
	private String name3;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String startHm3;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String endHm3;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String restHm3;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT_OPTIONAL, message = "20文字以内で入力してください")
	private String name4;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String startHm4;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String endHm4;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String restHm4;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT_OPTIONAL, message = "20文字以内で入力してください")
	private String name5;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String startHm5;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String endHm5;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String restHm5;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT_OPTIONAL, message = "20文字以内で入力してください")
	private String name6;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String startHm6;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String endHm6;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String restHm6;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_NAME_LENGTH_MAX_INPUT_OPTIONAL, message = "20文字以内で入力してください")
	private String name7;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String startHm7;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String endHm7;

	@Pattern(regexp = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_INPUT_OPTIONAL, message = "入力値が不正です")
	@Length(min = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MIN_INPUT_OPTIONAL, max = Const.PATTERN_SCHEDULE_TIME_UNIQUE_HM_LENGTH_MAX_INPUT_OPTIONAL, message = "入力値が不正です")
	private String restHm7;

	private String nowYm;


	/**
	 * バリデーションエラー判定(相関チェック)
	 *
	 * <p>それぞれのスケジュール時間区分の入力値に不備がないかを判定する<br>
	 * ただし、相関チェックでエラーのとき、trueが返される
	 * </p>
	 *
	 * @param void
	 * @return boolean<br>
	 * true: 相関チェックでエラーが発生したとき<br>
	 * false: 相関チェックが成功したとき
	 */
	public boolean isErrorValidRelatied() {

		//それぞれの入力値に不備がないかを判定する
		List<String> checkElectiveList = new ArrayList<>();
		checkElectiveList.add(checkElectiveValue(name1, startHm1, endHm1, restHm1));
		checkElectiveList.add(checkElectiveValue(name2, startHm2, endHm2, restHm2));
		checkElectiveList.add(checkElectiveValue(name3, startHm3, endHm3, restHm3));
		checkElectiveList.add(checkElectiveValue(name4, startHm4, endHm4, restHm4));
		checkElectiveList.add(checkElectiveValue(name5, startHm5, endHm5, restHm5));
		checkElectiveList.add(checkElectiveValue(name6, startHm6, endHm6, restHm6));
		checkElectiveList.add(checkElectiveValue(name7, startHm7, endHm7, restHm7));

		//1つでも入力値に不備がある(nullが含まれている)とき、trueを返す
		if (checkElectiveList.contains(null)) {
			return true;
		}

		//スケジュール時間区分が入力されていないか判定する変数
		boolean isNotInputValue = false;

		//checkElectiveListだけループし、スケジュール時間区分が適切な順番で入力されているか判定する
		for (String checkElective: checkElectiveList) {

			//スケジュール時間区分が入力されていないとき、isNotInputValueをfalseにする
			if ("0".equals(checkElective)) {
				isNotInputValue = true;
			}

			//その前のスケジュール時間区分が未入力であるにもかかわらず入力済みのスケジュール時間区分が存在するとき、trueを返す
			if ("1".equals(checkElective) && isNotInputValue) {
				return true;
			}
		}

		//入力値に不備がないとき、falseを返す
		return false;
	}



	/**
	 * [private共通処理] 入力値の入力済み判定
	 *
	 * <p>入力値の値によって返す変化する</p>
	 *
	 * @param name スケジュール時間区分の名前
	 * @param startHm スケジュール時間区分の開始時刻
	 * @param endHm スケジュール時間区分の終了時刻
	 * @param restHm スケジュール時間区分の休憩時間
	 * @return String<br>
	 * 0: 全ての入力値が未入力(空文字)のとき<br>
	 * 1: 全ての入力値が入力済みのとき<br>
	 * null: 1つでも入力済みの項目があるにもかかわらず、全ての入力値が入力されていないとき
	 */
	private String checkElectiveValue(String name, String startHm, String endHm, String restHm) {

		if ("".equals(name) && "".equals(startHm) && "".equals(endHm) && "".equals(restHm)) {

			//入力項目が全て空文字のとき、"0"を返す
			return "0";
		} else if (!"".equals(name) && !"".equals(startHm) && !"".equals(endHm) && !"".equals(restHm)) {

			//入力項目が全て入力済みのとき、"1"を返す
			return "1";
		} else {

			//入力項目に入力事項があり1つでも未入力の項目があるとき、nullを返す
			return null;
		}
	}
}
