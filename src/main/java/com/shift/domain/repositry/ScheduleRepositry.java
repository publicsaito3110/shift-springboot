package com.shift.domain.repositry;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.shift.entity.ScheduleEntity;

@Repository
public interface ScheduleRepositry extends BaseRepositry<ScheduleEntity, String> {

	List<ScheduleEntity> findByYmdLike(String ymd);
}
