package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.dto.DmChatDto;

/**
 * @author saito
 *
 */
@Repository
public interface DmChatRepository extends BaseRepository<DmChatDto, Integer>{

	/**
	 * [DB]二者間トーク検索処理
	 *
	 * <p>ログインユーザーと相手の全てのチャット履歴を取得する<br>
	 * ただし、チャットがないときはEmptyとなる
	 * </p>
	 *
	 * @param loginUser 現在ログインしているユーザのID
	 * @param receiveUser 相手のユーザのID
	 * @param htmlClassLoginUser メッセージがログインユーザのとき格納される
	 * @param htmlClassNonLoginUser メッセージが相手ユーザのとき格納される
	 * @return List<DmChatDto><br>
	 * フィールド(List&lt;DmChatDto&gt;)<br>
	 * id, msg, msg_date, readFlg, html_class_send_user
	 */
	@Query(value = "SELECT d.id, d.msg, d.msg_date, read_flg, CASE WHEN d.send_user = :loginUser THEN :htmlClassLoginUser WHEN d.send_user != :loginUser THEN :htmlClassNonLoginUser END AS html_class_send_user FROM dm d WHERE (d.send_user = :loginUser AND d.receive_user = :receiveUser) OR (d.send_user = :receiveUser AND d.receive_user = :loginUser) ORDER BY d.id LIMIT :limit OFFSET :offset", nativeQuery = true)
	public List<DmChatDto> selectTalkHistoryByLoginUserReceiveUser(String loginUser, String receiveUser, String htmlClassLoginUser, String htmlClassNonLoginUser, int limit, int offset);
}
