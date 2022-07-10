package com.shift.domain.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Data
public class DmMenuDto {


	//フィールド
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "msg")
	private String msg;

	@Column(name = "msg_to_name", unique = true)
	private String msgToName;

	@Column(name = "msg_to_id", unique = true)
	private String msgToId;
}
