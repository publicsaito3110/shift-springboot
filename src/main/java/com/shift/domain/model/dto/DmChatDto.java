package com.shift.domain.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.shift.common.CommonLogic;
import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Data
public class DmChatDto {


	//フィールド
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "msg")
	private String msg;

	@Column(name = "msg_date")
	private String msgDate;

	@Column(name = "read_flg")
	private String readFlg;

	@Column(name = "html_class_send_user", unique = true)
	private String htmlClassSendUser;



	/**
	 * 日付フォーマット変換処理
	 *
	 * <p>msgDateを日付フォーマット(MM/DD hh:mm)に変換する</p>
	 *
	 * @param void
	 * @return String 日付フォーマットに変換した値
	 */
	public String msgDateFormatDate() {

		//フォーマットをMM/DD hh:mmに変換する
		String date = msgDate.substring(5, 16);
		return date.replaceAll("-", "/");
	}


	/**
	 * 改行変換処理
	 *
	 * <p>改行コードが存在するとき、改行タグに変換する</p>
	 *
	 * @param void
	 * @return String 改行タグに変換した値
	 */
	public String msgFormatAfterBreakLine() {

		//改行対応
		return new CommonLogic().changeAfterBreakLine(msg);
	}


	/**
	 * 既読判定処理
	 *
	 * <p>既読済みかどうかをbooleanで返す<br>
	 * ただし、送信したメッセージがログインユーザのときのみ判定する
	 * </p>
	 *
	 * @param void
	 * @return boolean<tr>
	 * true: 送信したメッセージがログインユーザかつ既読済みのとき<br>
	 * false: 送信したメッセージがログインユーザで未読または送信したメッセージがログインユーザでないとき
	 */
	public boolean readFlgFormatNA() {

		//既読済みのとき、trueを返す
		if (Const.HTML_CLASS_DM_MSG_LOGIN_USER.equals(htmlClassSendUser) && Const.DM_READ_FLG.equals(readFlg)) {
			return true;
		}

		//未読のとき、falseを返す
		return false;
	}
}
