package com.shift.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author saito
 *
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>{

}
