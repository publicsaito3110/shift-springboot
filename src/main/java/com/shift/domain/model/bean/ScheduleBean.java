package com.shift.domain.model.bean;

import lombok.Data;

/**
 * @author saito
 *
 */
@Data
public class ScheduleBean {


	//フィールド
	private String ymd;
	private String user1;
	private String user2;
	private String user3;
	private String memo1;
	private String memo2;
	private String memo3;

	private String day;
	private String htmlClass;
}
