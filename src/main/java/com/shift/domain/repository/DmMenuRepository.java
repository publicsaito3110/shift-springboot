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
	 * <p>ログインユーザーが送受信した最後のチャットをユーザーごとに取得する<br>
	 * 取得する順番は送信履歴が新しいほうから順に降順となる<br>
	 * ただし、一度もチャットを送受信していないときはEmptyとなる
	 * </p>
	 *
	 * @param loginUser 現在ログインしているユーザのID
	 * @return List<DmChatDto><br>
	 * フィールド(List&lt;DmMenuDto&gt;)<br>
	 * id, msg, msg_to_name, msg_to_id
	 */
	@Query(value = "SELECT a.* FROM (SELECT DISTINCT d.id, d.msg, CASE WHEN d.send_user = :loginUser THEN s.name WHEN d.receive_user = :loginUser THEN u.name END AS msg_to_name, CASE WHEN d.send_user = :loginUser THEN s.id WHEN d.receive_user = :loginUser THEN u.id END AS msg_to_id, u.icon_flg FROM (dm d INNER JOIN USER u ON u.id = d.send_user) INNER JOIN USER s ON s.id = d.receive_user WHERE d.send_user = :loginUser OR d.receive_user = :loginUser ORDER BY d.id DESC) a GROUP BY a.msg_to_id", nativeQuery = true)
	public List<DmMenuDto> selectDmTalkHistoryByLoginUser(String loginUser);
}
