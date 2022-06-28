package com.shift.domain.repositry;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.shift.entity.DmEntity;

@Repository
public class DmRepositry {

	@Autowired
	JdbcTemplate jdbcTemplate;


	class ScheduleRowMapper implements RowMapper<DmEntity> {

		@Override
		public DmEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DmEntity dmEntity = new DmEntity();
			dmEntity.setMsg(rs.getString("msg"));
			dmEntity.setSendUser(rs.getString("send_user"));
			dmEntity.setReceiveUser(rs.getString("receive_user"));
			dmEntity.setMsgDate(rs.getString("msg_date"));
			dmEntity.setMsgToName(rs.getString("msg_to_name"));
			dmEntity.setMsgToId(rs.getString("msg_to_id"));

			return dmEntity;
		}
	}


	public List<DmEntity> selectDmTalkHistoryByYm(String id) {

		//SQL
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT a.* ");
		sqlSb.append(" FROM ( ");
		sqlSb.append(" SELECT DISTINCT d.msg_date, d.msg, d.send_user, d.receive_user ");
		sqlSb.append(" , CASE WHEN d.send_user = ? THEN s.name WHEN d.receive_user = ? THEN u.name END AS msg_to_name ");
		sqlSb.append(" , CASE WHEN d.send_user = ? THEN s.id WHEN d.receive_user = ? THEN u.id END AS msg_to_id ");
		sqlSb.append(" FROM (DM d INNER JOIN USER u ON u.id = d.send_user) INNER JOIN USER s ON s.id = d.receive_user  ");
		sqlSb.append(" WHERE d.send_user = ? OR d.receive_user = ? ");
		sqlSb.append(" ORDER BY d.id DESC ");
		sqlSb.append(" ) a ");
		sqlSb.append(" GROUP BY a.MSG_TO_ID ");
		sqlSb.append(" ; ");

		return jdbcTemplate.query(sqlSb.toString(), new Object[] {id, id, id, id, id, id}, new ScheduleRowMapper());
	}
}
