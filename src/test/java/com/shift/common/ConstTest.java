package com.shift.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author saito
 *
 */
@SpringBootTest
class ConstTest {


	@Test
	@DisplayName("dmInputTest: inputQuery DMバリデーション")
	public void dmInputTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1_1 = getChar(1, "a");
		String valueN1_2 = getChar(200, "a");
		String valueN1_3 = getMixBlankChar(200, "a");
		String regexN1 = Const.PATTERN_DM_MSG_INPUT;

		//テストケース(異常値)
		String valueUN1_1 = getChar(201, "a");
		String valueUN1_2 = getMixBlankChar(201, "a");
		String valueUN1_3 = " ";
		String valueUN1_4 = "";
		String regexUN1 = Const.PATTERN_DM_MSG_INPUT;

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		boolean resultN1_1 = CommonUtil.isSuccessValidation(valueN1_1, regexN1);
		boolean resultN1_2 = CommonUtil.isSuccessValidation(valueN1_2, regexN1);
		boolean resultN1_3 = CommonUtil.isSuccessValidation(valueN1_3, regexN1);

		//テスト(異常値)
		boolean resultUN1_1 = CommonUtil.isSuccessValidation(valueUN1_1, regexUN1);
		boolean resultUN1_2 = CommonUtil.isSuccessValidation(valueUN1_2, regexUN1);
		boolean resultUN1_3 = CommonUtil.isSuccessValidation(valueUN1_3, regexUN1);
		boolean resultUN1_4 = CommonUtil.isSuccessValidation(valueUN1_4, regexUN1);

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(true, resultN1_1);
		assertEquals(true, resultN1_2);
		assertEquals(true, resultN1_3);

		//結果(異常値)
		assertEquals(false, resultUN1_1);
		assertEquals(false, resultUN1_2);
		assertEquals(false, resultUN1_3);
		assertEquals(false, resultUN1_4);
	}


	@Test
	@DisplayName("newsInputTest: inputQuery NEWSバリデーション")
	public void newsInputTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1_1 = "1";
		String valueN1_2 = "2";
		String valueN1_3 = "3";
		String regexN1 = Const.PATTERN_NEWS_CATEGORY_INPUT;
		String valueN2_1 = getChar(1, "a");
		String valueN2_2 = getChar(200, "a");
		String valueN2_3 = getMixBlankChar(200, "a");
		String regexN2 = Const.PATTERN_NEWS_CONTENT_INPUT;
		String valueN3_1 = getChar(1, "a");
		String valueN3_2 = getChar(20, "a");
		String valueN3_3 = getMixBlankChar(20, "a");
		String regexN3 = Const.PATTERN_NEWS_TITLE_INPUT;
		String valueN4_1 = "20220101";
		String valueN4_2 = "20221231";
		String regexN4 = Const.PATTERN_NEWS_YMD_INPUT;
		String valueN5_1 = "2022-01-01";
		String valueN5_2 = "2022-12-31";
		String regexN5 = Const.PATTERN_NEWS_UNIQUE_DATE_INPUT;

		//テストケース(異常値)
		String valueUN1_1 = "4";
		String valueUN1_2 = " ";
		String valueUN1_3 = "";
		String regexUN1 = Const.PATTERN_NEWS_CATEGORY_INPUT;
		String valueUN2_1 = getChar(201, "a");
		String valueUN2_2 = " ";
		String valueUN2_3 = "";
		String regexUN2 = Const.PATTERN_NEWS_CONTENT_INPUT;
		String valueUN3_1 = getChar(21, "a");
		String valueUN3_2 = " ";
		String valueUN3_3 = "";
		String regexUN3 = Const.PATTERN_NEWS_TITLE_INPUT;
		String valueUN4_1 = "20220100";
		String valueUN4_2 = "20221232";
		String valueUN4_3 = "20221301";
		String valueUN4_4 = "2022011";
		String valueUN4_5 = "202201012";
		String valueUN4_6 = " ";
		String valueUN4_7 = "";
		String regexUN4 = Const.PATTERN_NEWS_YMD_INPUT;
		String valueUN5_1 = "2022-01-00";
		String valueUN5_2 = "2022-12-32";
		String valueUN5_3 = "2022-13-01";
		String valueUN5_4 = "2022-01-1";
		String valueUN5_5 = "2022-01-012";
		String valueUN5_6 = "20220101";
		String valueUN5_7 = "aa-aa-aa";
		String valueUN5_8 = " ";
		String valueUN5_9 = "";
		String regexUN5 = Const.PATTERN_NEWS_UNIQUE_DATE_INPUT;

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		boolean resultN1_1 = CommonUtil.isSuccessValidation(valueN1_1, regexN1);
		boolean resultN1_2 = CommonUtil.isSuccessValidation(valueN1_2, regexN1);
		boolean resultN1_3 = CommonUtil.isSuccessValidation(valueN1_3, regexN1);
		boolean resultN2_1 = CommonUtil.isSuccessValidation(valueN2_1, regexN2);
		boolean resultN2_2 = CommonUtil.isSuccessValidation(valueN2_2, regexN2);
		boolean resultN2_3 = CommonUtil.isSuccessValidation(valueN2_3, regexN2);
		boolean resultN3_1 = CommonUtil.isSuccessValidation(valueN3_1, regexN3);
		boolean resultN3_2 = CommonUtil.isSuccessValidation(valueN3_2, regexN3);
		boolean resultN3_3 = CommonUtil.isSuccessValidation(valueN3_3, regexN3);
		boolean resultN4_1 = CommonUtil.isSuccessValidation(valueN4_1, regexN4);
		boolean resultN4_2 = CommonUtil.isSuccessValidation(valueN4_2, regexN4);
		boolean resultN5_1 = CommonUtil.isSuccessValidation(valueN5_1, regexN5);
		boolean resultN5_2 = CommonUtil.isSuccessValidation(valueN5_2, regexN5);

		//テストケース(異常値)
		boolean resultUN1_1 = CommonUtil.isSuccessValidation(valueUN1_1, regexUN1);
		boolean resultUN1_2 = CommonUtil.isSuccessValidation(valueUN1_2, regexUN1);
		boolean resultUN1_3 = CommonUtil.isSuccessValidation(valueUN1_3, regexUN1);
		boolean resultUN2_1 = CommonUtil.isSuccessValidation(valueUN2_1, regexUN2);
		boolean resultUN2_2 = CommonUtil.isSuccessValidation(valueUN2_2, regexUN2);
		boolean resultUN2_3 = CommonUtil.isSuccessValidation(valueUN2_3, regexUN2);
		boolean resultUN3_1 = CommonUtil.isSuccessValidation(valueUN3_1, regexUN3);
		boolean resultUN3_2 = CommonUtil.isSuccessValidation(valueUN3_2, regexUN3);
		boolean resultUN3_3 = CommonUtil.isSuccessValidation(valueUN3_3, regexUN3);
		boolean resultUN4_1 = CommonUtil.isSuccessValidation(valueUN4_1, regexUN4);
		boolean resultUN4_2 = CommonUtil.isSuccessValidation(valueUN4_2, regexUN4);
		boolean resultUN4_3 = CommonUtil.isSuccessValidation(valueUN4_3, regexUN4);
		boolean resultUN4_4 = CommonUtil.isSuccessValidation(valueUN4_4, regexUN4);
		boolean resultUN4_5 = CommonUtil.isSuccessValidation(valueUN4_5, regexUN4);
		boolean resultUN4_6 = CommonUtil.isSuccessValidation(valueUN4_6, regexUN4);
		boolean resultUN4_7 = CommonUtil.isSuccessValidation(valueUN4_7, regexUN4);
		boolean resultUN5_1 = CommonUtil.isSuccessValidation(valueUN5_1, regexUN5);
		boolean resultUN5_2 = CommonUtil.isSuccessValidation(valueUN5_2, regexUN5);
		boolean resultUN5_3 = CommonUtil.isSuccessValidation(valueUN5_3, regexUN5);
		boolean resultUN5_4 = CommonUtil.isSuccessValidation(valueUN5_4, regexUN5);
		boolean resultUN5_5 = CommonUtil.isSuccessValidation(valueUN5_5, regexUN5);
		boolean resultUN5_6 = CommonUtil.isSuccessValidation(valueUN5_6, regexUN5);
		boolean resultUN5_7 = CommonUtil.isSuccessValidation(valueUN5_7, regexUN5);
		boolean resultUN5_8 = CommonUtil.isSuccessValidation(valueUN5_8, regexUN5);
		boolean resultUN5_9 = CommonUtil.isSuccessValidation(valueUN5_9, regexUN5);

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(true, resultN1_1);
		assertEquals(true, resultN1_2);
		assertEquals(true, resultN1_3);
		assertEquals(true, resultN2_1);
		assertEquals(true, resultN2_2);
		assertEquals(true, resultN2_3);
		assertEquals(true, resultN3_1);
		assertEquals(true, resultN3_2);
		assertEquals(true, resultN3_3);
		assertEquals(true, resultN4_1);
		assertEquals(true, resultN4_2);
		assertEquals(true, resultN5_1);
		assertEquals(true, resultN5_2);

		//結果(異常値)
		assertEquals(false, resultUN1_1);
		assertEquals(false, resultUN1_2);
		assertEquals(false, resultUN1_3);
		assertEquals(false, resultUN2_1);
		assertEquals(false, resultUN2_2);
		assertEquals(false, resultUN2_3);
		assertEquals(false, resultUN3_1);
		assertEquals(false, resultUN3_2);
		assertEquals(false, resultUN3_3);
		assertEquals(false, resultUN4_1);
		assertEquals(false, resultUN4_2);
		assertEquals(false, resultUN4_3);
		assertEquals(false, resultUN4_4);
		assertEquals(false, resultUN4_5);
		assertEquals(false, resultUN4_6);
		assertEquals(false, resultUN4_7);
		assertEquals(false, resultUN5_1);
		assertEquals(false, resultUN5_2);
		assertEquals(false, resultUN5_3);
		assertEquals(false, resultUN5_4);
		assertEquals(false, resultUN5_5);
		assertEquals(false, resultUN5_6);
		assertEquals(false, resultUN5_7);
		assertEquals(false, resultUN5_8);
		assertEquals(false, resultUN5_9);
	}



	/**
	 * 必要な文字数を返す<br>
	 * ただし、空白文字なし
	 *
	 * @param 文字数
	 * @param 許容可能な文字
	 * @return String 必要な文字数
	 */
	private String getChar(int charNumber, String permitChara) {

		String chara = "";

		while (chara.length() < charNumber) {
			chara += permitChara;
		}

		return chara;
	}


	/**
	 * 必要な文字数を返す<br>
	 * ただし、空白文字がランダムで代入される
	 *
	 * @param 文字数
	 * @return String 必要な文字数
	 */
	private String getMixBlankChar(int charNumber, String permitChara) {

		String chara = "";
		Random random = new Random();

		while (chara.length() < charNumber) {

			int randomNum = random.nextInt(2);

			//randomNumが奇数のとき空白文字を代入
			if (randomNum % 2 == 1) {
				chara += " ";
				continue;
			}

			chara += permitChara;
		}

		//charaが空白文字だけのとき
		if (!chara.contains(permitChara)) {

			//最初の空白文字をpermitCharaに変換する
			chara.replace(" ", permitChara);
		}

		return chara;
	}
}
