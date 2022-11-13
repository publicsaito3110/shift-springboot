package com.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shift.domain.model.bean.LoginAuthBean;
import com.shift.domain.model.bean.LoginForgotIdSendBean;
import com.shift.domain.model.bean.LoginForgotPasswordResetAuthBean;
import com.shift.domain.model.bean.LoginForgotPasswordResetBean;
import com.shift.domain.model.bean.LoginForgotPasswordResetModifyBean;
import com.shift.domain.model.bean.LoginForgotPasswordSendBean;
import com.shift.domain.service.LoginService;
import com.shift.form.LoginForgotPasswordResetModifyForm;

/**
 * @author saito
 *
 */
@Controller
public class LoginController extends BaseController {

	@Autowired
	private LoginService loginService;


	/**
	 * ログイン画面<br>
	 * [Controller] (/login)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login")
	public ModelAndView login(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("isAlertLoginFailed", false);
		//View
		modelAndView.setViewName("login");
		return modelAndView;
	}


	/**
	 * ログイン事後処理機能<br>
	 * [Controller] (/login/auth)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/auth")
	public ModelAndView loginAuth(Authentication authentication, ModelAndView modelAndView) {

		//--------------------------
		// ログイン認証成功時に実行
		//--------------------------
		//Service
		LoginAuthBean loginAuthBean = loginService.loginAuth(authentication.getName());

		//ログイン可能ユーザーでなかったとき
		if (!loginAuthBean.isLogin()) {

			//ログイン画面へ戻す
			modelAndView.addObject("isAlertLoginFailed", true);
			modelAndView.addObject("errorMassage", loginAuthBean.getErrorMassage());
			//View
			modelAndView.setViewName("/login");
			return modelAndView;
		}

		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}


	/**
	 * ユーザパスワード再設定メール入力画面<br>
	 * [Controller] (/login/forgot-password)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/forgot-password")
	public ModelAndView loginForgotPassword(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("isAlertSuccess", false);
		modelAndView.addObject("isAlertFailed", false);
		//View
		modelAndView.setViewName("login-forgot-password");
		return modelAndView;
	}


	/**
	 * ユーザパスワード再設定メール送信機能<br>
	 * [Controller] (/login/forgot-password/send)
	 *
	 * @param email RequestParameter 再設定したいユーザの登録済みメール
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/forgot-password/send")
	public ModelAndView loginForgotPasswordSend(@RequestParam(value="email") String email, Authentication authentication, ModelAndView modelAndView) {

		//Service
		LoginForgotPasswordSendBean loginForgotPasswordSendBean = loginService.loginForgotPasswordSend(email);
		if (loginForgotPasswordSendBean.isInsert() && loginForgotPasswordSendBean.isSuccessSendEmail()) {
			//再設定フォームをメールに送信成功したとき
			modelAndView.addObject("isAlertSuccess", true);
			modelAndView.addObject("isAlertFailed", false);
			//View
			modelAndView.setViewName("login-forgot-password");
			return modelAndView;
		}

		//再設定フォームをメールに送信失敗とき
		modelAndView.addObject("isAlertSuccess", false);
		modelAndView.addObject("isAlertFailed", true);
		//View
		modelAndView.setViewName("login-forgot-password");
		return modelAndView;
	}


	/**
	 * ユーザパスワード再設定画面認証機能1<br>
	 * [Controller] (/login/forgot-password/send)
	 *
	 * @param user RequestParameter 登録するユーザ
	 * @param urlParam RequestParameter 再設定用のURLパラメーター
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/forgot-password/reset")
	public ModelAndView loginForgotPasswordReset(@RequestParam(value="user") String user, @RequestParam(value="urlParam") String urlParam, Authentication authentication, ModelAndView modelAndView) {

		//Service
		LoginForgotPasswordResetBean loginForgotPasswordResetBean = loginService.loginForgotPasswordReset(user, urlParam);
		if (!loginForgotPasswordResetBean.isTempPasswordAuth()) {
			//再設定フォームをメールに送信失敗したとき、404へ強制的に遷移する
			modelAndView.setViewName("/error/404");
			return modelAndView;
		}

		//再設定フォームをメールに送信成功したとき
		modelAndView.addObject("user", loginForgotPasswordResetBean.getTempPasswordEntity().getUser());
		modelAndView.addObject("urlParam", loginForgotPasswordResetBean.getTempPasswordEntity().getUrlParam());
		//View
		modelAndView.setViewName("login-forgot-password-reset");
		return modelAndView;
	}


	/**
	 * ユーザパスワード再設定画面認証機能2<br>
	 * [Controller] (/login/forgot-password/reset/auth)
	 *
	 * @param authCode RequestParameter 再設定用の認証コード
	 * @param user RequestParameter 登録するユーザ
	 * @param urlParam RequestParameter 再設定用のURLパラメーター
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/login/forgot-password/reset/auth", method = RequestMethod.POST)
	public ModelAndView loginForgotPasswordResetAuth(@RequestParam(value="authCode") String authCode, @RequestParam(value="user") String user, @RequestParam(value="urlParam") String urlParam, Authentication authentication, ModelAndView modelAndView) {

		//Service
		LoginForgotPasswordResetAuthBean loginForgotPasswordResetAuthBean = loginService.loginForgotPasswordResetAuth(authCode, user, urlParam);
		if (!loginForgotPasswordResetAuthBean.isTempPasswordAuth()) {
			//ワンタイムパスワード情報の認証に失敗したとき
			modelAndView.addObject("user", user);
			modelAndView.addObject("urlParam", urlParam);
			modelAndView.addObject("isAlertFailed", true);
			//View
			modelAndView.setViewName("login-forgot-password-reset");
			return modelAndView;
		}

		//ワンタイムパスワード情報の認証に成功したとき
		LoginForgotPasswordResetModifyForm loginForgotPasswordResetModifyForm = new LoginForgotPasswordResetModifyForm();
		loginForgotPasswordResetModifyForm.setAuthCode(authCode);
		loginForgotPasswordResetModifyForm.setUser(user);
		loginForgotPasswordResetModifyForm.setUrlParam(urlParam);
		modelAndView.addObject("loginForgotPasswordResetModifyForm", loginForgotPasswordResetModifyForm);
		modelAndView.addObject("isAlertFailed", false);
		//View
		modelAndView.setViewName("login-forgot-password-reset-modify");
		return modelAndView;
	}


	/**
	 * ユーザパスワード再設定機能<br>
	 * [Controller] (/login/forgot-password/reset/modify)
	 *
	 * @param loginForgotPasswordResetModifyForm RequestParameter Form
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/login/forgot-password/reset/modify", method = RequestMethod.POST)
	public ModelAndView loginForgotPasswordResetModify(@Validated @ModelAttribute LoginForgotPasswordResetModifyForm loginForgotPasswordResetModifyForm, BindingResult bindingResult, Authentication authentication, ModelAndView modelAndView) {

		//バリデーションエラーのとき
		if (bindingResult.hasErrors()) {
			//View
			modelAndView.setViewName("login-forgot-password-reset-modify");
			return modelAndView;
		}

		//Service
		LoginForgotPasswordResetModifyBean LoginForgotPasswordResetModifyBean = loginService.loginForgotPasswordResetModify(loginForgotPasswordResetModifyForm);
		//パスワード再設定に失敗したとき
		if (!LoginForgotPasswordResetModifyBean.isTempPasswordAuth() || !LoginForgotPasswordResetModifyBean.isUpdate()) {
			modelAndView.addObject("isAlertFailed", false);
			modelAndView.addObject("isModalResult", true);
			modelAndView.addObject("modalResultTitle", "パスワード再設定結果");
			modelAndView.addObject("modalResultContentFail", "パスワードの再設定に失敗しました。");
			//View
			modelAndView.setViewName("login");
			return modelAndView;
		}

		//パスワード再設定に成功したとき
		modelAndView.addObject("isAlertFailed", false);
		modelAndView.addObject("isModalResult", true);
		modelAndView.addObject("modalResultTitle", "パスワード再設定結果");
		modelAndView.addObject("modalResultContentSuccess", "パスワードの再設定が完了しました。");
		//View
		modelAndView.setViewName("login");
		return modelAndView;
	}


	/**
	 * ログインID取得画面<br>
	 * [Controller] (/login/forgot-id)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/forgot-id")
	public ModelAndView loginForgotId(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("isAlertSuccess", false);
		modelAndView.addObject("isAlertFailed", false);
		modelAndView.setViewName("login-forgot-id");
		//View
		return modelAndView;
	}


	/**
	 * ユーザID再取得メール送信機能<br>
	 * [Controller] (/login/forgot-id/send)
	 *
	 * @param email RequestParameter
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/login/forgot-id/send", method = RequestMethod.POST)
	public ModelAndView loginForgotIdSend(@RequestParam(value="email") String email, Authentication authentication, ModelAndView modelAndView) {

		//service
		LoginForgotIdSendBean loginForgotIdSendBean = loginService.loginForgotIdSend(email);
		//メール送信が成功したとき
		if (loginForgotIdSendBean.isSuccessSendEmail()) {
			modelAndView.addObject("isAlertSuccess", true);
			modelAndView.addObject("isAlertFailed", false);
			//View
			modelAndView.setViewName("login-forgot-id");
			return modelAndView;
		}

		//メール送信が失敗したとき
		modelAndView.addObject("isAlertSuccess", false);
		modelAndView.addObject("isAlertFailed", true);
		//View
		modelAndView.setViewName("login-forgot-id");
		return modelAndView;
	}


	/**
	 * セッションエラー画面<br>
	 * [Controller] (/login/error)
	 *
	 * @param authentication Authentication
	 * @param modelAndView ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping("/login/error")
	public ModelAndView loginError(Authentication authentication, ModelAndView modelAndView) {

		modelAndView.addObject("isAlertLoginFailed", true);
		modelAndView.addObject("errorMassage", "セッションの有効期限が切れました。もう一度ログインしてください。");
		//View
		modelAndView.setViewName("login");
		return modelAndView;
	}
}
