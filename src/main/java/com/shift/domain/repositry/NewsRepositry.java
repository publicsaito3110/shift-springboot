package com.shift.domain.repositry;

import org.springframework.stereotype.Repository;

import com.shift.entity.NewsEntity;

@Repository
public interface NewsRepositry extends BaseRepositry<NewsEntity, Integer> {

}
