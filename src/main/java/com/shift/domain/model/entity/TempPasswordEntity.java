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
@Table(name="temp_password")
@Data
@EqualsAndHashCode(callSuper = true)
public class TempPasswordEntity extends BaseEntity {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user")
	private String user;

	@Column(name = "url_param")
	private String urlParam;

	@Column(name = "auth_code")
	private String authCode;

	@Column(name = "insert_date")
	private String insertDate;
}
