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

	@Column(name = "icon_kbn")
	private String iconKbn;


	//メソッド
	public String iconKbnFormatHtmlSrc() {

		if (CommonUtil.isSuccessValidation(iconKbn, Const.PATTERN_USER_ICON_KBN_JPG)) {

			//アイコンが登録済みかつ拡張子がjpgのときsrc(.jpg)を返す
			return Const.HTML_SRC_USER_ICON_DIR + msgToId + Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[0];
		} else if (CommonUtil.isSuccessValidation(iconKbn, Const.PATTERN_USER_ICON_KBN_JPEG)) {

			//アイコンが登録済みかつ拡張子がjpegのときsrc(.jpeg)を返す
			return Const.HTML_SRC_USER_ICON_DIR + msgToId + Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[1];
		} else if (CommonUtil.isSuccessValidation(iconKbn, Const.PATTERN_USER_ICON_KBN_PNG)) {

			//アイコンが登録済みかつ拡張子がpngのときsrc(.png)を返す
			return Const.HTML_SRC_USER_ICON_DIR + msgToId + Const.USER_ICON_ALLOW_FILE_EXTENSION_ARRAY[2];
		} else {

			//アイコンが登録済みでないときデフォルトアイコンのパスを返す
			return Const.HTML_SRC_USER_DEFOULT_ICON;
		}
	}
}
