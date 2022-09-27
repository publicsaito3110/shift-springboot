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
import com.shift.domain.model.entity.UserEntity;
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

		UserEntity userEntity = selectUserByUserId(loginUser);
		boolean isLogin = isCheckLoginUser(userEntity);
		//ログインが認証されたとき
		if (isLogin) {
			generateSession(userEntity);
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

		UserEntity userEntity = selectUserByEmail(email);
		boolean isRecordedEmail = isCheckLoginUser(userEntity);
		boolean isSuccessSendEmail = false;
		if (isRecordedEmail) {
			//メールアドレスが存在するときメールの送信処理と判定を取得
			isSuccessSendEmail = isSuccessSendEmail(userEntity);
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
	private void generateSession(UserEntity userEntity) {

		//セッションを完全に削除
		httpSession.invalidate();

		//セッションをセット
		AccountBean accountBean = new AccountBean(userEntity);
		httpSession.setAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN, accountBean);
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
	private boolean isSuccessSendEmail(UserEntity userEntity) {

		//送信先のメールアドレスを取得
		String sendToEmailAddress = userEntity.getEmail();

		//送信するメールのタイトルを設定
		String emailTitle = "ユーザーIDの再取得ついて";

		//送信するメールの内容を設定
		String emailContent = userEntity.getName() + "様\n\r\n\rこの度はご利用ありがとうございます。ユーザーIDの再取得がされました。\n\r\n\rあなたのユーザーIDは " + userEntity.getId() + " です。\n\r\n\rこのメールアドレスに心当たりのない方はこちらのメールアドレスまでご返信ください。";

		//メールを送信し、送信の可否を取得
		boolean isSuccessSendEmail = new EmailLogic().isSuccessEmailSend(mailSender, sendToEmailAddress, emailTitle, emailContent);
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
