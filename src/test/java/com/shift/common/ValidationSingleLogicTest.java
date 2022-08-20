package com.shift.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.shift.domain.model.bean.ValidationBean;

/**
 * @author saito
 *
 */
@SpringBootTest
class ValidationSingleLogicTest {


	@Test
	@DisplayName("isValidationEroorTest: バリデーションエラー判定処理")
	public void isValidationEroorTest() {

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
		boolean resultN1 = new ValidationSingleLogic(valueN1, regexN1, "エラー").isValidationEroor();
		boolean resultN2 = new ValidationSingleLogic(valueN2, regexN2, "エラー").isValidationEroor();
		boolean resultN3 = new ValidationSingleLogic(valueN3, regexN3, "エラー").isValidationEroor();

		//テスト(正常値)
		boolean resultUN1 = new ValidationSingleLogic(valueUN1, regexUN1, "エラー").isValidationEroor();
		boolean resultUN2 = new ValidationSingleLogic(valueUN2, regexUN2, "エラー").isValidationEroor();
		boolean resultUN3 = new ValidationSingleLogic(valueUN3, regexUN3, "エラー").isValidationEroor();

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(false, resultN1);
		assertEquals(true, resultN2);
		assertEquals(true, resultN3);

		//結果(異常値)
		assertEquals(true, resultUN1);
		assertEquals(true, resultUN2);
		assertEquals(true, resultUN3);
	}


	@Test
	@DisplayName("checkValidationTest: バリデーション判定処理")
	public void checkValidationTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1_1 = "a";
		String regexN1_1 = "a|b";
		String valueN1_2 = "1";
		String regexN1_2 = "1|2";
		String valueN2_1 = "a";
		String regexN2_1 = "1|2";
		String valueN2_2 = "1";
		String regexN2_2 = "a|b";

		//テストケース(異常値)
		String valueUN1_1 = null;
		String regexUN1_1 = "a|b";
		String valueUN1_2 = "a";
		String regexUN1_2 = null;
		String valueUN1_3 = "a";
		String regexUN1_3 = "<";

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		ValidationSingleLogic vSLN1 = new ValidationSingleLogic(valueN1_1, regexN1_1, "エラー");
		vSLN1.checkValidation(valueN1_2, regexN1_2, "エラー");
		boolean resultN1 = vSLN1.isValidationEroor();
		ValidationSingleLogic vSLN2 = new ValidationSingleLogic(valueN2_1, regexN2_1, "エラー");
		vSLN2.checkValidation(valueN2_2, regexN2_2, "エラー");
		boolean resultN2 = vSLN2.isValidationEroor();

		//テスト(異常値)
		ValidationSingleLogic vSLUN1 = new ValidationSingleLogic(valueUN1_1, regexUN1_1, "エラー");
		vSLUN1.checkValidation(valueUN1_2, regexUN1_2, "エラー");
		vSLUN1.checkValidation(valueUN1_3, regexUN1_3, "エラー");
		boolean resultUN1 = vSLUN1.isValidationEroor();

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(false, resultN1);
		assertEquals(true, resultN2);

		//結果(異常値)
		assertEquals(true, resultUN1);
	}


	@Test
	@DisplayName("getValidationResultTest: バリデーション結果取得処理")
	public void getValidationResultTest() {

		//-----------------------------------------------------------------
		// 準備
		//-----------------------------------------------------------------

		//テストケース(正常値)
		String valueN1_1 = "a";
		String regexN1_1 = "a|b";
		String valueN1_2 = "1";
		String regexN1_2 = "1|2";
		String valueN2_1 = "a";
		String regexN2_1 = "1|2";
		String valueN2_2 = "1";
		String regexN2_2 = "a|b";

		//テストケース(異常値)
		String valueUN1_1 = null;
		String regexUN1_1 = "a|b";
		String valueUN1_2 = "a";
		String regexUN1_2 = null;
		String valueUN1_3 = "a";
		String regexUN1_3 = "<";

		//期待値(正常値)
		List<ValidationBean> expectN1 = new ArrayList<>();
		expectN1.add(new ValidationBean("a", true, ""));
		expectN1.add(new ValidationBean("1", true, ""));
		List<ValidationBean> expectN2 = new ArrayList<>();
		expectN2.add(new ValidationBean("a", false, "エラー"));
		expectN2.add(new ValidationBean("1", false, "エラー"));

		//期待値(異常値)
		List<ValidationBean> expectUN1 = new ArrayList<>();
		expectUN1.add(new ValidationBean(null, false, "エラー"));
		expectUN1.add(new ValidationBean("a", false, "エラー"));
		expectUN1.add(new ValidationBean("a", false, "エラー"));

		//-----------------------------------------------------------------
		// 実行
		//-----------------------------------------------------------------

		//テスト(正常値)
		ValidationSingleLogic vSLN1 = new ValidationSingleLogic(valueN1_1, regexN1_1, "エラー");
		vSLN1.checkValidation(valueN1_2, regexN1_2, "エラー");
		List<ValidationBean> resultN1 = vSLN1.getValidationResult();
		ValidationSingleLogic vSLN2 = new ValidationSingleLogic(valueN2_1, regexN2_1, "エラー");
		vSLN2.checkValidation(valueN2_2, regexN2_2, "エラー");
		List<ValidationBean> resultN2 = vSLN2.getValidationResult();

		//テスト(異常値)
		ValidationSingleLogic vSLUN1 = new ValidationSingleLogic(valueUN1_1, regexUN1_1, "エラー");
		vSLUN1.checkValidation(valueUN1_2, regexUN1_2, "エラー");
		vSLUN1.checkValidation(valueUN1_3, regexUN1_3, "エラー");
		List<ValidationBean> resultUN1 = vSLUN1.getValidationResult();

		//-----------------------------------------------------------------
		// 検証
		//-----------------------------------------------------------------

		//結果(正常値)
		assertEquals(expectN1, resultN1);
		assertEquals(expectN2, resultN2);

		//結果(異常値)
		assertEquals(expectUN1, resultUN1);
	}
}
