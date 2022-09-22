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
public class ScheduleUserDto {


	//フィールド
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "user")
	private String user;

	@Column(name = "schedule1", unique = true)
	private String schedule1;

	@Column(name = "schedule2", unique = true)
	private String schedule2;

	@Column(name = "schedule3", unique = true)
	private String schedule3;

	@Column(name = "schedule4", unique = true)
	private String schedule4;

	@Column(name = "schedule5", unique = true)
	private String schedule5;

	@Column(name = "schedule6", unique = true)
	private String schedule6;

	@Column(name = "schedule7", unique = true)
	private String schedule7;
}
