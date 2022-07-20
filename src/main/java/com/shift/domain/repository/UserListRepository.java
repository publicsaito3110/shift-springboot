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

	@Query(value = "SELECT u.id, u.name, u.name_kana, u.gender, (SELECT COUNT(s.id) FROM user s WHERE s.id LIKE :keyword OR s.name LIKE :keyword OR s.name_kana LIKE :keyword ) AS COUNT FROM user u WHERE u.id LIKE :keyword OR u.name LIKE :keyword OR u.name_kana LIKE :keyword ORDER BY u.id LIMIT :limit OFFSET :offset", nativeQuery = true)
	List<UserListDto> selectUserByKeyWordLimitOffset(String keyword, int limit, int offset);

	@Query(value = "SELECT u.id, u.name, u.name_kana, u.gender, (SELECT COUNT(s.id) FROM user s WHERE s.id = :userId) AS COUNT FROM user u WHERE u.id = :userId ORDER BY u.id", nativeQuery = true)
	List<UserListDto> selectUserByUserId(String userId);
}
