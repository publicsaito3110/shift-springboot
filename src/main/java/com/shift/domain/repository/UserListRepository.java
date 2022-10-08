package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.dto.UserListDto;

/**
 * @author saito
 *
 */
@Repository
public interface UserListRepository extends BaseRepository<UserListDto, String> {

	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>ユーザ一覧を取得する<br>
	 * ただし、keywordが""のときはlimit及びoffsetの範囲内で全件取得する<br>
	 * 管理者ユーザのみの処理
	 * </p>
	 *
	 * @param keyword 検索絞り込みのためのキーワード<br>
	 * ただし、LIKE句であるため、"%keyword%" でなければならない
	 * @param limit 表示するユーザーの上限の数
	 * @param offset 何件目から表示するかの指定
	 * @return List<UserListDto><br>
	 * フィールド(List&lt;UserListDto&gt;)<br>
	 * id, name, nameKana, gender, count
	 */
	@Query(value = "SELECT u.id, u.name, u.name_kana, u.gender, (SELECT COUNT(s.id) FROM user s WHERE s.id LIKE :keyword OR s.name LIKE :keyword OR s.name_kana LIKE :keyword ) AS COUNT FROM user u WHERE u.id LIKE :keyword OR u.name LIKE :keyword OR u.name_kana LIKE :keyword ORDER BY u.id LIMIT :limit OFFSET :offset", nativeQuery = true)
	public List<UserListDto> selectUserByKeyWordLimitOffset(String keyword, int limit, int offset);


	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>全てのユーザ一覧を取得する</p>
	 *
	 * @param void
	 * @return List<UserListDto><br>
	 * フィールド(List&lt;UserListDto&gt;)<br>
	 * id, name, nameKana, gender, count
	 */
	@Query(value = "SELECT u.id, u.name, u.name_kana, u.gender, (SELECT COUNT(s.id) FROM user s) AS COUNT FROM user u ORDER BY u.id", nativeQuery = true)
	public List<UserListDto> selectUserALL();
}
