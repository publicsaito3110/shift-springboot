package com.shift.common;

import java.util.ArrayList;
import java.util.List;

import com.shift.domain.model.bean.ValidationBean;

/**
 * @author saito
 *
 */
public class ValidationSingleLogic {

	private List<String> inputQueryList = new ArrayList<>();
	private List<Boolean> isValidationSuccessList = new ArrayList<>();
	private List<String> errorMessageList = new ArrayList<>();


	//コンストラクタ
	public ValidationSingleLogic(String inputQuery, String regex, String errorMessage) {
		this.checkValidation(inputQuery, regex, errorMessage);
	}


	/**
	 * バリデーション処理
	 *
	 * <p>バリデーションをの結果をフィールドに反映させる<br>
	 * ただし、バリデーションエラーやException発生時はバリデーションエラーの結果が反映される
	 * </p>
	 *
	 * @param inputQuery 入力値<br>
	 * ただし、nullのときは必ずバリデーションが失敗する
	 * @param regex 正規表現<br>
	 * ただし、nullまたは正規表現に則していないのときは必ずバリデーションが失敗する
	 * @param errorMessage バリデーションエラーのときのメッセージ
	 */
	public void checkValidation(String inputQuery, String regex, String errorMessage) {

		//バリデーションを判定
		if (CommonUtil.isSuccessValidation(inputQuery, regex)) {

			//バリデーションチェックが成功していることを反映
			this.inputQueryList.add(inputQuery);
			this.errorMessageList.add("");
			this.isValidationSuccessList.add(true);
			return;
		}

		//バリデーションチェックがエラーであることを反映
		this.inputQueryList.add(inputQuery);
		this.errorMessageList.add(errorMessage);
		this.isValidationSuccessList.add(false);
	}


	/**
	 * バリデーション結果取得処理
	 *
	 * <p>バリデーションを結果を取得する</p>
	 *
	 * @param void
	 * @return List<ValidationBean><br>
	 * フィールド(List&lt;ValidationBean&gt;)<br>
	 * isValidationSuccess, errorMessage
	 */
	public List<ValidationBean> getValidationResult() {

		List<ValidationBean> ValidationBeanList = new ArrayList<>();

		//バリデーションチェックを行った回数分、結果をValidationBeanListに格納
		for (int i = 0; i < this.isValidationSuccessList.size(); i++) {

			String inputQuery = this.inputQueryList.get(i);
			boolean isValidationSuccess = this.isValidationSuccessList.get(i);
			String errorMessage = this.errorMessageList.get(i);
			ValidationBeanList.add(new ValidationBean(inputQuery, isValidationSuccess, errorMessage));
		}

		return ValidationBeanList;
	}


	/**
	 * バリデーションエラー判定処理
	 *
	 * <p>今までのバリデーションチェックからエラーがあるか判定する</p>
	 *
	 * @return boolean<br>
	 * true: バリデーションが全て成功しているとき<br>
	 * false: 一度でもバリデーションエラーがあるとき
	 */
	public boolean isValidationEroor() {

		//isValidationSuccessListにバリデーションエラーが含まれているとき
		if (this.isValidationSuccessList.contains(false)) {
			return true;
		}

		return false;
	}
}
