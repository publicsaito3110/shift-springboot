package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.dto.DmMenuDto;

/**
 * @author saito
 *
 */
@Repository
public interface DmMenuRepository extends BaseRepository<DmMenuDto, Integer>{

	/**
	 * [DB]最終トーク検索処理
	 *
	 * <p>ユーザーが送受信した最後のチャットをユーザーごとに取得する<br>
	 * 取得する順番は送信履歴が新しいほうから順に降順となる<br>
	 * ただし、一度もチャットを送受信していないときはEmptyとなる
	 * </p>
	 *
	 * @param userId 最終履歴を取得したいユーザのID
	 * @param readFlg 既読済み(red_flg)の値
	 * @return List<DmChatDto><br>
	 * フィールド(List&lt;DmMenuDto&gt;)<br>
	 * id, msg, msg_to_name, msg_to_id, icon_kbn, unreadCount
	 */
	@Query(value = "SELECT a.id, a.msg, a.msg_to_name, a.msg_to_id, a.icon_kbn, a.unread_count FROM (SELECT RANK() OVER (PARTITION BY x.msg_to_id ORDER BY x.id DESC) AS rk, x.*, (SELECT COUNT(m.id) FROM dm m WHERE m.receive_user = :userId AND m.send_user = x.msg_to_id AND (m.read_flg != :readFlg OR m.read_flg IS NULL)) AS unread_count FROM (SELECT d.id, d.msg, CASE WHEN d.send_user = :userId THEN s.name WHEN d.receive_user = :userId THEN u.name END AS msg_to_name, CASE WHEN d.send_user = :userId THEN s.id WHEN d.receive_user = :userId THEN u.id END AS msg_to_id, CASE WHEN d.send_user = :userId THEN s.icon_kbn WHEN d.receive_user = :userId THEN u.icon_kbn END AS icon_kbn FROM (dm d INNER JOIN user u ON u.id = d.send_user) INNER JOIN user s ON s.id = d.receive_user WHERE (d.send_user = :userId OR d.receive_user = :userId) ORDER BY d.id DESC) x) a WHERE a.rk = 1", nativeQuery = true)
	public List<DmMenuDto> selectDmTalkHistoryByLoginUser(String userId, String readFlg);
}
