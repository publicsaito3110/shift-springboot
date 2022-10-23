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
public class LoginForgotPasswordResetModifyBean {

	private boolean isTempPasswordAuth;

	private boolean isUpdate;

	private TempPasswordEntity tempPasswordEntity;
}
