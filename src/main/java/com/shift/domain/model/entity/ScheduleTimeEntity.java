package com.shift.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author saito
 *
 */
@Entity
@Table(name="schedule_time")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleTimeEntity extends BaseEntity {

	//フィールド
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "start_hms")
	private String startHms;

	@Column(name = "end_hms")
	private String endHms;

	@Column(name = "rest_hms")
	private String restHms;
}
