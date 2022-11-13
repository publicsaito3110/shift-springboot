package com.shift.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class LoginForgotPasswordResetModifyForm {

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_USER_PASSWORD_INPUT, message = "英数字のみ有効です")
	@Length(min = Const.PATTERN_USER_PASSWORD_LENGTH_MIN_INPUT, max = Const.PATTERN_USER_PASSWORD_LENGTH_MAX_INPUT, message = "4～80文字以内で入力してください")
	private String password;

	private String authCode;

	private String user;

	private String urlParam;
}
