package com.shift.domain.repositry;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shift.entity.ScheduleEntity;

@Repository
public interface ScheduleRepositry extends JpaRepository<ScheduleEntity, String> {

	List<ScheduleEntity> findByYmdLike(String ymd);
}
