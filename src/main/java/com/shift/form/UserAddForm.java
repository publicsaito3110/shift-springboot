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
public class UserAddForm {

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_ID_INPUT, message = "半角英数字4文字で入力してください")
	private String userId;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_NAME_INPUT, message = "20文字以内で入力してください")
	private String name;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_NAME_KANA_INPUT, message = "全角カナ40文字以内で入力してください")
	private String nameKana;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_GENDER_INPUT, message = "入力値が不正です")
	private String gender;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_PASSWORD_INPUT, message = "半角英数字4文字以上で入力してください")
	private String password;

	@Pattern(regexp = Const.PATTERN_USER_ADDRESS_INPUT, message = "100文字以内で入力してください")
	private String address;

	@Pattern(regexp = Const.PATTERN_USER_TEL_INPUT, message = "数字(ハイフンなし)10-11桁で入力してください")
	private String tel;

	@Pattern(regexp = Const.PATTERN_USER_EMAIL_INPUT, message = "不正なメールアドレスです")
	private String email;

	@Pattern(regexp = Const.PATTERN_USER_NOTE_INPUT, message = "400文字以内で入力してください")
	private String note;

	@Pattern(regexp = Const.PATTERN_USER_ADMIN_FLG_INPUT, message = "入力値が不正です")
	private String adminFlg;
}
