package com.shift.domain.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.shift.common.Const;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Data
public class DmTalkCountDto {


	@Id
	@Column(name = "talk_count")
	private Integer talkCount;



	/**
	 * チャット件目計算処理
	 *
	 * <p>チャット取得制限から最終チャット件目が最後になるような最初のチャット件目を計算する<br>
	 * ただし、チャット取得制限<br>
	 * ex) チャット総件目数が40かつチャット上限が20 -> 20～40になるように20が返される
	 * </p>
	 *
	 * @param void
	 * @return int チャット件目
	 */
	public int calcDmFirstChatOffset() {

		//DMのチャット取得制限の範囲内のとき
		if (talkCount <= Const.DM_CHAT_LIMIT) {
			return 0;
		}

		return talkCount - Const.DM_CHAT_LIMIT;
	}
}
