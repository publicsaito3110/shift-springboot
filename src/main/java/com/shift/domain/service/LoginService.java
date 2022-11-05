package com.shift.domain.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.common.EmailLogic;
import com.shift.common.LibApacheLogic;
import com.shift.domain.model.bean.AccountBean;
import com.shift.domain.model.bean.LoginAuthBean;
import com.shift.domain.model.bean.LoginForgotIdSendBean;
import com.shift.domain.model.bean.LoginForgotPasswordResetAuthBean;
import com.shift.domain.model.bean.LoginForgotPasswordResetBean;
import com.shift.domain.model.bean.LoginForgotPasswordResetModifyBean;
import com.shift.domain.model.bean.LoginForgotPasswordSendBean;
import com.shift.domain.model.dto.DmUnreadCountDto;
import com.shift.domain.model.entity.TempPasswordEntity;
import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.DmUnreadCountRepository;
import com.shift.domain.repository.TempPasswordRepository;
import com.shift.domain.repository.UserRepository;
import com.shift.form.LoginForgotPasswordResetModifyForm;

/**
 * @author saito
 *
 */
@Service
@Transactional
@PropertySource(value = "classpath:setting.properties")
public class LoginService extends BaseService {

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TempPasswordRepository tempPasswordRepository;

	@Autowired
	private DmUnreadCountRepository dmUnreadCountRepository;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${setting.net-domain-name}")
	private String domainName;

	//フィールド
	private String errorMassage;


	/**
	 * ログイン事後処理機能<br>
	 * [Service] (/login/auth)
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return LoginAuthBean
	 */
	public LoginAuthBean loginAuth(String loginUser) {

		//メールからログインユーザを取得する
		UserEntity userEntity = selectUserByUserId(loginUser);
		//取得したユーザがログイン可能ユーザかどうか判定する
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
	 * ユーザID再取得メール送信機能<br>
	 * [Service] (/login/forgot-id/send)
	 *
	 * @param email RequestParameter
	 * @return LoginForgotIdSendBean
	 */
	public LoginForgotIdSendBean loginForgotIdSend(String email) {

		//メールからユーザを取得
		UserEntity userEntity = selectUserByEmail(email);
		//取得したユーザがログイン可能ユーザか判定する
		boolean isLogin = isCheckLoginUser(userEntity);
		//ログイン可能ユーザのときユーザIDをメールに送信する
		boolean isSuccessSendEmail = false;
		if (isLogin) {
			isSuccessSendEmail = sendEmaiForUserId(userEntity);
		}

		//Beanにセット
		LoginForgotIdSendBean loginForgotIdSendBean = new LoginForgotIdSendBean(isSuccessSendEmail);
		return loginForgotIdSendBean;
	}


	/**
	 * ユーザパスワード再設定メール送信機能<br>
	 * [Service] (/login/forgot-password/send)
	 *
	 * @param email RequestParameter
	 * @return LoginForgotIdSendBean
	 */
	public LoginForgotPasswordSendBean loginForgotPasswordSend(String email) {

		//メールからユーザを取得
		UserEntity userEntity = selectUserByEmail(email);
		//取得したユーザがログイン可能ユーザか判定する
		boolean isRecordedEmail = isCheckLoginUser(userEntity);
		//ユーザ情報からワンタイムパスワード情報を取得
		List<TempPasswordEntity> tempPasswordEntityList = selectTempPasswordByUserEntity(userEntity);
		//ログイン可能ユーザかつ許容可能時間内にリセットリクエストをしていないとき
		boolean isInsert = false;
		boolean isSuccessSendEmail = false;
		if (isRecordedEmail && tempPasswordEntityList.isEmpty()) {
			//パスワード変更に必要となるワンタイムパスワード情報を作成
			String[] authCodeUrlParamArray = generateAuthCodeUrlParam();
			//ユーザとワンタイムパスワード情報を登録
			isInsert = insertTempPassword(userEntity, authCodeUrlParamArray[0], authCodeUrlParamArray[1]);
			//リセット情報をメールに送信
			isSuccessSendEmail = sendEmaiForResetPassword(userEntity, authCodeUrlParamArray[0], authCodeUrlParamArray[1]);
		}

		//Beanにセット
		LoginForgotPasswordSendBean loginForgotPasswordSendBean = new LoginForgotPasswordSendBean(isInsert, isSuccessSendEmail);
		return loginForgotPasswordSendBean;
	}


	/**
	 * ユーザパスワード再設定画面認証機能1<br>
	 * [Service] (/login/forgot-password/reset)
	 *
	 * @param user RequestParameter
	 * @param urlParam RequestParameter
	 * @return LoginForgotIdSendBean
	 */
	public LoginForgotPasswordResetBean loginForgotPasswordReset(String user, String urlParam) {

		//ユーザとURLパラメータからワンタイムパスワード情報を取得
		TempPasswordEntity tempPasswordEntity = selectTempPassword(user, urlParam);
		//ワンタイムパスワード情報を取得できたとき、認証コード入力を許可する
		boolean isTempPasswordAuth = false;
		if (tempPasswordEntity != null) {
			isTempPasswordAuth = true;
		}

		//Beanにセット
		LoginForgotPasswordResetBean loginForgotPasswordResetBean = new LoginForgotPasswordResetBean(isTempPasswordAuth, tempPasswordEntity);
		return loginForgotPasswordResetBean;
	}


	/**
	 * ユーザパスワード再設定画面認証機能2<br>
	 * [Service] (/login/forgot-password/reset/auth)
	 *
	 * @param authCode RequestParameter
	 * @param user RequestParameter
	 * @param urlParam RequestParameter
	 * @return LoginForgotIdSendBean
	 */
	public LoginForgotPasswordResetAuthBean loginForgotPasswordResetAuth(String authCode, String user, String urlParam) {

		//認証コード, ユーザ及びURLパラメータからワンタイムパスワード情報を取得
		TempPasswordEntity tempPasswordEntity = selectTempPassword(authCode, user, urlParam);
		//ワンタイムパスワード情報を取得できたとき、パスワード変更を許可する
		boolean isTempPasswordAuth = false;
		if (tempPasswordEntity != null) {
			isTempPasswordAuth = true;
		}

		//Beanにセット
		LoginForgotPasswordResetAuthBean loginForgotPasswordResetAuthBean = new LoginForgotPasswordResetAuthBean(isTempPasswordAuth, tempPasswordEntity);
		return loginForgotPasswordResetAuthBean;
	}


	/**
	 * ユーザパスワード再設定機能<br>
	 * [Service] (/login/forgot-password/reset/modify)
	 *
	 * @param email RequestParameter
	 * @return LoginForgotIdSendBean
	 */
	public LoginForgotPasswordResetModifyBean loginForgotPasswordResetModify(LoginForgotPasswordResetModifyForm loginForgotPasswordResetModifyForm) {

		//認証コード, ユーザ及びURLパラメータからワンタイムパスワード情報を取得
		TempPasswordEntity tempPasswordEntity = selectTempPassword(loginForgotPasswordResetModifyForm.getAuthCode(), loginForgotPasswordResetModifyForm.getUser(), loginForgotPasswordResetModifyForm.getUrlParam());
		//ワンタイムパスワード情報を取得できたとき、パスワード変更を許可する
		boolean isTempPasswordAuth = false;
		boolean isUpdate = false;
		if (tempPasswordEntity != null) {
			//パスワード変更を許可する
			isTempPasswordAuth = true;
			//パスワードを判定する
			isUpdate = updateUserPaawordByLoginForgotPasswordResetModifyForm(loginForgotPasswordResetModifyForm);
			//ユーザIDからユーザを取得する
			UserEntity userEntity = selectUserByUserId(tempPasswordEntity.getUser());
			//パスワードリセット完了メールを送信する
			sendEmaiForResetPasswordModifySuccess(userEntity);
			//ワンタイムパスワード情報を削除する
			deleteTempPassword(tempPasswordEntity);
		}

		//Beanにセット
		LoginForgotPasswordResetModifyBean LoginForgotPasswordResetModifyBean = new LoginForgotPasswordResetModifyBean(isTempPasswordAuth, isUpdate, tempPasswordEntity);
		return LoginForgotPasswordResetModifyBean;
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
	 * <p>ログイン情報からログイン済みを識別するセッションをセットする<br>
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
	 * ユーザIDメール送信処理
	 *
	 * <p>登録済みのメールアドレスにユーザIDを記載したメールを送信する<br>
	 * ただし、userEntityにメールアドレスが存在しない場合は何もしない
	 * </p>
	 *
	 * @param userEntity DBから取得したUserEntity
	 * @param isRecordedEmail ログイン情報
	 * @return void
	 */
	private boolean sendEmaiForUserId(UserEntity userEntity) {

		//送信先のメールアドレスを取得
		String sendToEmailAddress = userEntity.getEmail();

		//送信するメールのタイトルを設定
		String emailTitle = "ユーザーIDの再取得ついて";

		//送信するメールの内容を設定
		String emailContent = userEntity.getName() + "さん" + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE + "この度はご利用ありがとうございます。ユーザーIDの再取得がされました。" + Const.CHARACTER_CODE_BREAK_LINE +  Const.CHARACTER_CODE_BREAK_LINE + "あなたのユーザーIDは " + userEntity.getId() + " です。" + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE + "このメールアドレスに心当たりのない方はこちらのメールアドレスまでご返信ください。";

		//メールを送信し、送信の可否を取得
		boolean isSuccessSendEmail = new EmailLogic().sendEmail(mailSender, sendToEmailAddress, emailTitle, emailContent);
		return isSuccessSendEmail;
	}


	/**
	 * ワンタイムパスワード情報作成処理
	 *
	 * <p>パスワード再設定の際に必要となるワンタイムパスワード情報(authCode, urlParam)をランダムで作成する<br>
	 * String[0]が認証コード(authCode), String[1]がURLパラメータ(urlParam)
	 * </p>
	 *
	 * @param void
	 * @return String[] ランダムの認証コードとURLパラメータを格納した配列<br>
	 * String[0]が認証コード(authCode), String[1]がURLパラメータ(urlParam)
	 */
	private String[] generateAuthCodeUrlParam() {

		//認証コードとURLパラメータをランダムで作成する
		LibApacheLogic libApacheLogic = new LibApacheLogic();
		String authCode = libApacheLogic.getRandomNumeric(Const.LOGIN_FOGOT_PASSWORD_AUTH_CODE_COUNT);
		String urlParam = libApacheLogic.getRandomAlphanumeric(Const.LOGIN_FOGOT_PASSWORD_URL_PARAM_COUNT);

		//作成した認証コードとURLパラメータを配列にセットし、返す
		String[] authCodeUrlParamArray = {authCode, urlParam};
		return authCodeUrlParamArray;
	}


	/**
	 * ワンタイムパスワード情報メール送信処理
	 *
	 * <p>登録済みのメールアドレスにワンタイムパスワード情報を記載したメールを送信する<br>
	 * ただし、userEntityにメールアドレスが存在しない場合は何もしない
	 * </p>
	 *
	 * @param userEntity DBから取得したUserEntity
	 * @param authCode 認証コード
	 * @param urlParam URLパラメータ
	 * @return boolean<br>
	 * true: メールの送信が成功したとき<br>
	 * false: メールの送信が失敗したとき
	 */
	private boolean sendEmaiForResetPassword(UserEntity userEntity, String authCode, String urlParam) {

		//送信先のメールアドレスを取得
		String sendToEmailAddress = userEntity.getEmail();

		//送信するメールのタイトルを設定
		String emailTitle = "パスワード再設定ついて";

		//パスワード再設定画面のURLを設定
		String forgotPasswordAuthUrl = domainName + "/login/forgot-password/reset?user=" + userEntity.getId() + "&urlParam=" + urlParam;

		//送信するメールの内容を設定
		String emailContent = userEntity.getName() + "さん" + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE + "この度はご利用ありがとうございます。パスワードの再設定リクエストがされました。" + Const.CHARACTER_CODE_BREAK_LINE +  Const.CHARACTER_CODE_BREAK_LINE + "再設定用の認証コードは " + authCode + " です。" + Const.CHARACTER_CODE_BREAK_LINE + "下記のURLからパスワードを再設定してください。" +Const.CHARACTER_CODE_BREAK_LINE + forgotPasswordAuthUrl +  Const.CHARACTER_CODE_BREAK_LINE + "このメールアドレスの有効期限は1時間です。" + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE + "このメールアドレスに心当たりのない方はこちらのメールアドレスまでご返信ください。";

		//メールを送信し、送信の可否を取得
		boolean isSuccessSendEmail = new EmailLogic().sendEmail(mailSender, sendToEmailAddress, emailTitle, emailContent);
		return isSuccessSendEmail;
	}


	/**
	 * パスワードリセット完了メール送信処理
	 *
	 * <p>パスワードのリセットが完了したことを通知するメールを送信する<br>
	 * ただし、登録されているメールアドレスが無効の場合、メールアドレスは送信されず、falseが返される
	 * </p>
	 *
	 * @param userEntity DBから取得したUserEntity
	 * @param isRecordedEmail ログイン情報
	 * @return boolean<br>
	 * true: メールの送信が成功したとき<br>
	 * false: メールの送信が失敗したとき
	 */
	private boolean sendEmaiForResetPasswordModifySuccess(UserEntity userEntity) {

		//ユーザが存在しないとき、何もせずfalseを返す
		if (userEntity == null) {
			return false;
		}

		//送信先のメールアドレスを取得
		String sendToEmailAddress = userEntity.getEmail();

		//送信するメールのタイトルを設定
		String emailTitle = "パスワード再設定完了";

		//ログインURLを取得
		String loginUrl = domainName + "/login";

		//送信するメールの内容を設定
		String emailContent = userEntity.getName() + "さん" + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE + "この度はご利用ありがとうございます。パスワードの再設定が完了しました。" + Const.CHARACTER_CODE_BREAK_LINE + "下記のURLでログインしてください" + Const.CHARACTER_CODE_BREAK_LINE + loginUrl + Const.CHARACTER_CODE_BREAK_LINE + Const.CHARACTER_CODE_BREAK_LINE  + "このメールアドレスに心当たりのない方はこちらのメールアドレスまでご返信ください。";

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

		//ログインユーザが存在しないとき
		if (!userEntityOpt.isPresent()) {
			return null;
		}

		//UserEntityで取得し、返す
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


	/**
	 * [DB]ワンタイムパスワード情報登録処理
	 *
	 * <p>ワンタイムパスワード情報を登録する<br>
	 * ただし、ワンタイムパスワード情報の登録に失敗したときはfalseが返される
	 * </p>
	 *
	 * @param email RequestParameter
	 * @return boolean<br>
	 * true: ワンタイムパスワード情報の登録に成功したとき<br>
	 * false: ワンタイムパスワード情報の登録に失敗したとき
	 */
	private boolean insertTempPassword(UserEntity userEntity, String authCode, String urlParam) {

		//ユーザIDとワンタイムパスワード情報をtempPasswordEntityにセットする
		TempPasswordEntity tempPasswordEntity = new TempPasswordEntity();
		tempPasswordEntity.setUser(userEntity.getId());
		tempPasswordEntity.setAuthCode(authCode);
		tempPasswordEntity.setUrlParam(urlParam);
		tempPasswordEntity.setInsertDate(new Timestamp(System.currentTimeMillis()).toString());

		//ワンタイムパスワード情報を登録し、trueを返す
		tempPasswordRepository.save(tempPasswordEntity);
		return true;
	}


	/**
	 * [DB]ワンタイムパスワード情報検索処理
	 *
	 * <p>ユーザIDとURLパラメータと一致するワンタイムパスワード情報を取得する<br>
	 * ただし、ユーザが存在しない場合はnullとなる
	 * </p>
	 *
	 * @param userEntity DBから取得したUserEntity
	 * @param TempPasswordEntity RequestParameter URLパラメータ
	 * @return List<TempPasswordEntity> ワンタイムパスワード情報<br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	private List<TempPasswordEntity> selectTempPasswordByUserEntity(UserEntity userEntity) {

		//ユーザが存在しないとき、nullを返す
		if (userEntity == null) {
			return null;
		}

		//現在の日時とワンタイムパスワード認証の許容可能最小日時を配列で取得
		String[] nowMinDateTimeArray = getNowMinDateTimeArray();

		List<TempPasswordEntity> tempPasswordEntityList = tempPasswordRepository.findByUserAndInsertDateBetween(userEntity.getId(), nowMinDateTimeArray[1], nowMinDateTimeArray[0]);
		return tempPasswordEntityList;
	}


	/**
	 * [DB]ワンタイムパスワード情報検索処理
	 *
	 * <p>ユーザIDとURLパラメータと一致するワンタイムパスワード情報を取得する<br>
	 * ただし、許容可能日時の範囲内ではないまたは一致するワンタイムパスワード情報がない場合はnullとなる
	 * </p>
	 *
	 * @param user RequestParameter ユーザID
	 * @param TempPasswordEntity RequestParameter URLパラメータ
	 * @return UserEntity ワンタイムパスワード情報<br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	private TempPasswordEntity selectTempPassword(String user, String urlParam) {

		//現在の日時とワンタイムパスワード認証の許容可能最小日時を配列で取得
		String[] nowMinDateTimeArray = getNowMinDateTimeArray();

		TempPasswordEntity tempPasswordEntity = tempPasswordRepository.findByUserAndUrlParamAndInsertDateBetween(user, urlParam, nowMinDateTimeArray[1], nowMinDateTimeArray[0]);
		return tempPasswordEntity;
	}


	/**
	 * [DB]ワンタイムパスワード情報検索処理
	 *
	 * <p>認証コード, ユーザID, URLパラメータに一致するワンタイムパスワード情報を取得する<br>
	 * ただし、許容可能日時の範囲内ではないまたは一致するワンタイムパスワード情報がない場合はnullとなる
	 * </p>
	 *
	 * @param user RequestParameter 認証コード
	 * @param user RequestParameter ユーザID
	 * @param TempPasswordEntity RequestParameter URLパラメータ
	 * @return UserEntity ワンタイムパスワード情報<br>
	 * フィールド(TempPasswordEntity)<br>
	 * id, user, urlParam, authCode, insertDate
	 */
	private TempPasswordEntity selectTempPassword(String authCode, String user, String urlParam) {

		//現在の日時とワンタイムパスワード認証の許容可能最小日時を配列で取得
		String[] nowMinDateTimeArray = getNowMinDateTimeArray();

		TempPasswordEntity tempPasswordEntity = tempPasswordRepository.findByUserAndUrlParamAndAuthCodeAndInsertDateBetween(user, urlParam, authCode, nowMinDateTimeArray[1], nowMinDateTimeArray[0]);
		return tempPasswordEntity;
	}


	/**
	 * [DB]ユーザパスワード更新処理
	 *
	 * <p>ユーザのパスワードを再設定したパスワードに更新する<br>
	 * また、パスワードはハッシュ化して登録される<br>
	 * ただし、パスワード更新に失敗したらfalseが返される
	 * </p>
	 *
	 * @param userModifyForm RequestParameter Form
	 * ただし、許容されているファイル拡張子区分と一致しない場合は更新されない
	 * @param loginUser Authenticationから取得したユーザID
	 * @return boolean<br>
	 * true: ユーザ情報を更新したとき<br>
	 * false: ユーザ情報を更新できなかったとき
	 */
	private boolean updateUserPaawordByLoginForgotPasswordResetModifyForm(LoginForgotPasswordResetModifyForm loginForgotPasswordResetModifyForm) {

		//loginForgotPasswordResetModifyFormのユーザIDからユーザを取得
		Optional<UserEntity> userEntityOpt = userRepository.findById(loginForgotPasswordResetModifyForm.getUser());

		//ユーザを取得できなかったとき、何もせずfalseを返す
		if (!userEntityOpt.isPresent()) {
			return false;
		}

		//パスワードをハッシュ化
		String encodingPassword = passwordEncoder.encode(loginForgotPasswordResetModifyForm.getPassword());

		//userEntityに値をセットし、更新後trueを返す
		UserEntity userEntity = userEntityOpt.get();
		userEntity.setPassword(encodingPassword);
		userRepository.save(userEntity);
		return true;
	}


	/**
	 * [DB]ワンタイムパスワード情報削除処理
	 *
	 * <p>DBから取得したワンタイムパスワード情報を削除する<br>
	 * ただし、ワンタイムパスワード情報がない場合は必ず失敗する
	 * </p>
	 *
	 * @param tempPasswordEntity DBから取得したTempPasswordEntity
	 * @return boolean<br>
	 * true: ワンタイムパスワード情報の削除に成功したとき<br>
	 * false: ワンタイムパスワード情報の削除に失敗したとき
	 */
	private boolean deleteTempPassword(TempPasswordEntity tempPasswordEntity) {

		//tempPasswordEntityがnullのとき、何もせずfalseを返す
		if (tempPasswordEntity == null) {
			return false;
		}

		//削除し、trueを返す
		tempPasswordRepository.delete(tempPasswordEntity);
		return true;
	}



	/**
	 * [private共通処理]ワンタイムパスワード情報Timestamp文字列取得処理
	 *
	 * <p>ワンタイムパスワード情報で使用するTimestamp文字列を取得する<br>
	 * 現在の日時とワンタイムパスワード認証の許容可能最小日時を文字列で取得し、配列で返される<br>
	 * ただし、日時のフォーマットはTimestampの文字列となる
	 * </p>
	 *
	 * @param void
	 * @return String[]<br>
	 * String[0]は現在の日時, String[1]は許容可能最小日時
	 */
	private String[] getNowMinDateTimeArray() {

		//現在の日時とワンタイムパスワード認証の許容可能最小日時をLocalDateTimeで取得
		LocalDateTime nowLocalDateTime = LocalDateTime.now();
		LocalDateTime minLocalDateTime = nowLocalDateTime.minusHours(1);

		//取得したLocalDateTimeをTimeStampの文字列フォーマットで取得
		String nowDateTime = Timestamp.valueOf(nowLocalDateTime).toString();
		String minDateTime = Timestamp.valueOf(minLocalDateTime).toString();

		//配列に格納し、返す
		String[] nowMinDateTimeArray = {nowDateTime, minDateTime};
		return nowMinDateTimeArray;
	}
}
