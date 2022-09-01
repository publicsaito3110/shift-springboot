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


	@Test
	@DisplayName("userInputTest: inputQuery USERバリデーション")
	public void userInputTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1_1 = "a";
		String valueN1_2 = getMixBlankChar(100, "a");
		String valueN1_3 = "";
		String regexN1 = Const.PATTERN_USER_ADDRESS_INPUT;
		String valueN2_1 = "1";
		String valueN2_2 = "";
		String regexN2 = Const.PATTERN_USER_ADMIN_FLG_INPUT;
		String valueN3_1 = "1@a";
		String valueN3_2 = "daf@f";
		String valueN3_3 = "..@d";
		String valueN3_4 = "";
		String regexN3 = Const.PATTERN_USER_EMAIL_INPUT;
		String valueN4_1 = "1";
		String valueN4_2 = "2";
		String regexN4 = Const.PATTERN_USER_GENDER_INPUT;
		String valueN5_1 = "a";
		String valueN5_2 = getMixBlankChar(20, "a");
		String regexN5 = Const.PATTERN_USER_NAME_INPUT;
		String valueN6_1 = "カ";
		String valueN6_2 = getMixBlankChar(40, "カ");
		String regexN6 = Const.PATTERN_USER_NAME_KANA_INPUT;
		String valueN7_1 = "a";
		String valueN7_2 = getMixBlankChar(400, "a");
		String valueN7_3 = "";
		String regexN7 = Const.PATTERN_USER_NOTE_INPUT;
		String valueN8_1 = getChar(4, "a");
		String valueN8_2 = getChar(4, "A");
		String valueN8_3 = getChar(4, "0");
		String regexN8 = Const.PATTERN_USER_PASSWORD_INPUT;
		String valueN9_1 = getChar(10, "0");
		String valueN9_2 = getChar(11, "0");
		String valueN9_4 = "";
		String regexN9 = Const.PATTERN_USER_TEL_INPUT;

		//テストケース(異常値)
		String valueUN1_1 = getMixBlankChar(101, "a");
		String valueUN1_2 = " ";
		String regexUN1 = Const.PATTERN_USER_ADDRESS_INPUT;
		String valueUN2_1 = "2";
		String valueUN2_2 = " ";
		String regexUN2 = Const.PATTERN_USER_ADMIN_FLG_INPUT;
		String valueUN3_1 = "a";
		String valueUN3_2 = "a@";
		String valueUN3_3 = "a@@b";
		String valueUN3_4 = "@b";
		String valueUN3_5 = "あ@c";
		String valueUN3_6 = " ";
		String regexUN3 = Const.PATTERN_USER_EMAIL_INPUT;
		String valueUN4_1 = "3";
		String valueUN4_2 = "0";
		String valueUN4_3 = " ";
		String regexUN4 = Const.PATTERN_USER_GENDER_INPUT;
		String valueUN5_1 = getMixBlankChar(21, "a");
		String valueUN5_2 = " ";
		String valueUN5_3 = "";
		String regexUN5 = Const.PATTERN_USER_NAME_INPUT;
		String valueUN6_1 = getMixBlankChar(41, "ア");
		String valueUN6_2 = "あ";
		String valueUN6_3 = "a";
		String valueUN6_4 = "A";
		String valueUN6_5 = "1";
		String valueUN6_6 = " ";
		String valueUN6_7 = "";
		String regexUN6 = Const.PATTERN_USER_NAME_KANA_INPUT;
		String valueUN7_1 = getMixBlankChar(401, "a");
		String valueUN7_2 = " ";
		String regexUN7 = Const.PATTERN_USER_NOTE_INPUT;
		String valueUN8_1 = "a";
		String valueUN8_2 = "aaa";
		String valueUN8_3 = "aaa";
		String valueUN8_4 = getChar(4, "ア");
		String valueUN8_5 = getChar(41, "あ");
		String valueUN8_6 = "12 34";
		String valueUN8_7 = " ";
		String valueUN8_8 = "";
		String regexUN8 = Const.PATTERN_USER_PASSWORD_INPUT;
		String valueUN9_1 = getChar(9, "0");
		String valueUN9_2 = getChar(12, "0");
		String valueUN9_3 = "123 456 78";
		String valueUN9_4 = getChar(9, "ア");
		String valueUN9_5 = getChar(9, "a");
		String valueUN9_6 = "123 456 78";
		String regexUN9 = Const.PATTERN_USER_TEL_INPUT;

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		boolean resultN1_1 = CommonUtil.isSuccessValidation(valueN1_1, regexN1);
		boolean resultN1_2 = CommonUtil.isSuccessValidation(valueN1_2, regexN1);
		boolean resultN1_3 = CommonUtil.isSuccessValidation(valueN1_3, regexN1);
		boolean resultN2_1 = CommonUtil.isSuccessValidation(valueN2_1, regexN2);
		boolean resultN2_2 = CommonUtil.isSuccessValidation(valueN2_2, regexN2);
		boolean resultN3_1 = CommonUtil.isSuccessValidation(valueN3_1, regexN3);
		boolean resultN3_2 = CommonUtil.isSuccessValidation(valueN3_2, regexN3);
		boolean resultN3_3 = CommonUtil.isSuccessValidation(valueN3_3, regexN3);
		boolean resultN3_4 = CommonUtil.isSuccessValidation(valueN3_4, regexN3);
		boolean resultN4_1 = CommonUtil.isSuccessValidation(valueN4_1, regexN4);
		boolean resultN4_2 = CommonUtil.isSuccessValidation(valueN4_2, regexN4);
		boolean resultN5_1 = CommonUtil.isSuccessValidation(valueN5_1, regexN5);
		boolean resultN5_2 = CommonUtil.isSuccessValidation(valueN5_2, regexN5);
		boolean resultN6_1 = CommonUtil.isSuccessValidation(valueN6_1, regexN6);
		boolean resultN6_2 = CommonUtil.isSuccessValidation(valueN6_2, regexN6);
		boolean resultN7_1 = CommonUtil.isSuccessValidation(valueN7_1, regexN7);
		boolean resultN7_2 = CommonUtil.isSuccessValidation(valueN7_2, regexN7);
		boolean resultN7_3 = CommonUtil.isSuccessValidation(valueN7_3, regexN7);
		boolean resultN8_1 = CommonUtil.isSuccessValidation(valueN8_1, regexN8);
		boolean resultN8_2 = CommonUtil.isSuccessValidation(valueN8_2, regexN8);
		boolean resultN8_3 = CommonUtil.isSuccessValidation(valueN8_3, regexN8);
		boolean resultN9_1 = CommonUtil.isSuccessValidation(valueN9_1, regexN9);
		boolean resultN9_2 = CommonUtil.isSuccessValidation(valueN9_2, regexN9);
		boolean resultN9_4 = CommonUtil.isSuccessValidation(valueN9_4, regexN9);

		//テストケース(異常値)
		boolean resultUN1_1 = CommonUtil.isSuccessValidation(valueUN1_1, regexUN1);
		boolean resultUN1_2 = CommonUtil.isSuccessValidation(valueUN1_2, regexUN1);
		boolean resultUN2_1 = CommonUtil.isSuccessValidation(valueUN2_1, regexUN2);
		boolean resultUN2_2 = CommonUtil.isSuccessValidation(valueUN2_2, regexUN2);
		boolean resultUN3_1 = CommonUtil.isSuccessValidation(valueUN3_1, regexUN3);
		boolean resultUN3_2 = CommonUtil.isSuccessValidation(valueUN3_2, regexUN3);
		boolean resultUN3_3 = CommonUtil.isSuccessValidation(valueUN3_3, regexUN3);
		boolean resultUN3_4 = CommonUtil.isSuccessValidation(valueUN3_4, regexUN3);
		boolean resultUN3_5 = CommonUtil.isSuccessValidation(valueUN3_5, regexUN3);
		boolean resultUN3_6 = CommonUtil.isSuccessValidation(valueUN3_6, regexUN3);
		boolean resultUN4_1 = CommonUtil.isSuccessValidation(valueUN4_1, regexUN4);
		boolean resultUN4_2 = CommonUtil.isSuccessValidation(valueUN4_2, regexUN4);
		boolean resultUN4_3 = CommonUtil.isSuccessValidation(valueUN4_3, regexUN4);
		boolean resultUN5_1 = CommonUtil.isSuccessValidation(valueUN5_1, regexUN5);
		boolean resultUN5_2 = CommonUtil.isSuccessValidation(valueUN5_2, regexUN5);
		boolean resultUN5_3 = CommonUtil.isSuccessValidation(valueUN5_3, regexUN5);
		boolean resultUN6_1 = CommonUtil.isSuccessValidation(valueUN6_1, regexUN6);
		boolean resultUN6_2 = CommonUtil.isSuccessValidation(valueUN6_2, regexUN6);
		boolean resultUN6_3 = CommonUtil.isSuccessValidation(valueUN6_3, regexUN6);
		boolean resultUN6_4 = CommonUtil.isSuccessValidation(valueUN6_4, regexUN6);
		boolean resultUN6_5 = CommonUtil.isSuccessValidation(valueUN6_5, regexUN6);
		boolean resultUN6_6 = CommonUtil.isSuccessValidation(valueUN6_6, regexUN6);
		boolean resultUN6_7 = CommonUtil.isSuccessValidation(valueUN6_7, regexUN6);
		boolean resultUN7_1 = CommonUtil.isSuccessValidation(valueUN7_1, regexUN7);
		boolean resultUN7_2 = CommonUtil.isSuccessValidation(valueUN7_2, regexUN7);
		boolean resultUN8_1 = CommonUtil.isSuccessValidation(valueUN8_1, regexUN8);
		boolean resultUN8_2 = CommonUtil.isSuccessValidation(valueUN8_2, regexUN8);
		boolean resultUN8_3 = CommonUtil.isSuccessValidation(valueUN8_3, regexUN8);
		boolean resultUN8_4 = CommonUtil.isSuccessValidation(valueUN8_4, regexUN8);
		boolean resultUN8_5 = CommonUtil.isSuccessValidation(valueUN8_5, regexUN8);
		boolean resultUN8_6 = CommonUtil.isSuccessValidation(valueUN8_6, regexUN8);
		boolean resultUN8_7 = CommonUtil.isSuccessValidation(valueUN8_7, regexUN8);
		boolean resultUN8_8 = CommonUtil.isSuccessValidation(valueUN8_8, regexUN8);
		boolean resultUN9_1 = CommonUtil.isSuccessValidation(valueUN9_1, regexUN9);
		boolean resultUN9_2 = CommonUtil.isSuccessValidation(valueUN9_2, regexUN9);
		boolean resultUN9_3 = CommonUtil.isSuccessValidation(valueUN9_3, regexUN9);
		boolean resultUN9_4 = CommonUtil.isSuccessValidation(valueUN9_4, regexUN9);
		boolean resultUN9_5 = CommonUtil.isSuccessValidation(valueUN9_5, regexUN9);
		boolean resultUN9_6 = CommonUtil.isSuccessValidation(valueUN9_6, regexUN9);


		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(true, resultN1_1);
		assertEquals(true, resultN1_2);
		assertEquals(true, resultN1_3);
		assertEquals(true, resultN2_1);
		assertEquals(true, resultN2_2);
		assertEquals(true, resultN3_1);
		assertEquals(true, resultN3_2);
		assertEquals(true, resultN3_3);
		assertEquals(true, resultN3_4);
		assertEquals(true, resultN4_1);
		assertEquals(true, resultN4_2);
		assertEquals(true, resultN5_1);
		assertEquals(true, resultN5_2);
		assertEquals(true, resultN6_1);
		assertEquals(true, resultN6_2);
		assertEquals(true, resultN7_1);
		assertEquals(true, resultN7_2);
		assertEquals(true, resultN7_3);
		assertEquals(true, resultN8_1);
		assertEquals(true, resultN8_2);
		assertEquals(true, resultN8_3);
		assertEquals(true, resultN9_1);
		assertEquals(true, resultN9_2);
		assertEquals(true, resultN9_4);


		//結果(異常値)
		assertEquals(false, resultUN1_1);
		assertEquals(false, resultUN1_2);
		assertEquals(false, resultUN2_1);
		assertEquals(false, resultUN2_2);
		assertEquals(false, resultUN3_1);
		assertEquals(false, resultUN3_2);
		assertEquals(false, resultUN3_3);
		assertEquals(false, resultUN3_4);
		assertEquals(false, resultUN3_5);
		assertEquals(false, resultUN3_6);
		assertEquals(false, resultUN4_1);
		assertEquals(false, resultUN4_2);
		assertEquals(false, resultUN4_3);
		assertEquals(false, resultUN5_1);
		assertEquals(false, resultUN5_2);
		assertEquals(false, resultUN5_3);
		assertEquals(false, resultUN6_1);
		assertEquals(false, resultUN6_2);
		assertEquals(false, resultUN6_3);
		assertEquals(false, resultUN6_4);
		assertEquals(false, resultUN6_5);
		assertEquals(false, resultUN6_6);
		assertEquals(false, resultUN6_7);
		assertEquals(false, resultUN7_1);
		assertEquals(false, resultUN7_2);
		assertEquals(false, resultUN8_1);
		assertEquals(false, resultUN8_2);
		assertEquals(false, resultUN8_3);
		assertEquals(false, resultUN8_4);
		assertEquals(false, resultUN8_5);
		assertEquals(false, resultUN8_6);
		assertEquals(false, resultUN8_7);
		assertEquals(false, resultUN8_8);
		assertEquals(false, resultUN9_1);
		assertEquals(false, resultUN9_2);
		assertEquals(false, resultUN9_3);
		assertEquals(false, resultUN9_4);
		assertEquals(false, resultUN9_5);
		assertEquals(false, resultUN9_6);
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
