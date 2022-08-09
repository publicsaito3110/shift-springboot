package com.shift.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class NewsEditModifyForm {

	@NotBlank(message = "必須入力です")
	private String id;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_NEWS_YMD_ALL, message = "入力値が不正です")
	private String ymd;

	@NotBlank(message = "必須入力です")
	private String title;

	@NotBlank(message = "必須入力です")
	@Pattern(regexp = Const.PATTERN_NEWS_CATEGORY_ALL, message = "入力値が不正です")
	private String category;

	@NotBlank(message = "必須入力です")
	private String content;
}
