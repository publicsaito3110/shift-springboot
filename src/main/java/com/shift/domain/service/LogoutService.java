package com.shift.domain.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author saito
 *
 */
@Service
@Transactional
public class LogoutService extends BaseService {

	@Autowired
	private HttpSession httpSession;


	/**
	 * ログアウト機能<br>
	 * [Service] (/logout)
	 *
	 * @param void
	 * @return void
	 */
	public void logout() {

		removeSession();
	}


	/**
	 * セッション削除処理
	 *
	 * <p>保持しているセッションを全て削除する</p>
	 *
	 * @param void
	 * @return void
	 */
	private void removeSession() {

		//セッションを全て削除
		httpSession.invalidate();
	}
}
