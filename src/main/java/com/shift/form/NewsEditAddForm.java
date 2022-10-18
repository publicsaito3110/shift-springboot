package com.shift.form;

import java.time.LocalDate;

import javax.validation.constraints.Pattern;

import com.shift.common.CommonLogic;
import com.shift.common.Const;
import com.shift.common.annotation.NotNgChar;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class NewsEditAddForm {

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_NEWS_TITLE_INPUT, message = "入力値が不正です")
	private String title;

	@Pattern(regexp = Const.PATTERN_NEWS_CATEGORY_INPUT, message = "入力値が不正です")
	private String category;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_NEWS_CONTENT_INPUT, message = "入力値が不正です")
	private String content;

	@Pattern(regexp = Const.PATTERN_NEWS_UNIQUE_DATE_INPUT, message = "入力値が不正です")
	private String date;



	/**
	 * バリデーションエラー判定(相関チェック)<br>
	 *
	 * <p>(相関チェックをする値)<br>
	 * date: 日付が新規追加可能日付かどうかの判定
	 * </p>
	 * @param void
	 * @return boolean<br>
	 * true: 相関チェックエラーとき<br>
	 * false: 相関チェックエラーでないとき
	 */
	public boolean isErrorValidRelated() {

		//入力値のdateをLocalDateで取得
		String inputDateYmd = date.replaceAll("-", "");
		CommonLogic commonLogic = new CommonLogic();
		LocalDate inputDateLd = commonLogic.getLocalDateByYmd(inputDateYmd);

		//現在日から新規追加可能日付をLocalDateで取得
		LocalDate nowLd = LocalDate.now();
		LocalDate minRecordableMonthLd = nowLd.minusDays(1);
		LocalDate maxRecordableMonthLd = nowLd.plusMonths(Const.NEWS_RECORDABLE_MAX_MONTH);

		//入力値が新規追加可能日付の範囲内のとき、falseを返す
		if (inputDateLd.isAfter(minRecordableMonthLd) && inputDateLd.isBefore(maxRecordableMonthLd)) {
			return false;
		}

		//相関チェックエラーのとき、trueを返す
		return true;
	}
}
