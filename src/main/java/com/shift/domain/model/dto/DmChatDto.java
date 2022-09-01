package com.shift.domain.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.shift.common.CommonLogic;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Data
public class DmChatDto {


	//フィールド
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "msg")
	private String msg;

	@Column(name = "msg_date")
	private String msgDate;

	@Column(name = "html_class_send_user", unique = true)
	private String htmlClassSendUser;


	//メソッド
	public String getMsgDateFormatDate() {

		//フォーマットをMM/DD hh:mmに変換する
		String date = this.msgDate.substring(5, 16);
		return date.replaceAll("-", "/");
	}

	public String getMsgAfterBreakLine() {

		//改行対応
		return new CommonLogic().changeAfterBreakLine(this.msg);
	}
}
