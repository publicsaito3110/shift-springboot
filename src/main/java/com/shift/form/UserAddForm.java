package com.shift.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.shift.common.Const;

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
public class UserAddForm {

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_ID_ALL, message = "半角英数字で入力してください")
	private String userId;

	@NotBlank(message = "必須入力です")
	private String name;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_NAME_KANA_ALL, message = "カタカナで入力してください")
	private String nameKana;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_GENDER_ALL, message = "入力値が不正です")
	private String gender;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_PASSWORD_ALL, message = "半角英数字で入力してください")
	private String password;

	private String address;

	@Pattern(regexp = Const.PATTERN_USER_TEL_ALL, message = "- (ハイフンなし)8-9桁で入力してください")
	private String tel;

	@Pattern(regexp = Const.PATTERN_USER_EMAIL_ALL, message = "不正なメールアドレスです")
	private String email;

	private String note;

	@Pattern(regexp = Const.PATTERN_USER_ADMIN_FLG_ALL, message = "入力値が不正です")
	private String adminFlg;
}
