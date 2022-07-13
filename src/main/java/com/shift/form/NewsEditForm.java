package com.shift.form;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class NewsEditForm {

	private String ymd;
	private String beforeTitle;
	private String beforeCategory;
	private String beforeContent;
	private String afterTitle;
	private String afterCategory;
	private String afterContent;
}
