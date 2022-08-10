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

	@Query(value = "SELECT a.* FROM (SELECT DISTINCT d.id, d.msg, CASE WHEN d.send_user = :loginUser THEN s.name WHEN d.receive_user = :loginUser THEN u.name END AS msg_to_name, CASE WHEN d.send_user = :loginUser THEN s.id WHEN d.receive_user = :loginUser THEN u.id END AS msg_to_id FROM (dm d INNER JOIN USER u ON u.id = d.send_user) INNER JOIN USER s ON s.id = d.receive_user WHERE d.send_user = :loginUser OR d.receive_user = :loginUser ORDER BY d.id DESC) a GROUP BY a.msg_to_id", nativeQuery = true)
	public List<DmMenuDto> selectDmTalkHistoryByLoginUser(String loginUser);
}
