package com.shift.domain.model.bean;

import com.shift.common.CommonUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationBean {

	private String inputQuery;

	private boolean isValidationSuccess;

	private String errorMessage;


	//メソッド
	public String inputQueryFormatHtml() {
		return CommonUtil.changeEmptyByNull(this.inputQuery);
	}


	public String htmlClassValid() {

		//バリデーションチェックを行っていないとき
		if (this.inputQuery == null && this.errorMessage == null) {
			return "";
		}

		//バリデーションチェック成功済みのとき
		if (this.isValidationSuccess) {

			//Bootstrapのvalid(html-class)を返す
			return "is-valid ";
		}

		//Bootstrapのinvalid(html-class)を返す
		return "is-invalid ";
	}


	public String errorMessageFormatHtml() {
		return CommonUtil.changeEmptyByNull(this.errorMessage);
	}
}
