package com.shift.domain.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shift.domain.Interface.DmSendInterface;
import com.shift.domain.model.entity.DmEntity;
import com.shift.domain.repository.DmRepository;

@Component
public class DmSendImpl extends BaseImple implements DmSendInterface {

	@Autowired
	DmRepository dmRepository;


	@Override
	public void chatRecord(String receiveUser, String msg) {

		String loginUser = "A001";

		DmEntity dmEntity = new DmEntity();
		dmEntity.setSendUser(loginUser);
		dmEntity.setReceiveUser(receiveUser);
		dmEntity.setMsg(msg);
		dmEntity.setMsgDate(new Timestamp(System.currentTimeMillis()).toString());
		dmRepository.save(dmEntity);
	}


}
