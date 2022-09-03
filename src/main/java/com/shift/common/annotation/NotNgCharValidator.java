package com.shift.common.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.shift.common.CommonUtil;
import com.shift.common.Const;

/**
 * @author saito
 *
 */
public class NotNgCharValidator implements ConstraintValidator<NotNgChar, String> {


	@Override
	public void initialize(NotNgChar constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		//使用不可の文字が含まれていないとき
		if (CommonUtil.isSuccessValidation(value, Const.PATTERN_CHARACTER_NOT_NG_CHAR)) {
			return true;
		}

		return false;
	}
}
