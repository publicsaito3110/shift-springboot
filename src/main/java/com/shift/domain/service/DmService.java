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

		//Beanにセット
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

		//Beanにセット
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

		//Beanにセット
		DmSendBean dmSendBean = new DmSendBean(talkHistoryList);
		return dmSendBean;
	}


	@Autowired
	private DmRepository dmRepository;

	@Autowired
	private DmMenuRepository dmMenuRepository;


	//フィールド
	private String loginUser;


	/**
	 * ログインユーザーID取得処理
	 *
	 * <p>SessionからログインしているユーザーのIDを取得</p>
	 *
	 * @param void
	 * @return void
	 */
	private void getLoginUserBySession() {

		String loginUser = "A001";
		this.loginUser = loginUser;
	}


	/**
	 * [DB]最終トーク検索処理
	 *
	 * <p>ログインユーザーが送受信した最後のチャットをユーザーごとに取得する<br>
	 * ただし、一度もチャットを送受信していないときはメッセージがないことを表示
	 * </p>
	 *
	 * @param void
	 * @return List<DmMenuDto> ログインユーザーが送受信した最後のチャット
	 */
	private List<DmMenuDto> selectFinalTalkHistoryAllUser() {

		List<DmMenuDto> dmHistoryList = new ArrayList<>();
		dmHistoryList = this.dmMenuRepository.selectDmTalkHistoryByLoginUser(this.loginUser);

		//ログインしているユーザがメッセージを一度も送受信していないとき
		if (dmHistoryList.isEmpty()) {

			//dmHistoryListに結果を代入
			DmMenuDto bean = new DmMenuDto();
			bean.setMsg("メッセージはありません");
			dmHistoryList.add(bean);
		}

		return dmHistoryList;
	}


	/**
	 * [DB]2者間トーク検索処理
	 *
	 * <p>ログインユーザーと相手の全てのチャット履歴を取得する<br>
	 * ただし、チャットがないときは何も表示しない
	 * </p>
	 *
	 * @param receiveUser RequestParameter
	 * @return List<DmMenuDto> ログインユーザーとチャット相手の全てのチャット
	 */
	private List<DmEntity> selectTalkHistoryByReceiveUser(String receiveUser) {

		List<DmEntity> talkHistoryList = new ArrayList<>();
		talkHistoryList = this.dmRepository.selectTalkHistoryByLoginUserReceiveUser(this.loginUser, receiveUser);

		return talkHistoryList;
	}


	/**
	 * [DB]チャット登録処理
	 *
	 * <p>ログインユーザーが送信したチャットのメッセージ, 相手, 時間を登録する<br>
	 * ただし、送信時間の取得はJava(TimeStamp)で行う
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
