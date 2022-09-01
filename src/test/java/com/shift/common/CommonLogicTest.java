package com.shift.common;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author saito
 *
 */
@SpringBootTest
class CommonLogicTest {

	private CommonLogic commonLogic = new CommonLogic();


	@Test
	@DisplayName("changeAfterbreakLineTest: 改行対応処理")
	public void changeAfterbreakLineTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1 = null;
		String valueN2 = "";
		String valueN3 = "改行なし";
		String valueN4 = "改行あり" + "\n";

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		String resultN1 = commonLogic.changeAfterBreakLine(valueN1);
		String resultN2 = commonLogic.changeAfterBreakLine(valueN2);
		String resultN3 = commonLogic.changeAfterBreakLine(valueN3);
		String resultN4 = commonLogic.changeAfterBreakLine(valueN4);

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(null, resultN1);
		assertEquals("", resultN2);
		assertEquals("改行なし", resultN3);
		assertEquals("改行あり<br>", resultN4);
	}


	@Test
	@DisplayName("getLocalDateByYmd: LocalDate変換処理")
	public void getLocalDateByYmdTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1 = "20220101";
		String valueN2 = "20221231";

		//テストケース(異常値)
		String valueUN1 = null;
		String valueUN2 = "";
		String valueUN3 = "200000";
		String valueUN4 = "2022";
		String valueUN5 = "202201012222";
		String valueUN6 = "20220100";
		String valueUN7 = "20221232";

		//期待値(正常値)
		LocalDate expectN1 = LocalDate.of(2022, 1, 1);
		LocalDate expectN2 = LocalDate.of(2022, 12, 31);

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		LocalDate resultN1 = commonLogic.getLocalDateByYmd(valueN1);
		LocalDate resultN2 = commonLogic.getLocalDateByYmd(valueN2);

		//テスト(異常値)
		LocalDate resultUn1 = commonLogic.getLocalDateByYmd(valueUN1);
		LocalDate resultUn2 = commonLogic.getLocalDateByYmd(valueUN2);
		LocalDate resultUn3 = commonLogic.getLocalDateByYmd(valueUN3);
		LocalDate resultUn4 = commonLogic.getLocalDateByYmd(valueUN4);
		LocalDate resultUn5 = commonLogic.getLocalDateByYmd(valueUN5);
		LocalDate resultUn6 = commonLogic.getLocalDateByYmd(valueUN6);
		LocalDate resultUn7 = commonLogic.getLocalDateByYmd(valueUN7);
		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(expectN1, resultN1);
		assertEquals(expectN2, resultN2);

		//結果(異常値)
		assertEquals(null, resultUn1);
		assertEquals(null, resultUn2);
		assertEquals(null, resultUn3);
		assertEquals(null, resultUn4);
		assertEquals(null, resultUn5);
		assertEquals(null, resultUn6);
		assertEquals(null, resultUn7);
	}
}
