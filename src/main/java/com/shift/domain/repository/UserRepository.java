package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.UserEntity;

/**
 * @author saito
 *
 */
@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {

	/**
	 * [DB]キーワードユーザー検索処理
	 *
	 * <p>キーワードから一致するユーザを取得する<br>
	 * ただし、退職フラグのあるユーザは除外される<br>
	 * 該当ユーザーがいない場合はEmptyとなる
	 * </p>
	 *
	 * @param loginUser ログインしているユーザID
	 * @param delFlg 退職フラグの値
	 * @param keyword 検索絞り込みのためのキーワード<br>
	 * ただし、LIKE句であるため、"%keyword%" でなければならない
	 * @return List<UserEntity><br>
	 * フィールド(List&lt;UserEntity&gt;)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, icon_kbn, admin_flg, del_flg
	 */
	@Query(value = "SELECT u.* FROM user u WHERE u.id != :loginUser AND (u.del_flg != :delFlg OR u.del_flg IS NULL) AND (u.id LIKE :keyword OR u.name LIKE :keyword OR u.name_kana LIKE :keyword) ORDER BY u.id", nativeQuery = true)
	public List<UserEntity> selectUserByKeywordNotUserIdDelFlg(String loginUser, String delFlg, String keyword);


	/**
	 * [DB]未退職ユーザー検索処理
	 *
	 * <p>未退職ユーザのみを取得する<br>
	 * ただし、退職フラグのあるユーザは除外される<br>
	 * 該当ユーザーがいない場合はEmptyとなる
	 * </p>
	 *
	 * @param delFlg 退職フラグの値
	 * @return List<UserEntity><br>
	 * フィールド(List&lt;UserEntity&gt;)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, icon_kbn, admin_flg, del_flg
	 */
	public List<UserEntity> findByDelFlgNotOrDelFlgIsNull(String delFlg);


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>emailと一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はnullとなる
	 * </p>
	 *
	 * @param email 検索したいメールアドレス
	 * @return UserEntity<br>
	 * フィールド(UserEntity)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, icon_kbn, admin_flg, del_flg
	 */
	public UserEntity findByEmail(String email);


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>idまたはemailのどちらかと一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はEmptyとなる
	 * </p>
	 *
	 * @param id 検索したいID
	 * @param email 検索したいメールアドレス
	 * @return List<UserEntity> <br>
	 * フィールド(List&lt;UserEntity&gt;)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, icon_kbn, admin_flg, del_flg
	 */
	public List<UserEntity> findByIdOrEmail(String id, String email);
}
