package com.shift.domain.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.common.EmailLogic;
import com.shift.domain.model.bean.AccountBean;
import com.shift.domain.model.bean.LoginAuthBean;
import com.shift.domain.model.bean.LoginForgotIdSendBean;
import com.shift.domain.model.dto.DmUnreadCountDto;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.DmUnreadCountRepository;
import com.shift.domain.repository.UserRepository;

/**
 * @author saito
 *
 */
@Service
public class LoginService extends BaseService {

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DmUnreadCountRepository dmUnreadCountRepository;

	@Autowired
	private MailSender mailSender;

	//フィールド
	private String errorMassage;


	/**
	 * [Service] (/login/auth)
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return LoginAuthBean
	 */
	public LoginAuthBean loginAuth(String loginUser) {

		//ログインユーザを取得
		UserEntity userEntity = selectUserByUserId(loginUser);
		//ログイン可能ユーザかどうか判定する
		boolean isLogin = isCheckLoginUser(userEntity);
		//ログインが認証されたとき、セッションをセットする
		if (isLogin) {
			DmUnreadCountDto dmUnreadCountDto = selectUnreadMsgCountByLoginUser(loginUser);
			generateSession(userEntity, dmUnreadCountDto);
		}

		//Beanにセット
		LoginAuthBean loginAuthBean = new LoginAuthBean(isLogin, errorMassage);
		return loginAuthBean;
	}


	/**
	 * [Service] (/login/forgot-id/send)
	 *
	 * @param email RequestParameter
	 * @return LoginForgotIdSendBean
	 */
	public LoginForgotIdSendBean loginForgotIdSend(String email) {

		//ログインユーザを取得
		UserEntity userEntity = selectUserByEmail(email);
		//メールアドレスが登録されているか判定する
		boolean isRecordedEmail = isCheckLoginUser(userEntity);
		//メールアドレスが存在するときメールの送信処理と判定を取得
		boolean isSuccessSendEmail = false;
		if (isRecordedEmail) {
			isSuccessSendEmail = sendEmaiForUserIdl(userEntity);
		}

		//Beanにセット
		LoginForgotIdSendBean loginForgotIdSendBean = new LoginForgotIdSendBean(isSuccessSendEmail);
		return loginForgotIdSendBean;
	}


	/**
	 * ログイン可能ユーザ判定処理
	 *
	 * <p>ログイン情報(userEntity)を基に取得したユーザーがログイン可能であるか判定する<br>
	 * 一致するユーザーがいないまたは退職済みの場合はfalseになる<br>
	 * また、ログインが不可であるときはエラーメッセージをフィールド(errorMassage)にセットする
	 * </p>
	 *
	 * @param userEntity DBから取得したUserEntity
	 * @return boolean<br>
	 * true: ログイン情報から一致するユーザーかつ退職済みでないユーザであるとき<br>
	 * false: ログイン情報から一致するユーザがいないまたは退職済みであるとき
	 */
	private boolean isCheckLoginUser(UserEntity userEntity) {

		//ログイン情報からユーザーを取得できなかったとき
		if (userEntity == null) {

			//フィールドにセットし、falseを返す
			errorMassage = "IDまたはパスワードが違います";
			return false;
		}

		//退職済みだったとき
		if (CommonUtil.isSuccessValidation(userEntity.getDelFlg(), Const.PATTERN_USER_DEL_FLG)) {

			//フィールドにセットし、falseを返す
			errorMassage = "このユーザーは現在ログインできません";
			return false;
		}

		return true;
	}


	/**
	 * セッション処理
	 *
	 * <p>ログイン情報からセッションをセットする<br>
	 * ただし、isLoginがfalse(ログインが認証されていない)ときはセッションをセットしない
	 * </p>
	 *
	 * @param userEntity DBから取得したUserEntity
	 * @return void
	 */
	private void generateSession(UserEntity userEntity, DmUnreadCountDto dmUnreadCountDto) {

		//セッションを完全に削除
		httpSession.invalidate();

		//アカウント情報をセッションをセット
		AccountBean accountBean = new AccountBean(userEntity);
		httpSession.setAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN, accountBean);

		if (dmUnreadCountDto != null) {

			//未読メッセージが存在するとき、未読メッセージ数をセッションをセット
			httpSession.setAttribute(Const.SESSION_KEYWORD_DM_UNREAD_COUNT, dmUnreadCountDto.getUnreadCount());
		} else {

			//未読メッセージが存在しないとき、未読メッセージ数(0)をセッションをセット
			httpSession.setAttribute(Const.SESSION_KEYWORD_DM_UNREAD_COUNT, 0);
		}
	}


	/**
	 * メール送信処理
	 *
	 * <p>登録済みのメールアドレスにユーザIDを記載したメールを送信する<br>
	 * ただし、userEntityにメールアドレスが存在しない場合は何もしない
	 * </p>
	 *
	 * @param userEntity DBから取得したUserEntity
	 * @param isRecordedEmail ログイン情報
	 * @return void
	 */
	private boolean sendEmaiForUserIdl(UserEntity userEntity) {

		//送信先のメールアドレスを取得
		String sendToEmailAddress = userEntity.getEmail();

		//送信するメールのタイトルを設定
		String emailTitle = "ユーザーIDの再取得ついて";

		//送信するメールの内容を設定
		String emailContent = userEntity.getName() + "様" + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE + "この度はご利用ありがとうございます。ユーザーIDの再取得がされました。" + Const.CHARACTER_CODE_BREAK_LINE +  Const.CHARACTER_CODE_BREAK_LINE + "あなたのユーザーIDは " + userEntity.getId() + " です。" + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE + "このメールアドレスに心当たりのない方はこちらのメールアドレスまでご返信ください。";

		//メールを送信し、送信の可否を取得
		boolean isSuccessSendEmail = new EmailLogic().sendEmail(mailSender, sendToEmailAddress, emailTitle, emailContent);
		return isSuccessSendEmail;
	}


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>loginUserと一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はnullとなる
	 * </p>
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return UserEntity<br>
	 * フィールド(UserEntity)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private UserEntity selectUserByUserId(String loginUser) {

		Optional<UserEntity> userEntityOpt = userRepository.findById(loginUser);

		//loginUserが存在しないとき
		if (!userEntityOpt.isPresent()) {
			return null;
		}

		UserEntity userEntity = userEntityOpt.get();
		return userEntity;
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
	 * [DB]ユーザ検索処理
	 *
	 * <p>emailと一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はnullとなる
	 * </p>
	 *
	 * @param email RequestParameter
	 * @return UserEntity<br>
	 * フィールド(UserEntity)<br>
	 * id, name, nameKana, gender, password, address, tel, email, note, admin_flg, del_flg
	 */
	private UserEntity selectUserByEmail(String email) {

			UserEntity userEntity = userRepository.findByEmail(email);
			return userEntity;
	}
}
