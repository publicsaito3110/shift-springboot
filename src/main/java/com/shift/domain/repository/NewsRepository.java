package com.shift.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.NewsEntity;

/**
 * @author saito
 *
 */
@Repository
public interface NewsRepository extends BaseRepository<NewsEntity, Integer> {

	@Query(value = "SELECT * FROM news WHERE ymd <= :nowYmd ORDER BY ymd DESC LIMIT :newsLimit",  nativeQuery = true)
	List<NewsEntity> selectNewsBeforeNowByNowYmdNewsLimit(String nowYmd, int newsLimit);

	List<NewsEntity> findByYmdGreaterThanOrderByYmd(String nowYmd);
}
