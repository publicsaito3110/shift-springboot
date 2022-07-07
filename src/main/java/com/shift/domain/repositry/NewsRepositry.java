package com.shift.domain.repositry;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.entity.NewsEntity;

@Repository
public interface NewsRepositry extends BaseRepositry<NewsEntity, Integer> {

	@Query(value = "SELECT * FROM news WHERE ymd <= :nowYmd ORDER BY ymd DESC LIMIT :newsLimit",  nativeQuery = true)
	List<NewsEntity> selectNewsBeforeNowByNowYmdNewsLimit(String nowYmd, int newsLimit);
}
