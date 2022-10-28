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
public class ScheduleWorkCountYearDto {


	//フィールド
	@Id
	@Column(name = "user_id", unique = true)
	private String userId;

	@Column(name = "user_name", unique = true)
	private String userName;

	@Column(name = "work_count", unique = true)
	private int workCount;
}
