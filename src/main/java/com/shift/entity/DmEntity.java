package com.shift.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "dm")
@Data
public class DmEntity {


	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "send_user")
	private String sendUser;

	@Column(name = "receive_user")
	private String receiveUser;

	@Column(name = "msg")
	private String msg;

	@Column(name = "msg_date")
	private String msgDate;


	//メソッド
	public String getMsgDateFormatDate() {

		//フォーマットをMM/DD hh:mmに変換する
		String date = this.msgDate.substring(5, 16);
		return date.replaceAll("-", "/");
	}

	public String getMsgAfterBreakLine() {

		//改行対応
		return this.msg.replaceAll("\n", "<br>");
	}
}
