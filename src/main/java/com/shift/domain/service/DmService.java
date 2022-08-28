package com.shift.domain.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.shift.domain.model.entity.DmEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.DmChatRepository;
import com.shift.domain.repository.DmMenuRepository;
import com.shift.domain.repository.DmRepository;
import com.shift.domain.repository.UserRepository;

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
	private UserRepository userRepository;


	/**
	 * [Service] (/dm)
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return DmBean
	 */
	public DmBean dm(String loginUser) {

		List<DmMenuDto> dmFinalHistoryList = selectFinalTalkHistoryAllUser(loginUser);

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

		List<UserEntity> userList = selectUserByKeyword(keyword, loginUser);

		//Beanにセット
		DmAddressBean dmAddressBean = new DmAddressBean(userList);
		return dmAddressBean;
	}


	/**
	 * [Service] (/dm/talk)
	 *
	 * @param receiveUser RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return DmTalkBean
	 */
	public DmTalkBean dmTalk(String receiveUser, String loginUser) {

		UserEntity userEntity = selectUserByReceiveUser(receiveUser);
		List<DmChatDto> talkHistoryList = selectTalkHistoryByReceiveUser(receiveUser, loginUser);

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
	public DmTalkSendBean dmTalkSend(String receiveUser, String msg, String loginUser) {

		UserEntity userEntity = selectUserByReceiveUser(receiveUser);
		insertChatByReceiveUserMsg(receiveUser, msg, loginUser);
		List<DmChatDto> talkHistoryList = selectTalkHistoryByReceiveUser(receiveUser, loginUser);

		//Beanにセット
		DmTalkSendBean dmTalkSendBean = new DmTalkSendBean(userEntity.getId(), userEntity.getName(), talkHistoryList);
		return dmTalkSendBean;
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
	 * id, msg, msg_to_name, msg_to_id
	 */
	private List<DmMenuDto> selectFinalTalkHistoryAllUser(String loginUser) {

		List<DmMenuDto> dmFinalHistoryList = new ArrayList<>();
		dmFinalHistoryList = dmMenuRepository.selectDmTalkHistoryByLoginUser(loginUser);

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

		List<DmChatDto> talkHistoryList = dmChatRepository.selectTalkHistoryByLoginUserReceiveUser(loginUser, receiveUser);
		return talkHistoryList;
	}


	/**
	 * [DB]チャット登録処理
	 *
	 * <p>ログインユーザーが送信したチャットのメッセージ, 送信先ユーザ, 時間を登録する<br>
	 * ただし、送信時間の取得はJava(TimeStamp)で行う
	 * </p>
	 *
	 * @param receiveUser RequestParameter
	 * @param msg RequestParameter
	 * @param loginUser Authenticationから取得したユーザID
	 * @return void
	 */
	private void insertChatByReceiveUserMsg(String receiveUser, String msg, String loginUser) {

		DmEntity dmEntity = new DmEntity();
		dmEntity.setSendUser(loginUser);
		dmEntity.setReceiveUser(receiveUser);
		dmEntity.setMsg(msg);
		dmEntity.setMsgDate(new Timestamp(System.currentTimeMillis()).toString());
		dmRepository.save(dmEntity);
	}
}
