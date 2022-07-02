package com.shift.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DmMenueDto {


	//フィールド
	@Id
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

	@Column(name = "msg_to_name")
	private String msgToName;

	@Column(name = "msg_to_id")
	private String msgToId;
}
