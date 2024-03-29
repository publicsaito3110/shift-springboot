package com.shift.domain.model.bean;

import com.shift.domain.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saito
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyModifyBean {

	private boolean isSucessUpdate;

	private UserEntity userEntity;
}
