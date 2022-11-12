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
public class DmTalkSendForm {

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_DM_RECEIVE_USER_REGEXP_INPUT ,message = "入力値が不正です")
	@Length(min = Const.PATTERN_DM_RECEIVE_USER_LENGTH_MIN_INPUT, max = Const.PATTERN_DM_RECEIVE_USER_LENGTH_MAX_INPUT, message = "入力値が不正です")
	private String receiveUser;

	@NotBlank(message = "必須入力です")
	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_DM_MSG_LENGTH_MIN_INPUT, max = Const.PATTERN_DM_MSG_LENGTH_MAX_INPUT, message = "200文字以内で入力してください")
	private String msg;
}
