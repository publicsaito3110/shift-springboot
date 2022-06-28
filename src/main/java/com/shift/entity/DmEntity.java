package com.shift.entity;

public class DmEntity {


	//フィールド
	private String sendUser;
	private String receiveUser;
	private String msg;
	private String msgDate;
	private String msgToName;
	private String msgToId;


	//コンストラクタ―
	public DmEntity() {}

	public DmEntity(String sendUser, String receiveUser, String msg, String msgDate, String msgToName, String msgToId) {
		super();
		this.sendUser = sendUser;
		this.receiveUser = receiveUser;
		this.msg = msg;
		this.msgDate = msgDate;
		this.msgToName = msgToName;
		this.msgToId = msgToId;
	}


	//getter, setter
	public String getSendUser() {
		return sendUser;
	}
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	public String getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}
	public String getMsgToName() {
		return msgToName;
	}

	public void setMsgToName(String msgToName) {
		this.msgToName = msgToName;
	}

	public String getMsgToId() {
		return msgToId;
	}

	public void setMsgToId(String msgToId) {
		this.msgToId = msgToId;
	}
}
