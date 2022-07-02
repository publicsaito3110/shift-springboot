package com.shift.domain.repositry;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.dto.DmMenueDto;

@Repository
public interface DmMenueRepositry extends JpaRepository<DmMenueDto, Integer>{

	@Query(value = "SELECT a.* FROM (SELECT DISTINCT d.id, d.msg_date, d.msg, d.send_user, d.receive_user, CASE WHEN d.send_user = :loginUser THEN s.name WHEN d.receive_user = :loginUser THEN u.name END AS msg_to_name, CASE WHEN d.send_user = :loginUser THEN s.id WHEN d.receive_user = :loginUser THEN u.id END AS msg_to_id FROM (dm d INNER JOIN USER u ON u.id = d.send_user) INNER JOIN USER s ON s.id = d.receive_user WHERE d.send_user = :loginUser OR d.receive_user = :loginUser ORDER BY d.id DESC) a GROUP BY a.msg_to_id", nativeQuery = true)
	List<DmMenueDto> selectDmTalkHistoryByLoginUser(String loginUser);
}
