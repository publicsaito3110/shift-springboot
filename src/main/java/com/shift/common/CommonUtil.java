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
	 * @param value 全てのString型の変数
	 * @return String nullのとき空文字に変換し、null以外のときは何もしない
	 */
	public static String changeEmptyByNull(String value) {

		//nullのとき
		if (value == null) {
			return "";
		}

		return value;
	}
}
