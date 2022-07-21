package com.shift.common;

/**
 * @author saito
 *
 */
public class Const {
	private Const() {
		//インスタンス化を禁止
	}

	//-----------
	//Calendar
	//-----------
	public static final String HTML_CLASS_CALENDAR_SAT = "calendar-sattday";
	public static final String HTML_CLASS_CALENDAR_SUN = "calendar-sunday";


	//-----
	//Home
	//-----
	public static final int HOME_NEWS_SELECT_LIMIT = 5;
	public static final int HOME_NEWS_DISPLAY_NEW_ICON_LIMIT_DATE = 14;
	public static final String HOME_NEWS_NEW_ICON_SRC = "/png/icon/new-icon.png";
	public static final String HOME_NEWS_CATEGORY_ICON_SRC = "/png/icon/category-icon";


	//-------
	//Login
	//-------
	public static final String PATTERN_USER_DEL_FLG = "1";
	public static final String PATTERN_USER_ADMIN_FLG = "1";


	//---------
	//NewsEdit
	//---------
	public static final String[] NEWS_CATEGORY_ALL_ARRAY = {"お知らせ", "重要", "シフト"};


	//------
	//User
	//------
	public static final int USER_SELECT_LIMIT = 5;
	public static final int USER_LIST_PAGINATION_LIMIT_PAGE_ODD = 5;
	public static final String PATTERN_USER_GENDER_1_MAN = "1";
	public static final String USER_GENDER_1_MAN = "男性";
	public static final String PATTERN_USER_GENDER_2_WOMEN = "2";
	public static final String USER_GENDER_2_WOMEN = "女性";


	//-------
	//Common
	//-------
	public static final String SESSION_KEYWORD_ACCOUNT_BEAN = "KEY1_SESSION_ACCOUNT_BEAN";
}
