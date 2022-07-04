package com.shift.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "dm")
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

	@Column(name = "msg_to_name", unique = true)
	private String msgToName;

	@Column(name = "msg_to_id", unique = true)
	private String msgToId;
}
