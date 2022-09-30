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
public class DmMenuDto {


	//フィールド
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "msg")
	private String msg;

	@Column(name = "msg_to_name", unique = true)
	private String msgToName;

	@Column(name = "msg_to_id", unique = true)
	private String msgToId;

	@Column(name = "icon_flg", unique = true)
	private String iconFlg;


	//メソッド
	public String iconFlgFormatHtmlSrc() {

		if (CommonUtil.isSuccessValidation(iconFlg, Const.PATTERN_USER_ICON_FLG_JPG)) {

			//アイコンが登録済みかつ拡張子がjpgのときsrc(.jpg)を返す
			return Const.HTML_SRC_USER_ICON_FOLDER_PATH + id + ".jpg";
		} else if (CommonUtil.isSuccessValidation(iconFlg, Const.PATTERN_USER_ICON_FLG_JPEG)) {

			//アイコンが登録済みかつ拡張子がjpegのときsrc(.jpeg)を返す
			return Const.HTML_SRC_USER_ICON_FOLDER_PATH + id + ".jpeg";
		} else if (CommonUtil.isSuccessValidation(iconFlg, Const.PATTERN_USER_ICON_FLG_PNG)) {

			//アイコンが登録済みかつ拡張子がpngのときsrc(.png)を返す
			return Const.HTML_SRC_USER_ICON_FOLDER_PATH + id + ".png";
		} else {

			//アイコンが登録済みでないときデフォルトアイコンのパスを返す
			return Const.HTML_SRC_USER_DEFOULT_ICON;
		}
	}
}
