package com.shift.domain.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author saito
 *
 */
@Service
public class LogoutService extends BaseService {


	/**
	 * [Service] (/logout)
	 *
	 * @param void
	 * @return void
	 */
	public void logout() {

		this.removeSession();
	}

	@Autowired
	private HttpSession httpSession;


	/**
	 * セッション削除処理
	 *
	 * <p>保持しているセッション全てを削除する</p>
	 *
	 * @param void
	 * @return void
	 */
	private void removeSession() {

		//セッションを全て削除する
		httpSession.invalidate();
	}
}
