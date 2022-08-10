package com.shift.domain.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.ScheduleEntity;

/**
 * @author saito
 *
 */
@Repository
public interface ScheduleRepository extends BaseRepository<ScheduleEntity, String> {

	public List<ScheduleEntity> findByYmdLike(String ymd);
}
