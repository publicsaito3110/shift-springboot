package com.shift.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.shift.common.Const;
import com.shift.common.annotation.NotNgChar;
import com.shift.common.annotation.NotOnlyWhitespace;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class NewsEditModifyForm {

	@NotBlank(message = "入力値が不正です")
	private String id;

	@NotBlank(message = "入力値が不正です")
	@Pattern(regexp = Const.PATTERN_NEWS_YMD_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_NEWS_YMD_LENGTH_MIN_INPUT, max = Const.PATTERN_NEWS_YMD_LENGTH_MAX_INPUT, message = "200文字以内で入力してください")
	private String ymd;

	@NotBlank(message = "必須入力です")
	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_NEWS_TITLE_LENGTH_MIN_INPUT, max = Const.PATTERN_NEWS_TITLE_LENGTH_MAX_INPUT, message = "20文字以内で入力してください")
	private String title;

	@NotBlank(message = "入力値が不正です")
	@Pattern(regexp = Const.PATTERN_NEWS_CATEGORY_INPUT, message = "入力値が不正です")
	@Length(min = Const.PATTERN_NEWS_CATEGORY_LENGTH_MIN_INPUT, max = Const.PATTERN_NEWS_CATEGORY_LENGTH_MAX_INPUT, message = "入力値が不正です")
	private String category;

	@NotBlank(message = "必須入力です")
	@NotNgChar(message = "入力禁止文字が含まれています")
	@NotOnlyWhitespace(message = "空白文字のみは禁止されています")
	@Length(min = Const.PATTERN_NEWS_CONTENT_LENGTH_MIN_INPUT, max = Const.PATTERN_NEWS_CONTENT_LENGTH_MAX_INPUT, message = "200文字以内で入力してください")
	private String content;
}
