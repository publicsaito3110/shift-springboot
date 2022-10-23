package com.shift.form;

import javax.validation.constraints.Pattern;

import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class LoginForgotPasswordResetModifyForm {

	@Pattern(regexp = Const.PATTERN_USER_PASSWORD_INPUT, message = "4文字以上の英数字のみ有効です")
	private String password;

	private String authCode;

	private String user;

	private String urlParam;
}
