package com.shift.entity;

public class ScheduleEntity {


	//フィールド
	private String ymd;
	private String user1;
	private String user2;
	private String user3;
	private String memo1;
	private String memo2;
	private String memo3;


	//コンストラクタ―
	public ScheduleEntity() {}

	public ScheduleEntity(String ymd, String user1, String user2, String user3, String memo1, String memo2, String memo3) {
		super();
		this.ymd = ymd;
		this.user1 = user1;
		this.user2 = user2;
		this.user3 = user3;
		this.memo1 = memo1;
		this.memo2 = memo2;
		this.memo3 = memo3;
	}


	//getter, setter
	public String getYmd() {
		return ymd;
	}
	public void setYmd(String ymd) {
		this.ymd = ymd;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	public String getUser3() {
		return user3;
	}
	public void setUser3(String user3) {
		this.user3 = user3;
	}
	public String getMemo1() {
		return memo1;
	}
	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}
	public String getMemo2() {
		return memo2;
	}
	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}
	public String getMemo3() {
		return memo3;
	}
	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}
	public String getFormatDay() {
		return ymd.substring(6, 8);
	}
}
