package com.shift.domain.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shift.common.CommonUtil;
import com.shift.common.Const;
import com.shift.domain.model.bean.AccountBean;
import com.shift.domain.model.bean.UserBean;
import com.shift.domain.model.dto.UserListDto;
import com.shift.domain.repository.UserListRepository;

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
	public UserBean user(String page, String keyword) {

		this.chackUserIdAdminUser();
		this.calcOffsetByPage(page);

		//管理者であるとき
		if (this.isAdminUser) {
			this.selectUserByKeyword(keyword);
		}
		//管理者でないとき
		if (!this.isAdminUser) {
			this.selectUserByUserId();
		}

		UserBean userBean = new UserBean(this.userList);
		return userBean;
	}

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserListRepository userListRepository;


	//フィールド
	private String userId;
	private boolean isAdminUser;
	private int offset;
	private List<UserListDto> userList;


	/**
	 * 管理者判定処理
	 *
	 * <p>Sessionから管理者かどうかを取得する<br>
	 * 管理者かどうかの判定をisAdminUser(フィールド)にセットする
	 * </p>
	 *
	 * @param void
	 * @return void
	 */
	private void chackUserIdAdminUser() {

		AccountBean accountBean = (AccountBean)httpSession.getAttribute(Const.SESSION_KEYWORD_ACCOUNT_BEAN);
		this.userId = accountBean.getUserId();
		this.isAdminUser = accountBean.isAdminUser();
	}


	/**
	 * 件目計算処理
	 *
	 * <p>指定したページに対応した件目(offset)を計算する<br>
	 * ただし、ページが指定されていないときはoffsetは0となる
	 * </p>
	 *
	 * @param page Request Param
	 * @return void
	 */
	private void calcOffsetByPage(String page) {

		//ページ数を指定されていないとき
		if (page == null || page.isEmpty()) {

			this.offset = 0;
			return;
		}

		//現在のページから表示すべき件目を計算する
		int nowPage = Integer.parseInt(page);
		int offset = (nowPage - 1) * Const.USER_SELECT_LIMIT;
		this.offset = offset;
	}


	/**
	 * [DB]ユーザ一覧検索処理
	 *
	 * <p>ユーザ一覧を取得する<br>
	 * ただし、keywordがない場合は全件を取得<br>
	 * 管理者ユーザのみの処理
	 * </p>
	 *
	 * @param keyword Request Param
	 * @return void
	 */
	private void selectUserByKeyword(String keyword) {

		//パラメータがないとき""に変換する
		keyword = CommonUtil.changeEmptyByNull(keyword);

		//keyWordをLIKEで一致するようにする
		keyword = "%" + keyword + "%";
		List<UserListDto> userList = userListRepository.selectUserByKeyWordLimitOffset(keyword, Const.USER_SELECT_LIMIT, this.offset);
		this.userList = userList;
	}


	/**
	 * [DB]ユーザ検索処理
	 *
	 * <p>ログインユーザを取得する<br>
	 * 一般ユーザのみの処理
	 * </p>
	 *
	 * @param void
	 * @return void
	 */
	private void selectUserByUserId() {

		List<UserListDto> userList = userListRepository.selectUserByUserId(this.userId);
		this.userList = userList;
	}


	/**
	 * 件目計算処理
	 *
	 * <p>指定したページに対応した件目(offset)を計算する<br>
	 * ただし、ページが指定されていないときはoffsetは0となる
	 * </p>
	 *
	 * @param page Request Param
	 * @return void
	 */
	private void calcNowPage(String page) {

		//ページ数を指定されていないとき
		if (page == null || page.isEmpty()) {

			this.offset = 0;
			return;
		}

		//現在のページから表示すべき件目を計算する
		int nowPage = Integer.parseInt(page);
		int offset = (nowPage - 1) * Const.USER_SELECT_LIMIT;
		this.offset = offset;
	}
}
