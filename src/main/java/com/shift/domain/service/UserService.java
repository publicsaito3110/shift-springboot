package com.shift.domain.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.domain.repository.UserRepository;

/**
 * @author saito
 *
 */
@Service
public class UserService extends BaseService {


	/**
	 * [Service] (/user)
	 *
	 * @param void
	 * @return void
	 */
	public void logout() {

		this.removeSession();
	}

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserRepository userRepository;


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
