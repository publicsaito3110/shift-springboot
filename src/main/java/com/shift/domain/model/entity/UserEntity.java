package com.shift.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shift.common.CommonUtil;
import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Table(name="user")
@Data
public class UserEntity {

	//フィールド
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "nameKana")
	private String nameKana;

	@Column(name = "gender")
	private String gender;

	@Column(name = "password")
	private String password;

	@Column(name = "address")
	private String address;

	@Column(name = "tel")
	private String tel;

	@Column(name = "email")
	private String email;

	@Column(name = "note")
	private String note;

	@Column(name = "admin_flg")
	private String adminFlg;

	@Column(name = "del_flg")
	private String delFlg;


	//メソッド
	public String getAdminFlgFormatRole() {

		//管理者であるとき
		if (CommonUtil.isSuccessValidation(this.adminFlg, Const.PATTERN_ROLE_USER_ADMIN)) {
			return Const.ROLE_USER_ADMIN;
		}

		//一般ユーザーであるとき
		if (CommonUtil.isSuccessValidation(this.adminFlg, Const.PATTERN_ROLE_USER_GENERAL)) {
			return Const.ROLE_USER_GENERAL;
		}

		return "";
	}
}
