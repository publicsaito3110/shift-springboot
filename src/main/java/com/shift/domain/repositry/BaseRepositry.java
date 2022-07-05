package com.shift.domain.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepositry<T, ID> extends JpaRepository<T, ID>{

}
