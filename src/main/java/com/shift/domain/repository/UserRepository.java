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

	public UserEntity findByIdAndPassword(String id, String password);

	@Query(value = "SELECT u.* FROM user u WHERE u.id != :loginUser AND (u.del_flg != :delFlg OR u.del_flg IS NULL) AND (u.id LIKE :keyword OR u.name LIKE :keyword OR u.name_kana LIKE :keyword) ORDER BY u.id", nativeQuery = true)
	public List<UserEntity> selectUserByKeywordNotUserIdDelFlg(String loginUser, String delFlg, String keyword);
}
