package com.shift.domain.repositry;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.shift.entity.ScheduleEntity;

@Repository
public class ScheduleRepositry {

	@Autowired
	JdbcTemplate jdbcTemplate;


	class ScheduleRowMapper implements RowMapper<ScheduleEntity> {

		@Override
		public ScheduleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ScheduleEntity scheduleEntity = new ScheduleEntity();
			scheduleEntity.setYmd(rs.getString("ymd"));
			scheduleEntity.setUser1(rs.getString("user1"));
			scheduleEntity.setMemo1(rs.getString("memo1"));
			scheduleEntity.setUser2(rs.getString("user2"));
			scheduleEntity.setMemo2(rs.getString("memo2"));
			scheduleEntity.setUser3(rs.getString("user3"));
			scheduleEntity.setMemo3(rs.getString("memo3"));
			return scheduleEntity;
		}
	}


	public List<ScheduleEntity> selectScheduleAll() {

		return jdbcTemplate.query("SELECT * FROM schedule", new ScheduleRowMapper());
	}
}
