package com.shift.form;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class NewsEditForm {

	private String id;
	private String ymd;
	private String title;
	private String category;
	private String content;
}
