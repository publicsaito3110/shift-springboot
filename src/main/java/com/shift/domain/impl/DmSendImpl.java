package com.shift.domain.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shift.domain.Interface.DmSendInterface;
import com.shift.domain.repositry.DmRepositry;
import com.shift.entity.DmEntity;

@Component
public class DmSendImpl extends BaseImple implements DmSendInterface {

	@Autowired
	DmRepositry dmRepositry;


	@Override
	public void chatRecord(String receiveUser, String msg) {

		String loginUser = "A001";

		DmEntity dmEntity = new DmEntity();
		dmEntity.setSendUser(loginUser);
		dmEntity.setReceiveUser(receiveUser);
		dmEntity.setMsg(msg);
		dmEntity.setMsgDate(new Timestamp(System.currentTimeMillis()).toString());
		dmRepositry.save(dmEntity);
	}


}
