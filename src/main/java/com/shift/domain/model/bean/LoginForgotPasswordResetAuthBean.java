package com.shift.domain.model.bean;

import com.shift.domain.model.entity.TempPasswordEntity;

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
public class LoginForgotPasswordResetAuthBean {

	private boolean isTempPasswordAuth;

	private TempPasswordEntity tempPasswordEntity;
}
