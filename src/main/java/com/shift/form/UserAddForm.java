package com.shift.form;

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
public class UserAddForm {

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_ID_INPUT, message = "半角英数字のみ有効です")
	@Length(min = Const.PATTERN_USER_ID_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_ID_LENGTH_MAX_INPUT, message = "4文字のみ有効です")
	private String userId;

	@NotBlank(message = "必須入力です")
	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_USER_NAME_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_NAME_LENGTH_MAX_INPUT, message = "20文字以内のみ有効です")
	private String name;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_NAME_KANA_INPUT, message = "全角カナで入力してください")
	@Length(min = Const.PATTERN_USER_NAME_KANA_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_NAME_KANA_LENGTH_MAX_INPUT, message = "40文字以内のみ有効です")
	private String nameKana;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_GENDER_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_USER_GENDER_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_GENDER_LENGTH_MAX_INPUT, message = "入力値が不正です")
	private String gender;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_PASSWORD_INPUT, message = "半角英数字で入力してください")
	@Length(min = Const.PATTERN_USER_PASSWORD_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_PASSWORD_LENGTH_MAX_INPUT, message = "4文字以上で入力してください")
	private String password;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_USER_ADDRESS_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_ADDRESS_LENGTH_MAX_INPUT, message = "100文字以内で入力してください")
	private String address;

	@Pattern(regexp = Const.PATTERN_USER_TEL_INPUT, message = "数字(ハイフンなし)10-11桁で入力してください")
	@Length(min = Const.PATTERN_USER_TEL_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_TEL_LENGTH_MAX_INPUT, message = "数字(ハイフンなし)10-11桁で入力してください")
	private String tel;

	@Pattern(regexp = Const.PATTERN_USER_EMAIL_INPUT, message = "有効なメールアドレスを入力してください")
	@Length(min = Const.PATTERN_USER_EMAIL_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_EMAIL_LENGTH_MAX_INPUT, message = "254文字以内で入力してください")
	private String email;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_USER_NOTE_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_NOTE_LENGTH_MAX_INPUT, message = "400文字以内で入力してください")
	private String note;

	@Pattern(regexp = Const.PATTERN_USER_ADMIN_FLG_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_USER_ADMIN_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_ADMIN_LENGTH_MAX_INPUT, message = "入力値が不正です")
	private String adminFlg;
}
