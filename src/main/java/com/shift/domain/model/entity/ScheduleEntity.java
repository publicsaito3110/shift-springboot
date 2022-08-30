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
@Table(name="schedule")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleEntity extends BaseEntity {

	//フィールド
	@Id
	@Column(name = "ymd")
	private String ymd;

	@Column(name = "user1")
	private String user1;

	@Column(name = "user2")
	private String user2;

	@Column(name = "user3")
	private String user3;

	@Column(name = "memo1")
	private String memo1;

	@Column(name = "memo2")
	private String memo2;

	@Column(name = "memo3")
	private String memo3;


	//メソッド
	public String getFormatDay() {
		return ymd.substring(6, 8);
	}
}
