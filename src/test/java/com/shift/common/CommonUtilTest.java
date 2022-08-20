package com.shift.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author saito
 *
 */
@SpringBootTest
class CommonUtilTest {


	@Test
	@DisplayName("changeEmptyByNullTest: null空文字変換処理")
	public void changeEmptyByNullTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1 = null;
		String valueN2 = "";
		String valueN3 = "value";

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		String resultN1 = CommonUtil.changeEmptyByNull(valueN1);
		String resultN2 = CommonUtil.changeEmptyByNull(valueN2);
		String resultN3 = CommonUtil.changeEmptyByNull(valueN3);

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals("", resultN1);
		assertEquals("", resultN2);
		assertEquals("value", resultN3);
	}


	@Test
	@DisplayName("isSuccessValidationTest: バリデーション判定処理")
	public void isSuccessValidationTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1 = "a";
		String regexN1 = "a|b";
		String valueN2 = "a";
		String regexN2 = "1|2";
		String valueN3 = "";
		String regexN3 = "1|2";

		//テストケース(異常値)
		String valueUN1 = null;
		String regexUN1 = "a|b";
		String valueUN2 = "a";
		String regexUN2 = null;
		String valueUN3 = "a";
		String regexUN3 = "<";

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		boolean resultN1 = CommonUtil.isSuccessValidation(valueN1, regexN1);
		boolean resultN2 = CommonUtil.isSuccessValidation(valueN2, regexN2);
		boolean resultN3 = CommonUtil.isSuccessValidation(valueN3, regexN3);

		//テスト(異常値)
		boolean resultUN1 = CommonUtil.isSuccessValidation(valueUN1, regexUN1);
		boolean resultUN2 = CommonUtil.isSuccessValidation(valueUN2, regexUN2);
		boolean resultUN3 = CommonUtil.isSuccessValidation(valueUN3, regexUN3);

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(true, resultN1);
		assertEquals(false, resultN2);
		assertEquals(false, resultN3);

		//結果(異常値)
		assertEquals(false, resultUN1);
		assertEquals(false, resultUN2);
		assertEquals(false, resultUN3);
	}
}
