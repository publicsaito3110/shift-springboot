package com.shift.domain.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.dto.DmUnreadCountDto;

/**
 * @author saito
 *
 */
@Repository
public interface DmUnreadCountRepository extends BaseRepository<DmUnreadCountDto, Integer>{

	/**
	 * [DB]未読メッセージ数検索処理
	 *
	 * <p>ユーザーの未読メッセージ数を全て取得する<br>
	 * ただし、未読メッセージがない, 存在しないユーザID, チャットがないときはnullにはならないが、unreadCountは0となる
	 * </p>
	 *
	 * @param userId 未読メッセージを取得したいユーザのID
	 * @param readFlg 既読済みの値
	 * @return DmUnreadCountDto<br>
	 * フィールド(DmUnreadCountDto)<br>
	 * id, unreadCount
	 */
	@Query(value = "SELECT d.id, COUNT(d.id) AS unread_count FROM dm d WHERE d.receive_user = :userId AND (d.read_flg != :readFlg OR d.read_flg IS NULL)", nativeQuery = true)
	public DmUnreadCountDto selectUnreadMsgCountByUserIdReadFlg(String userId, String readFlg);
}
