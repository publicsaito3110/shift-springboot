package com.shift.domain.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.shift.common.CommonUtil;
import com.shift.common.Const;

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


	//メソッド
	public String genderFormatMF() {

		//genderが男性のパターンと一致しているとき"男性"を返す
		if (CommonUtil.isSuccessValidation(gender, Const.PATTERN_USER_GENDER_1_MAN)) {
			return Const.USER_GENDER_1_MAN;
		}

		//genderが女性のパターンと一致しているとき"女性"を返す
		if (CommonUtil.isSuccessValidation(gender, Const.PATTERN_USER_GENDER_2_WOMEN)) {
			return Const.USER_GENDER_2_WOMEN;
		}

		return "";
	}
}
