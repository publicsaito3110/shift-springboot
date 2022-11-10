package com.shift.common.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.shift.common.Const;

/**
 * @author saito
 *
 */
public class NotOnlyWhitespaceValidator implements ConstraintValidator<NotOnlyWhitespace, String> {


	@Override
	public void initialize(NotOnlyWhitespace constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		//nullまたは空文字のとき
		if (value == null || value.isEmpty()) {
			return true;
		}

		//空文字及び改行コードを全て空文字に変換する
		String afterOnlyChar = value.replaceAll(Const.PATTERN_CHARACTER_WHITESPACE, "");

		//空文字でない(変換前の値が空白文字及び改行コードのみではなく、何かしらの文字がある)とき
		if (!"".equals(afterOnlyChar)) {
			return true;
		}

		return false;
	}
}
