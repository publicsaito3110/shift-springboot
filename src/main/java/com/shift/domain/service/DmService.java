package com.shift.domain.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.AccountBean;
import com.shift.domain.model.bean.DmBean;
import com.shift.domain.model.bean.DmTalkAddressBean;
import com.shift.domain.model.bean.DmTalkBean;
import com.shift.domain.model.bean.DmTalkSendBean;
import com.shift.domain.model.dto.DmMenuDto;
import com.shift.domain.model.entity.DmEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.DmMenuRepository;
import com.shift.domain.repository.DmRepository;
import com.shift.domain.repository.UserRepository;

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
		this.selectUserByReceiveUser(receiveUser);
		List<DmEntity> talkHistoryList = this.selectTalkHistoryByReceiveUser(receiveUser);

		//Beanにセット
		DmTalkBean dmTalkBean = new DmTalkBean(this.userEntity.getId(), this.userEntity.getName(), talkHistoryList);
		return dmTalkBean;
	}


	/**
	 * [Service] (/dm/talk/address)
	 *
	 * @param keyword RequestParameter
	 * @return DmTalkBean
	 */
	public DmTalkAddressBean dmTalkAddress(String keyword) {

		this.getLoginUserBySession();
		List<UserEntity> userList = this.selectUserByKeyword(keyword);

		//Beanにセット
		DmTalkAddressBean dmTalkAddressBean = new DmTalkAddressBean(userList);
		return dmTalkAddressBean;
	}


	/**
	 * [Service] (/dm/talk/send)
	 *
	 * @param receiveUser RequestParameter
	 * @param msg RequestParameter
	 * @return DmTalkSendBean
	 */
	public DmTalkSendBean dmTalkSend(String receiveUser, String msg) {

		this.getLoginUserBySession();
		this.selectUserByReceiveUser(receiveUser);
		this.insertChatByReceiveUserMsg(receiveUser, msg);
		List<DmEntity> talkHistoryList = this.selectTalkHistoryByReceiveUser(receiveUser);

		//Beanにセット
		DmTalkSendBean dmTalkSendBean = new DmTalkSendBean(this.userEntity.getId(), this.userEntity.getName(), talkHistoryList);
		return dmTalkSendBean;
	}


	@Autowired
	private HttpSession httpSession;

	@Autowired
	private DmRepository dmRepository;

	@Autowired
	private DmMenuRepository dmMenuRepository;

	@Autowired
	private UserRepository userRepository;


	//フィールド
	private String loginUser;
	private UserEntity userEntity;


	/**
	 * ログインユーザーID取得処理
	 *
	 * <p>SessionからログインしているユーザーのIDを取得</p>
	 *
	 * @param void
	 * @return void
	 */
	private void getLoginUserBySession() {

		AccountBean accountBean = (AccountBean)httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN);
		this.loginUser = accountBean.getUserId();
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
	 * [DB]キーワードユーザー検索処理
	 *
	 * <p>キーワードからユーザ情報を取得する<br>
	 * ただし、該当ユーザーがいない場合はEmptyとなる
	 * </p>
	 *
	 * @param keyword RequestParameter
	 * @return void
	 */
	private List<UserEntity> selectUserByKeyword(String keyword) {

		keyword = CommonUtil.changeEmptyByNull(keyword);

		//keyWordをLIKEで一致するように検索する
		keyword = "%" + keyword + "%";
		List<UserEntity> userList = this.userRepository.selectUserByKeywordNotUserIdDelFlg(this.loginUser, Const.USER_DEL_FLG, keyword);

		return userList;
	}


	/**
	 * [DB]ユーザー検索処理
	 *
	 * <p>チャット相手のユーザ情報を取得する<br>
	 * ただし、チャット相手のユーザーが取得できない場合はEmptyとなる
	 * </p>
	 *
	 * @param receiveUser RequestParameter
	 * @return void
	 */
	private void selectUserByReceiveUser(String receiveUser) {

		Optional<UserEntity> userEntityOptional = this.userRepository.findById(receiveUser);

		//receiveUserが存在するときフィールドにセット
		if (userEntityOptional.isPresent()) {
			this.userEntity = userEntityOptional.get();
		}
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
