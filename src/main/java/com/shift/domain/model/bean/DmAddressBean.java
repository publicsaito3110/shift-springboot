package com.shift.domain.model.bean;

import java.util.List;

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
public class DmAddressBean {

	private List<UserEntity> userList;
}
