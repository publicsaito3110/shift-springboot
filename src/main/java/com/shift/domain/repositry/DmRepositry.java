package com.shift.domain.repositry;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.entity.DmEntity;

@Repository
public interface DmRepositry extends JpaRepository<DmEntity, Integer>{

@Query(value = "SELECT d.id, d.msg_date, d.msg, CASE WHEN d.send_user = :loginUser THEN 'login-user' WHEN d.send_user != :loginUser THEN 'not-login-user' END AS send_user, d.receive_user FROM dm d WHERE d.send_user = :loginUser AND d.receive_user = :receiveUser OR d.send_user = :receiveUser AND d.receive_user = :loginUser ORDER BY d.id", nativeQuery = true)
	public List<DmEntity> selectTalkHistoryByLoginUserReceiveUser(String loginUser, String receiveUser);
}
