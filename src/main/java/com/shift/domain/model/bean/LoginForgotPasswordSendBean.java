package com.shift.domain.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForgotPasswordSendBean {

	private boolean isInsert;

	private boolean isSuccessSendEmail;
}
