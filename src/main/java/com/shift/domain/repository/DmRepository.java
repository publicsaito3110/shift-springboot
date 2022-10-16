package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.DmEntity;

/**
 * @author saito
 *
 */
@Repository
public interface DmRepository extends BaseRepository<DmEntity, Integer>{


	/**
	 * [DB]二者間トークのログインユーザの未読チャット検索処理
	 *
	 * <p>送信ユーザーと受信ユーザとのチャットで送信ユーザが未読のチャットを全て取得する<br>
	 * ただし、未読チャットがないときはEmptyとなる
	 * </p>
	 *
	 * @param sendUser 未読チャットを取得したい送信ユーザのID
	 * @param receiveUser チャット相手のユーザのID
	 * @param readFlg 既読済み(red_flg)の値
	 * @return List<DmEntity><br>
	 * フィールド(List&lt;DmEntity&gt;)<br>
	 * id, sendUser, receiveUser, msg, msg_date, readFlg, html_class_send_user
	 */
	@Query(value = "SELECT d.* FROM dm d WHERE d.send_user = :sendUser AND d.receive_user = :receiveUser AND (d.read_flg != :readFlg OR d.read_flg IS NULL);", nativeQuery = true)
	public List<DmEntity> findBySendUserAndReceiveUserAndReadFlgNot(String sendUser, String receiveUser, String readFlg);
}
