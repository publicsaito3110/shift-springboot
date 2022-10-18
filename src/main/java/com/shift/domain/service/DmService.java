package com.shift.domain.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.DmAddressBean;
import com.shift.domain.model.bean.DmBean;
import com.shift.domain.model.bean.DmTalkBean;
import com.shift.domain.model.bean.DmTalkSendBean;
import com.shift.domain.model.dto.DmChatDto;
import com.shift.domain.model.dto.DmMenuDto;
import com.shift.domain.model.dto.DmUnreadCountDto;
import com.shift.domain.model.entity.DmEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.DmChatRepository;
import com.shift.domain.repository.DmMenuRepository;
import com.shift.domain.repository.DmRepository;
import com.shift.domain.repository.DmUnreadCountRepository;
import com.shift.domain.repository.UserRepository;
import com.shift.form.DmTalkSendForm;

/**
 * @author saito
 *
 */
@Service
public class DmService extends BaseService {

	@Autowired
	private DmRepository dmRepository;

	@Autowired
	private DmMenuRepository dmMenuRepository;

	@Autowired
	private DmChatRepository dmChatRepository;

	@Autowired
	private DmUnreadCountRepository dmUnreadCountRepository;

	@Autowired
	private UserRepository userRepository;


	/**
	 * [Service] (/dm)
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @param httpSession ControllerからHttpSession
	 * @return DmBean
	 */
	public DmBean dm(String loginUser, HttpSession httpSession) {

		//ログインユーザの受信した最終メッセージを取得
		List<DmMenuDto> dmFinalHistoryList = selectFinalTalkHistoryAllUser(loginUser);
		//ログインユーザの全ての未読メッセージ数を取得する
		DmUnreadCountDto dmUnreadCountDto = selectUnreadMsgCountByLoginUser(loginUser);
		//セッションに未読メッセージ数をセットしなおす
		resetSessionDmUnreadMsgCount(dmUnreadCountDto, httpSession);

		//Beanにセット
		DmBean dmBean = new DmBean(dmFinalHistoryList);
		return dmBean;
	}


	/**
	 * [Service] (/dm/address)
	 *
	 * @param keyword RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return DmTalkBean
	 */
	public DmAddressBean dmAddress(String keyword, String loginUser) {

		//未退職ユーザを全て取得する
		List<UserEntity> userList = selectUserByKeyword(keyword, loginUser);

		//Beanにセット
		DmAddressBean dmAddressBean = new DmAddressBean(userList);
		return dmAddressBean;
	}


	/**
	 * [Service] (/dm/talk)
	 *
	 * @param receiveUser RequestParameter 取得したいユーザ間のメッセージ
	 * @param loginUser Authenticationから取得したユーザID
	 * @param httpSession ControllerからHttpSession
	 * @return DmTalkBean
	 */
	public DmTalkBean dmTalk(String receiveUser, String loginUser, HttpSession httpSession) {

		//ユーザを取得する
		UserEntity userEntity = selectUserByReceiveUser(receiveUser);
		//二者間のトークを取得する
		List<DmChatDto> talkHistoryList = selectTalkHistoryByReceiveUser(receiveUser, loginUser);
		//二者間のトークでログインユーザの未読済みメッセージを既読に更新する
		updateDmReadFlg(receiveUser, loginUser);
		//ログインユーザの全ての未読メッセージ数を取得する
		DmUnreadCountDto dmUnreadCountDto = selectUnreadMsgCountByLoginUser(loginUser);
		//セッションに未読メッセージ数をセットしなおす
		resetSessionDmUnreadMsgCount(dmUnreadCountDto, httpSession);

		//Beanにセット
		DmTalkBean dmTalkBean = new DmTalkBean(userEntity.getId(), userEntity.getName(), talkHistoryList);
		return dmTalkBean;
	}


	/**
	 * [Service] (/dm/talk/send)
	 *
	 * @param receiveUser RequestParameter
	 * @param msg RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return DmTalkSendBean
	 */
	public DmTalkSendBean dmTalkSend(DmTalkSendForm dmTalkSendForm, String loginUser) {

		//ユーザを取得する
		UserEntity userEntity = selectUserByReceiveUser(dmTalkSendForm.getReceiveUser());
		//ログインユーザが送信したメッセージを登録する
		insertChatByDmTalkSendForm(dmTalkSendForm, loginUser);
		//二者間のトークを取得する
		List<DmChatDto> talkHistoryList = selectTalkHistoryByReceiveUser(dmTalkSendForm.getReceiveUser(), loginUser);

		//Beanにセット
		DmTalkSendBean dmTalkSendBean = new DmTalkSendBean(userEntity.getId(), userEntity.getName(), talkHistoryList);
		return dmTalkSendBean;
	}


	/**
	 * セッション処理
	 *
	 * <p>未読メッセージ数セッションをセットしなおす</p>
	 *
	 * @param dmUnreadCountDto DBから取得したDmUnreadCountDto
	 * @param httpSession ControllerからHttpSession
	 * @return void
	 */
	private void resetSessionDmUnreadMsgCount(DmUnreadCountDto dmUnreadCountDto, HttpSession httpSession) {

		//未読メッセージ数を保持しているセッションを削除する
		httpSession.removeAttribute(Const.SESSION_KEYWORD_DM_UNREAD_COUNT);

		if (dmUnreadCountDto != null) {

			//未読メッセージが存在するとき、未読メッセージ数をセッションをセット
			httpSession.setAttribute(Const.SESSION_KEYWORD_DM_UNREAD_COUNT, dmUnreadCountDto.getUnreadCount());
		} else {

			//未読メッセージが存在しないとき、未読メッセージ数(0)をセッションをセット
			httpSession.setAttribute(Const.SESSION_KEYWORD_DM_UNREAD_COUNT, 0);
		}
	}


	/**
	 * [DB]最終トーク検索処理
	 *
	 * <p>ログインユーザーが送受信した最後のチャットをユーザーごとに取得する<br>
	 * ただし、一度もチャットを送受信していないときはEmptyとなる
	 * </p>
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return List<DmMenuDto> <br>
	 * フィールド(List&lt;DmMenuDto&gt;)<br>
	 * id, msg, msg_to_name, msg_to_id, icon_kbn, unreadCount
	 */
	private List<DmMenuDto> selectFinalTalkHistoryAllUser(String loginUser) {

		List<DmMenuDto> dmFinalHistoryList = dmMenuRepository.selectDmTalkHistoryByLoginUser(loginUser, Const.DM_READ_FLG);
		return dmFinalHistoryList;
	}


	/**
	 * [DB]キーワードユーザー検索処理
	 *
	 * <p>キーワードからユーザ情報を取得する<br>
	 * ただし、該当ユーザーがいない場合はEmptyとなる
	 * </p>
	 *
	 * @param keyword RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return void
	 */
	private List<UserEntity> selectUserByKeyword(String keyword, String loginUser) {

		keyword = CommonUtil.changeEmptyByNull(keyword);

		//keyWordをLIKEで一致するように検索する
		String trimKeyword = "%" + keyword + "%";
		List<UserEntity> userList = this.userRepository.selectUserByKeywordNotUserIdDelFlg(loginUser, Const.USER_DEL_FLG, trimKeyword);
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
	 * @return UserEntity<br>
	 * フィールド(UserEntity)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private UserEntity selectUserByReceiveUser(String receiveUser) {

		Optional<UserEntity> userEntityOptional = userRepository.findById(receiveUser);

		//receiveUserが存在しないとき
		if (!userEntityOptional.isPresent()) {

			//Emptyを返す
			return new UserEntity();
		}

		return userEntityOptional.get();
	}


	/**
	 * [DB]二者間トーク検索処理
	 *
	 * <p>ログインユーザーと相手との全てのチャット履歴を取得する<br>
	 * ただし、チャットがないときは何も表示しない
	 * </p>
	 *
	 * @param receiveUser RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return List<DmMenuDto><br>
	 * フィールド(List&lt;DmChatDto&gt;)<br>
	 * id, msg, msg_date, html_class_send_user
	 */
	private List<DmChatDto> selectTalkHistoryByReceiveUser(String receiveUser, String loginUser) {

		List<DmChatDto> talkHistoryList = dmChatRepository.selectTalkHistoryByLoginUserReceiveUser(loginUser, receiveUser, Const.HTML_CLASS_DM_MSG_LOGIN_USER, Const.HTML_CLASS_DM_MSG_NON_LOGIN_USER);
		return talkHistoryList;
	}


	/**
	 * [DB]2者間トーク既読済み更新処理
	 *
	 * <p>ログインユーザーと相手とのチャットでログインユーザが未読のチャットを全て取得し既読済みに更新する<br>
	 * ただし、未読のチャットがないときは何も表示しない
	 * </p>
	 *
	 * @param receiveUser RequestParameter DMを受信したユーザ
	 * @param loginUser Authenticationから取得したユーザID
	 * @return List<DmMenuDto><br>
	 * フィールド(List&lt;DmChatDto&gt;)<br>
	 * id, msg, msg_date, html_class_send_user
	 */
	private String updateDmReadFlg(String receiveUser, String loginUser) {

		//二者間のメッセージのうち受信ユーザーがログインユーザかつ未読メッセージをすべて取得
		List<DmEntity> dmEntityList = dmRepository.findBySendUserAndReceiveUserAndReadFlgNot(receiveUser, loginUser, Const.DM_READ_FLG);

		//ログインユーザの未読メッセージがないとき、何もせず"0"を返す
		if (dmEntityList.isEmpty()) {
			return "0";
		}

		//dmEntityListの回数だけループする
		for (DmEntity dmEntity: dmEntityList) {

			//既読済みに更新する
			dmEntity.setReadFlg(Const.DM_READ_FLG);
			dmRepository.save(dmEntity);
		}

		return "1";
	}


	/**
	 * [DB]未読メッセージ数取得処理
	 *
	 * <p>ログインユーザの未読メッセージ数を全て取得する<br>
	 * ただし、未読メッセージがない, 存在しないユーザID, チャットがないときはnullになる
	 * </p>
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return DmUnreadCountDto<br>
	 * フィールド(DmUnreadCountDto)<br>
	 * id, unreadCount
	 */
	private DmUnreadCountDto selectUnreadMsgCountByLoginUser(String loginUser) {

		DmUnreadCountDto dmUnreadCountDto = dmUnreadCountRepository.selectUnreadMsgCountByUserIdReadFlg(loginUser, Const.DM_READ_FLG);
		return dmUnreadCountDto;
	}


	/**
	 * [DB]メッセージ登録処理
	 *
	 * <p>ログインユーザーが送信したチャットのメッセージ, 送信先ユーザ, 時間を登録する<br>
	 * ただし、送信時間の取得はJava(TimeStamp)で行う
	 * </p>
	 *
	 * @param dmTalkSendForm RequestParameter Form
	 * @param msg RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return boolean<br>
	 * true: メッセージの登録が成功したとき<br>
	 * false: メッセージの登録が失敗したとき
	 */
	private boolean insertChatByDmTalkSendForm(DmTalkSendForm dmTalkSendForm, String loginUser) {

		DmEntity dmEntity = new DmEntity();
		dmEntity.setSendUser(loginUser);
		dmEntity.setReceiveUser(dmTalkSendForm.getReceiveUser());
		dmEntity.setMsg(dmTalkSendForm.getMsg());
		dmEntity.setMsgDate(new Timestamp(System.currentTimeMillis()).toString());
		dmRepository.save(dmEntity);
		return true;
	}
}
