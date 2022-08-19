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
	//Dm
	//-----
	public static final String USER_DEL_FLG = "1";


	//-----
	//Home
	//-----
	public static final int HOME_NEWS_SELECT_LIMIT = 5;
	public static final int HOME_NEWS_DISPLAY_NEW_ICON_LIMIT_DATE = 14;
	public static final String HOME_NEWS_NEW_ICON_SRC = "/png/icon/new-icon.png";
	public static final String HOME_NEWS_CATEGORY_ICON_SRC = "/png/icon/category-icon";


	//---------
	//NewsEdit
	//---------
	public static final String[] NEWS_CATEGORY_ALL_ARRAY = {"お知らせ", "重要", "シフト"};
	public static final int NEWS_RECORDABLE_MAX_MONTH = 3;


	//------
	//User
	//------
	public static final int USER_SELECT_LIMIT = 5;
	public static final int USER_LIST_PAGINATION_LIMIT_PAGE_ODD = 5;
	public static final String USER_GENDER_1_MAN = "男性";
	public static final String USER_GENDER_2_WOMEN = "女性";
	public static final String[] USER_GENDER_ALL_ARRAY = {"男性", "女性"};
	public static final String USER_ADMIN_FLG = "1";


	//-------
	//Common
	//-------

	//セッション
	public static final String SESSION_KEYWORD_ACCOUNT_BEAN = "KEY1_SESSION_ACCOUNT_BEAN";

	//管理者ロール
	public static final String ROLE_USER_ADMIN = "ADMIN";
	public static final String ROLE_USER_GENERAL = "GENERAL";

	//バリデーションパターン(正規表現)
	public static final String PATTERN_ROLE_USER_ADMIN = "1";
	public static final String PATTERN_ROLE_USER_GENERAL = "^$";

	public static final String PATTERN_DM_MSG_INPUT = "^(?![\\s\\S]{201,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";

	public static final String PATTERN_NEWS_CATEGORY_INPUT = "1|2|3";
	public static final String PATTERN_NEWS_CONTENT_INPUT = "^(?![\\s\\S]{201,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_NEWS_TITLE_INPUT = "^(?![\\s\\S]{21,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_NEWS_YMD_INPUT = "^[0-9]{4}+(0[1-9]|1[0-2])+(0[1-9]|[12][0-9]|3[01])$";
	public static final String PATTERN_NEWS_UNIQUE_DATE_INPUT = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

	public static final String PATTERN_USER_ADDRESS_INPUT = "^(?![\\s\\S]{101,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$|^$";
	public static final String PATTERN_USER_ADMIN_FLG = "1";
	public static final String PATTERN_USER_ADMIN_FLG_INPUT = "1|^$";
	public static final String PATTERN_USER_DEL_FLG = "1";
	public static final String PATTERN_USER_EMAIL_INPUT = "^[a-zA-Z0-9-_\\.]+@[a-zA-Z0-9-_\\.]+$|^$";
	public static final String PATTERN_USER_GENDER_INPUT = "1|2";
	public static final String PATTERN_USER_GENDER_1_MAN = "1";
	public static final String PATTERN_USER_GENDER_2_WOMEN = "2";
	public static final String PATTERN_USER_ID_INPUT = "^[A-Za-z0-9]{4}";
	public static final String PATTERN_USER_NAME_INPUT = "^(?![\\s\\S]{21,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_USER_NAME_KANA_INPUT = "^(?![\\s\\S]{41,})(^(\\s+)([ァ-ンー]+).*$|^([ァ-ンー]+)(\\s+).*$|[ァ-ンー]+).*$\"";
	public static final String PATTERN_USER_NOTE_INPUT = "^(?![\\s\\S]{401,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$|^$";
	public static final String PATTERN_USER_PASSWORD_INPUT = "^[A-Za-z0-9]{4,}";
	public static final String PATTERN_USER_TEL_INPUT = "^0\\d{9,11}+$|^$";
}
