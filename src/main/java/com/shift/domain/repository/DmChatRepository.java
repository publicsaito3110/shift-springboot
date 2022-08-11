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
	 * @return List<DmChatDto><br>
	 * フィールド(List&lt;DmChatDto&gt;)<br>
	 * id, msg, msg_date, html_class_send_user
	 */
	@Query(value = "SELECT d.id, d.msg, d.msg_date, CASE WHEN d.send_user = :loginUser THEN 'login-user' WHEN d.send_user != :loginUser THEN 'non-login-user' END AS html_class_send_user FROM dm d WHERE d.send_user = :loginUser AND d.receive_user = :receiveUser OR d.send_user = :receiveUser AND d.receive_user = :loginUser ORDER BY d.id", nativeQuery = true)
	public List<DmChatDto> selectTalkHistoryByLoginUserReceiveUser(String loginUser, String receiveUser);
}
