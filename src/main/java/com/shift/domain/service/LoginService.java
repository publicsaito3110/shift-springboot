package com.shift.domain.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.AccountBean;
import com.shift.domain.model.bean.LoginAuthBean;
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

	//フィールド
	private String errorMassage;


	/**
	 * [Service] (/login/auth)
	 *
	 * @param loginUser Authenticationから取得したユーザID
	 * @return void
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
}
