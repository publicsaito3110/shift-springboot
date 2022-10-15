package com.shift.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author saito
 *
 */
@Entity
@Table(name = "dm")
@Data
@EqualsAndHashCode(callSuper = true)
public class DmEntity extends BaseEntity {

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

	@Column(name = "read_flg")
	private String readFlg;
}
