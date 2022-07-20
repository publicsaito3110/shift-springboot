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
public class UserListDto {


	//フィールド
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "name_kana")
	private String nameKana;

	@Column(name = "gender")
	private String gender;

	@Column(name = "count", unique = true)
	private String count;
}
