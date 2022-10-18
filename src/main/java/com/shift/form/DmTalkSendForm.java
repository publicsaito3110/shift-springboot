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
public class DmTalkSendForm {

	@Pattern(regexp = Const.PATTERN_DM_RECEIVE_USER_INPUT ,message = "入力値が不正です")
	private String receiveUser;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_DM_MSG_INPUT, message = "入力値が不正です")
	private String msg;
}
