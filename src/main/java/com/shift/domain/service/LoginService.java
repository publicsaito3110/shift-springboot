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


	/**
	 * [Service] (/login/auth)
	 *
	 * @param userId authentication
	 * @return void
	 */
	public LoginAuthBean loginAuth(String userId) {

		this.selectUserByUserId(userId);
		boolean isLogin = this.isCheckLoginUser();
		this.generateSession();

		//Beanにセット
		LoginAuthBean loginAuthBean = new LoginAuthBean(isLogin, this.errorMassage);
		return loginAuthBean;
	}

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserRepository userRepository;


	//フィールド
	private UserEntity userEntity;
	private boolean isLogin;
	private String errorMassage;


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>userIdから一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はEmptyとなる
	 * </p>
	 *
	 * @param userId authentication
	 * @return void
	 */
	private void selectUserByUserId(String userId) {

		Optional<UserEntity> userEntityOpt = this.userRepository.findById(userId);

		if (userEntityOpt.isPresent()) {
			this.userEntity = userEntityOpt.get();
		}
	}


	/**
	 * ログイン判定処理
	 *
	 * <p>ログイン情報を基に取得したユーザーを判定する<br>
	 * 一致するユーザーがいないまたは退職済みの場合はfalseになる<br>
	 * また、falseのときはエラーメッセージがerrorMassage(フィールド)にセットされる
	 * </p>
	 *
	 * @param void
	 * @return boolean true: ログイン情報から一致するユーザーかつ退職済みでないユーザであるとき<br>
	 * false: ログイン情報から一致するユーザがいないまたは退職済みであるとき
	 */
	private boolean isCheckLoginUser() {

		//ログイン情報からユーザーを取得できなかったとき
		if (this.userEntity == null) {

			this.errorMassage = "IDまたはパスワードが違います";
			this.isLogin = false;
			return false;
		}


		//delFlgを取得
		String delFlg = CommonUtil.changeEmptyByNull(this.userEntity.getDelFlg());

		//退職済みだったとき
		if (delFlg.matches(Const.PATTERN_USER_DEL_FLG)) {

			this.errorMassage = "このユーザーは現在ログインできません";
			this.isLogin = false;
			return false;
		}

		this.isLogin = true;
		return true;
	}


	/**
	 * セッション処理
	 *
	 * <p>ログイン情報からセッションをセットする<br>
	 * ただし、isLoginがfalse(ログインが認証されていない)ときはセッションをセットしない
	 * </p>
	 *
	 * @param void
	 * @return void
	 */
	private void generateSession() {

		//ログインが認証されていないとき
		if (!this.isLogin) {
			return;
		}

		//セッションを完全に削除
		this.httpSession.invalidate();

		//セッションをセット
		AccountBean accountBean = new AccountBean(this.userEntity);
		this.httpSession.setAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN, accountBean);
	}
}
