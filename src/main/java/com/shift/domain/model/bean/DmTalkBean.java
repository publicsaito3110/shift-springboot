package com.shift.domain.model.bean;

import java.util.List;

import com.shift.domain.model.entity.DmEntity;

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
public class DmTalkBean {

	private String receiveUser;

	private String receiveUserName;

	private List<DmEntity> talkHistoryList;
}
