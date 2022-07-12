package com.shift.common;

/**
 * @author saito
 *
 */
public class CommonLogic {


	/**
	 * 改行処理
	 *
	 * <p>引数に改行コード(\n)があるとき改行タグ(br)に変換して返す<br>
	 * ただし、nullのときは何もせず返す
	 * </p>
	 *
	 * @param value 全てのString
	 * @return String 改行済みの値(nullのときは何もしない)
	 */
	public String changeAfterbreakLine(String value) {

		//nullまたは""のとき
		if (value.isEmpty()) {
			return value;
		}
		//改行対応
		return value.replaceAll("\n", "<br>");
	}
}
