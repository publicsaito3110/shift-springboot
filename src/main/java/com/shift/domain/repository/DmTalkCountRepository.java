package com.shift.domain.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.dto.DmTalkCountDto;

/**
 * @author saito
 *
 */
@Repository
public interface DmTalkCountRepository extends BaseRepository<DmTalkCountDto, Integer>{


	/**
	 * [DB]二者間トーク総数検索処理
	 *
	 * <p>送信ユーザーと受信ユーザとのチャットで送信ユーザが未読のチャットを全て取得する<br>
	 * ただし、未読チャットがないときはnullとなる
	 * </p>
	 *
	 * @param sendUser 未読チャットを取得したい送信ユーザのID
	 * @param receiveUser チャット相手のユーザのID
	 * @return List<DmTalkCountDto><br>
	 * フィールド(List&lt;DmTalkCountDto&gt;)<br>
	 * talk_count
	 */
	@Query(value = "SELECT COUNT(d.id) AS talk_count FROM dm d WHERE (d.send_user = :receiveUser AND d.receive_user = :sendUser) OR (d.send_user = :sendUser AND d.receive_user = :receiveUser)", nativeQuery = true)
	public DmTalkCountDto selectTalkCount(String receiveUser, String sendUser);
}
