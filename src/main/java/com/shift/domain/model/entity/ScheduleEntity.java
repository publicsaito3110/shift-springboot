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
@Table(name="schedule")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleEntity extends BaseEntity {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "ymd")
	private String ymd;

	@Column(name = "user")
	private String user;

	@Column(name = "schedule")
	private String schedule;


	//メソッド
	public String getFormatDay() {

		//day(DD)に変換し、返す
		return ymd.substring(6, 8);
	}
}
