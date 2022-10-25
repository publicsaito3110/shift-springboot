package com.shift.domain.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author saito
 *
 */
@Entity
@Data
public class DmUnreadCountDto {

	//フィールド
	@Id
	@Column(name = "unread_count" , unique = true)
	private Integer unreadCount;
}
