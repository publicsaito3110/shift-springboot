package com.shift.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.shift.common.Const;
import com.shift.common.annotation.NotNgChar;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class NewsEditModifyForm {

	@NotBlank(message = "必須入力です")
	private String id;

	@Pattern(regexp = Const.PATTERN_NEWS_YMD_INPUT, message = "入力値が不正です")
	private String ymd;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_NEWS_TITLE_INPUT, message = "20文字以内のみ有効です")
	private String title;

	@Pattern(regexp = Const.PATTERN_NEWS_CATEGORY_INPUT, message = "入力値が不正です")
	private String category;

	@NotNgChar(message = "入力禁止文字が含まれています")
	@Pattern(regexp = Const.PATTERN_NEWS_CONTENT_INPUT, message = "200文字以内のみ有効です")
	private String content;
}
