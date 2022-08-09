package com.shift.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class UserModifyForm {

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_ID_ALL, message = "入力値が不正です")
	private String userId;

	@NotBlank(message = "必須入力です")
	private String name;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_NAME_KANA_ALL, message = "カタカナで入力してください")
	private String nameKana;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_GENDER_ALL, message = "入力値が不正です")
	private String gender;

	private String note;
}
