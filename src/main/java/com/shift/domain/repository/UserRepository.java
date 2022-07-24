package com.shift.domain.repository;


import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.shift.domain.model.entity.UserEntity;

/**
 * @author saito
 *
 */
@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {

	UserEntity findByIdAndPassword(String id, String password);

	Optional<UserEntity> findById(String id);
}
