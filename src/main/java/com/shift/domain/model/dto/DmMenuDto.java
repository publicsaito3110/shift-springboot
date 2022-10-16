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

	@Column(name = "unread_count", unique = true)
	private String unreadCount;



	/**
	 * ユーザアイコン取得処理
	 *
	 * <p>メッセージ送信相手のユーザのアイコンのパスを返す<br>
	 * ただし、アイコンが登録済みでないときデフォルトアイコンのパスを返す
	 * </p>
	 *
	 * @param void
	 * @return String 送信相手のアイコンのパス
	 */
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


	/**
	 * 未読メッセージ判定処理
	 *
	 * <p>未読メッセージが存在するとき、trueを返す<br>
	 * ただし、未読メッセージが存在しないとき、falseを返す
	 * </p>
	 *
	 * @param void
	 * @return boolean<br>
	 * true: 未読メッセージが存在するとき<br>
	 * false: 未読メッセージが存在しないとき
	 */
	public boolean  unreadCountFormatNA() {

		//未読メッセージが0件のとき、falseを返す
		if ("0".equals(unreadCount)) {
			return false;
		}

		//未読メッセージが1件以上のとき、trueを返す
		return true;
	}
}
