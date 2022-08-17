package com.shift.common;

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
	 * <p>null のとき空文字に変換する<br>
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
	 * バリデーション判定処理
	 *
	 * <p>バリデーションを正規表現で判定する<br>
	 * そして、バリデーションの結果をbooleanで返す
	 * </p>
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

		} catch (Exception e) {

			//例外発生時、falseを返す
			return false;
		}

		return false;
	}
}
