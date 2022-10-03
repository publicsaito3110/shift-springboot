package com.shift.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shift.common.CommonLogic;
import com.shift.common.CommonUtil;
import com.shift.common.Const;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author saito
 *
 */
@Entity
@Table(name="user")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

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

	@Column(name = "icon_kbn")
	private String iconKbn;

	@Column(name = "admin_flg")
	private String adminFlg;

	@Column(name = "del_flg")
	private String delFlg;


	//メソッド
	public String getAdminFlgFormatRole() {

		if (CommonUtil.isSuccessValidation(adminFlg, Const.PATTERN_ROLE_USER_ADMIN)) {

			//管理者であるとき、管理者であることを返す
			return Const.ROLE_USER_ADMIN;
		} else {

			//一般ユーザーであるとき、一般ユーザーであることを返す
			return Const.ROLE_USER_GENERAL;
		}
	}


	public String iconKbnFormatHtmlSrc() {

		if (CommonUtil.isSuccessValidation(iconKbn, Const.PATTERN_USER_ICON_KBN_JPG)) {

			//アイコンが登録済みかつ拡張子がjpgのときsrc(.jpg)を返す
			return Const.HTML_SRC_USER_ICON_DIR + id + Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[0];
		} else if (CommonUtil.isSuccessValidation(iconKbn, Const.PATTERN_USER_ICON_KBN_JPEG)) {

			//アイコンが登録済みかつ拡張子がjpegのときsrc(.jpeg)を返す
			return Const.HTML_SRC_USER_ICON_DIR + id + Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[1];
		} else if (CommonUtil.isSuccessValidation(iconKbn, Const.PATTERN_USER_ICON_KBN_PNG)) {

			//アイコンが登録済みかつ拡張子がpngのときsrc(.png)を返す
			return Const.HTML_SRC_USER_ICON_DIR + id + Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[2];
		} else {

			//アイコンが登録済みでないときデフォルトアイコンのパスを返す
			return Const.HTML_SRC_USER_DEFOULT_ICON;
		}
	}


	public boolean isDelFlg() {

		//退職済みでないとき、falseを返す
		if (!CommonUtil.isSuccessValidation(delFlg, Const.PATTERN_USER_DEL_FLG)) {
			return false;
		}

		//退職済みであるとき、trueを返す
		return true;
	}


	public String noteFormatAfterBreakLine() {

		//改行対応
		return new CommonLogic().changeAfterBreakLine(note);
	}
}
