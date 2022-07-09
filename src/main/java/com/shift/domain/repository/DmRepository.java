package com.shift.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.DmEntity;

/**
 * @author saito
 *
 */
@Repository
public interface DmRepository extends BaseRepository<DmEntity, Integer>{

@Query(value = "SELECT d.id, d.msg_date, d.msg, CASE WHEN d.send_user = :loginUser THEN 'login-user' WHEN d.send_user != :loginUser THEN 'not-login-user' END AS send_user, d.receive_user FROM dm d WHERE d.send_user = :loginUser AND d.receive_user = :receiveUser OR d.send_user = :receiveUser AND d.receive_user = :loginUser ORDER BY d.id", nativeQuery = true)
	public List<DmEntity> selectTalkHistoryByLoginUserReceiveUser(String loginUser, String receiveUser);
}
