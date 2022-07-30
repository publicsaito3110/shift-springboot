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
		String resultN1 = CommonUtil.changeEmptyByNull(valueN1);
		String resultN2 = CommonUtil.changeEmptyByNull(valueN2);
		String resultN3 = CommonUtil.changeEmptyByNull(valueN3);

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------
		assertEquals("", resultN1);
		assertEquals("", resultN2);
		assertEquals("value", resultN3);
	}
}
