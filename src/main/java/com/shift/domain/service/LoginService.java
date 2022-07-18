package com.shift.domain.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.Const;
import com.shift.domain.model.bean.AccountBean;
import com.shift.domain.model.bean.LoginBean;
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
	 * @param userId Request Param
	 * @param password Request Param
	 * @return void
	 */
	public LoginBean loginAuth(String userId, String password) {

		this.selectUserByUserIdPassword(userId, password);
		boolean isLogin = this.checkLoginUser();
		this.generateSession();

		//Beanにセット
		LoginBean loginBean = new LoginBean(isLogin, this.errorMassage);
		return loginBean;
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
	 * <p>userIdとpasswordから一致するユーザを取得する<br>
	 * ただし、一致するユーザーがいない場合はuserEntityのフィールドは全てnullとなる
	 * </p>
	 *
	 * @param userId
	 * @param password
	 * @return void
	 */
	private void selectUserByUserIdPassword(String userId, String password) {

		UserEntity userEntity = this.userRepository.findByIdAndPassword(userId, password);
		this.userEntity = userEntity;
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
	private boolean checkLoginUser() {

		//ログイン情報からユーザーを取得できなかったとき
		if (this.userEntity == null) {

			this.errorMassage = "IDまたはパスワードが違います";
			this.isLogin = false;
			return false;
		}

		//退職済みだったとき
		if (Const.PATTERN_DEL_FLG.equals(this.userEntity.getDelFlg())) {

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
	 * ただし、isLogin=false(ログインが認証されていない)ときはセッションをセットしない
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
		this.httpSession.setAttribute("ACCOUNT_BEAN", accountBean);
	}
}
