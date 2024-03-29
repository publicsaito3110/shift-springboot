package com.shift.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author saito
 *
 */
public class CommonUtil {
	private CommonUtil() {
		//インスタンス化を禁止
	}


	/**
	 * null空文字変換処理
	 *
	 * <p>nullのとき空文字に変換する<br>
	 * ただし、null以外のときは何もしない
	 * </p>
	 *
	 * @param value 全てのStringの値
	 * @return String nullのとき空文字に変換し、null以外のときは何もしない
	 */
	public static String changeEmptyByNull(String value) {

		//nullのとき
		if (value == null) {
			return "";
		}

		return value;
	}


	/**
	 * エラーメッセージ取得処理
	 *
	 * <p>バリデーション判定後のbindingResultからバリデーションエラーメッセージをListで取得する<br>
	 * ただし、bindingResultがnullまたはバリデーションエラーがないときはnullが返される
	 * </p>
	 *
	 * @param bindingResult Controllerから取得したBindingResult
	 * @return List<String> (List&lt;String&gt;)<br>
	 * バリデーション時に発生した全てのエラーメッセージ
	 */
	public static List<String> getErrorMessage(BindingResult bindingResult) {

		//bindingResultがnullのとき、nullを返す
		if (bindingResult == null) {
			return null;
		}

		//bindingResultにエラー情報がないとき、nullを返す
		if (!bindingResult.hasErrors()) {
			return null;
		}

		//bindingResultからエラーをListで取得
		List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

		//エラーメッセージを格納するための変数
		List<String> errorMessageList = new ArrayList<>();

		//fieldErrorListの回数だけループし、エラーメッセージを格納する
		for (FieldError fieldError: fieldErrorList) {
			errorMessageList.add(fieldError.getDefaultMessage());
		}

		//エラーメッセージを返す
		return errorMessageList;
	}


	/**
	 * ユーザROLE取得処理
	 *
	 * <p>authenticationからROLEを取得し、配列で返す<br>
	 * また、役職が複数ある場合は役職ごとに格納される<br>
	 * ただし、authenticationからROLEが取得できないときはnullを返す
	 * </p>
	 *
	 * @param authentication Controllerから取得したAuthentication
	 * @return String[] ROLE(役職が複数あるときは要素数が2以上になる)
	 */
	public static String[] getUserRoleArrayByAuthentication(Authentication authentication) {

		//authenticationがnullのとき
		if (authentication == null) {
			return null;
		}

		//authenticationからROLEを取得
		String userRole = authentication.getAuthorities().toString();

		//userRoleがnullのとき
		if (userRole == null) {
			return null;
		}

		//[]を削除し、ROLE_...のフォーマットにする
		String trim1UserRole = userRole.replaceAll("\\[", "");
		String trim2UserRole = trim1UserRole.replaceAll("\\]", "");

		//役職ごとに配列に変換し、返す
		String[] userRoleArray = trim2UserRole.split(", ");
		return userRoleArray;
	}


	/**
	 * バリデーション判定処理
	 *
	 * <p>バリデーションを正規表現で判定し、バリデーションの結果をbooleanで返す</p>
	 *
	 * @param value 全てのStringの値<br>
	 * ただし、nullのときは必ずバリデーションが失敗する
	 * @param regex 正規表現<br>
	 * ただし、nullまたは正規表現に則していないのときは必ずバリデーションが失敗する
	 * @return boolean<br>
	 * true: バリデーションが成功(正規表現に基づいている)<br>
	 * false: バリデーションが失敗(正規表現に基づいていないまたは引数が異常なとき)
	 */
	public static boolean isSuccessValidation(String value, String regex) {

		try {

			//バリデーションが成功したとき
			if (value.matches(regex)) {
				return true;
			}

			//バリデーション失敗時、falseを返す
			return false;
		} catch (Exception e) {

			//例外発生時、falseを返す
			return false;
		}
	}
}
