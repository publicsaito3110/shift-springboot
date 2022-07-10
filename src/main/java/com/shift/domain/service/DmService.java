package com.shift.domain.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.domain.model.bean.DmBean;
import com.shift.domain.model.bean.DmSendBean;
import com.shift.domain.model.bean.DmTalkBean;
import com.shift.domain.model.dto.DmMenuDto;
import com.shift.domain.model.entity.DmEntity;
import com.shift.domain.repository.DmMenuRepository;
import com.shift.domain.repository.DmRepository;

/**
 * @author saito
 *
 */
@Service
public class DmService extends BaseService {


	/**
	 * [Service] (/dm)
	 *
	 * @param ym RequestParameter
	 * @return DmBean
	 */
	public DmBean dm() {

		this.getLoginUserBySession();
		List<DmMenuDto> dmHistoryList = this.selectFinalTalkHistoryAllUser();

		//Bean�ɃZ�b�g
		DmBean dmBean = new DmBean(dmHistoryList);
		return dmBean;
	}


	/**
	 * [Service] (/dm/talk)
	 *
	 * @param receiveUser RequestParameter
	 * @return DmTalkBean
	 */
	public DmTalkBean dmTalk(String receiveUser) {

		this.getLoginUserBySession();
		List<DmEntity> talkHistoryList = this.selectTalkHistoryByReceiveUser(receiveUser);

		//Bean�ɃZ�b�g
		DmTalkBean dmTalkBean = new DmTalkBean(talkHistoryList);
		return dmTalkBean;
	}


	/**
	 * [Service] (/dm/talk/send)
	 *
	 * @param receiveUser RequestParameter
	 * @param msg RequestParameter
	 * @return DmSendBean
	 */
	public DmSendBean dmTalkSend(String receiveUser, String msg) {

		this.getLoginUserBySession();
		this.insertChatByReceiveUserMsg(receiveUser, msg);
		List<DmEntity> talkHistoryList = this.selectTalkHistoryByReceiveUser(receiveUser);

		//Bean�ɃZ�b�g
		DmSendBean dmSendBean = new DmSendBean(talkHistoryList);
		return dmSendBean;
	}


	@Autowired
	private DmRepository dmRepository;

	@Autowired
	private DmMenuRepository dmMenuRepository;


	//�t�B�[���h
	private String loginUser;


	/**
	 * ���O�C�����[�U�[ID�擾����
	 *
	 * <p>Session���烍�O�C�����Ă��郆�[�U�[��ID���擾</p>
	 *
	 * @param void
	 * @return void
	 */
	private void getLoginUserBySession() {

		String loginUser = "A001";
		this.loginUser = loginUser;
	}


	/**
	 * [DB]�ŏI�g�[�N��������
	 *
	 * <p>���O�C�����[�U�[������M�����Ō�̃`���b�g�����[�U�[���ƂɎ擾����<br>
	 * �������A��x���`���b�g�𑗎�M���Ă��Ȃ��Ƃ��̓��b�Z�[�W���Ȃ����Ƃ�\��
	 * </p>
	 *
	 * @param void
	 * @return List<DmMenuDto> ���O�C�����[�U�[������M�����Ō�̃`���b�g
	 */
	private List<DmMenuDto> selectFinalTalkHistoryAllUser() {

		List<DmMenuDto> dmHistoryList = new ArrayList<>();
		dmHistoryList = this.dmMenuRepository.selectDmTalkHistoryByLoginUser(this.loginUser);

		//���O�C�����Ă��郆�[�U�����b�Z�[�W����x������M���Ă��Ȃ��Ƃ�
		if (dmHistoryList.isEmpty()) {

			//dmHistoryList�Ɍ��ʂ���
			DmMenuDto bean = new DmMenuDto();
			bean.setMsg("���b�Z�[�W�͂���܂���");
			dmHistoryList.add(bean);
		}

		return dmHistoryList;
	}


	/**
	 * [DB]2�Ҋԃg�[�N��������
	 *
	 * <p>���O�C�����[�U�[�Ƒ���̑S�Ẵ`���b�g�������擾����<br>
	 * �������A�`���b�g���Ȃ��Ƃ��͉����\�����Ȃ�
	 * </p>
	 *
	 * @param receiveUser RequestParameter
	 * @return List<DmMenuDto> ���O�C�����[�U�[�ƃ`���b�g����̑S�Ẵ`���b�g
	 */
	private List<DmEntity> selectTalkHistoryByReceiveUser(String receiveUser) {

		List<DmEntity> talkHistoryList = new ArrayList<>();
		talkHistoryList = this.dmRepository.selectTalkHistoryByLoginUserReceiveUser(this.loginUser, receiveUser);

		return talkHistoryList;
	}


	/**
	 * [DB]�`���b�g�o�^����
	 *
	 * <p>���O�C�����[�U�[�����M�����`���b�g�̃��b�Z�[�W, ����, ���Ԃ�o�^����<br>
	 * �������A���M���Ԃ̎擾��Java(TimeStamp)�ōs��
	 * </p>
	 *
	 * @param receiveUser RequestParameter
	 * @param msg RequestParameter
	 * @return void
	 */
	private void insertChatByReceiveUserMsg(String receiveUser, String msg) {

		DmEntity dmEntity = new DmEntity();
		dmEntity.setSendUser(this.loginUser);
		dmEntity.setReceiveUser(receiveUser);
		dmEntity.setMsg(msg);
		dmEntity.setMsgDate(new Timestamp(System.currentTimeMillis()).toString());
		this.dmRepository.save(dmEntity);
	}
}
