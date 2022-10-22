package com.shift.common;

/**
 * @author saito
 *
 */
public class Const {
	private Const() {
		//インスタンス化を禁止
	}


	//-----
	//Dm
	//-----
	public static final String DM_READ_FLG = "1";
	public static final String HTML_CLASS_DM_MSG_LOGIN_USER = "login-user";
	public static final String HTML_CLASS_DM_MSG_NON_LOGIN_USER = "non-login-user";


	//-----
	//Home
	//-----
	public static final int HOME_NEWS_SELECT_LIMIT = 5;
	public static final int HOME_NEWS_DISPLAY_NEW_ICON_LIMIT_DATE = 14;
	public static final String HOME_NEWS_NEW_ICON_SRC = "/png/icon/new-icon.png";
	public static final String HOME_NEWS_CATEGORY_ICON_SRC = "/png/icon/category-icon";
	public static final int HOME_DISPLAY_SCHEDULE_DAY = 7;


	//---------
	//Login
	//---------
	public static final int LOGIN_FOGOT_PASSWORD_AUTH_CODE_COUNT = 6;
	public static final int LOGIN_FOGOT_PASSWORD_URL_PARAM_COUNT = 100;


	//---------
	//NewsEdit
	//---------
	public static final String[] NEWS_CATEGORY_ALL_ARRAY = {"お知らせ", "重要", "シフト"};
	public static final int NEWS_RECORDABLE_MAX_MONTH = 3;


	//---------
	//Schedule
	//---------
	public static final String SCHEDULE_PRE_DAY_NOT_RECORDED = "0";
	public static final String SCHEDULE_PRE_DAY_RECORDED = "1";
	public static final String SCHEDULE_DAY_NOT_RECORDED = "0";
	public static final String SCHEDULE_DAY_RECORDED = "1";
	public static final int SCHEDULE_RECORDABLE_MAX_DIVISION = 7;
	public static final String[] SCHEDULE_HTML_CLASS_DISPLAY_COLOR_ARRAY = {"teal", "orange", "pink", "yellow", "purple", "cyan", "gray"};
	public static final String[] SCHEDULE_HTML_CLASS_DISPLAY_BG_COLOR_ARRAY = {"bg-teal", "bg-orange", "bg-pink", "bg-yellow", "bg-purple", "bg-cyan", "bg-gray"};
	public static final String[] SCHEDULE_HTML_CLASS_DISPLAY_BTN_COLOR_ARRAY = {"btn-teal", "btn-orange", "btn-pink", "btn-yellow", "btn-purple", "btn-cyan", "btn-gray"};
	public static final String SCHEDULE_TIME_INSERT_NEW_END_YMD = "99999999";
	public static final String SCHEDULE_TIME_INSERT_MODIFY_END_YMD = "00000000";


	//------
	//User
	//------
	public static final String USER_DEL_FLG = "1";
	public static final int USER_SELECT_LIMIT = 5;
	public static final int USER_LIST_PAGINATION_LIMIT_PAGE_ODD = 5;
	public static final String USER_GENDER_1_MAN = "男性";
	public static final String USER_GENDER_2_WOMEN = "女性";
	public static final String[] USER_GENDER_ALL_ARRAY = {"男性", "女性"};
	public static final String USER_ADMIN_FLG = "1";
	public static final String HTML_SRC_USER_DEFOULT_ICON = "/user-icon/common/guest.png";
	public static final String HTML_SRC_USER_ICON_DIR = "/user-icon/";
	public static final String USER_ICON_RECORD_ROOT_DIR = "static/user-icon/";
	public static final String[] USER_ICON_ALLOW_FILE_EXTENSION_ARRAY = {".jpg", ".jpeg", ".png"};
	public static final String USER_ICON_FLG_JPG = "1";
	public static final String USER_ICON_FLG_JPEG = "2";
	public static final String USER_ICON_FLG_PNG = "3";


	//-------
	//Common
	//-------

	//HTMLタグ
	public static final String HTML_TAG_BR = "<br>";

	//記号
	public static final String CHARACTER_PERCENT = "%";
	public static final String CHARACTER_CODE_BREAK_LINE = "\r\n";

	//セッション
	public static final String SESSION_KEYWORD_ACCOUNT_BEAN = "KEY1_SESSION_ACCOUNT_BEAN";
	public static final String SESSION_KEYWORD_DM_UNREAD_COUNT = "KEY2_SESSION_DM_UNREAD_COUNT";

	//ロール(役職)
	public static final String ROLE_USER_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER_GENERAL = "ROLE_GENERAL";

	//ドメイン
	public static final String DOMAIN_NAME = "http://localhost:8080";

	//---------------------------------
	//バリデーションパターン(正規表現)
	//---------------------------------

	//共通
	public static final String PATTERN_CHARACTER_NOT_NG_CHAR = "[^!\"#$%&'()\\*\\+\\-\\.,\\/:;<=>?@\\[\\\\\\]^_`{|}~]+";

	public static final String PATTERN_ROLE_USER_ADMIN = "1";
	public static final String PATTERN_ROLE_USER_GENERAL = "^$";

	//dm
	public static final String PATTERN_DM_MSG_INPUT = "^(?![\\s\\S]{201,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_DM_RECEIVE_USER_INPUT = "^[A-Za-z0-9]{4}";

	//news
	public static final String PATTERN_NEWS_CATEGORY_INPUT = "1|2|3";
	public static final String PATTERN_NEWS_CONTENT_INPUT = "^(?![\\s\\S]{201,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_NEWS_TITLE_INPUT = "^(?![\\s\\S]{21,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_NEWS_YMD_INPUT = "^[0-9]{4}+(0[1-9]|1[0-2])+(0[1-9]|[12][0-9]|3[01])$";
	public static final String PATTERN_NEWS_UNIQUE_DATE_INPUT = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

	//schedule
	public static final String PATTERN_SCHEDULE_YM_INPUT = "^[0-9]{4}+(0[1-9]|1[0-2])$";
	public static final String PATTERN_SCHEDULE_DAY_INPUT = "^(0[1-9]|[12][0-9]|3[01])$";
	public static final String PATTERN_SCHEDULE_UNIQUE_ADD_USER_ID_INPUT = "^[A-Za-z0-9]{4}|^$";

	//schedule_pre
	public static final String PATTERN_SCHEDULE_PRE_YM_INPUT = "^[0-9]{4}+(0[1-9]|1[0-2])$";

	//schedule_time
	public static final String PATTERN_SCHEDULE_TIME_UNIQUE_START_YM_INPUT = "^[0-9]{4}-(0[1-9]|1[0-2])$";
	public static final String PATTERN_SCHEDULE_TIME_NAME_MUST_INPUT = "^(?![\\s\\S]{21,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_SCHEDULE_TIME_NAME_ELECTIVE_INPUT = "^(?![\\s\\S]{21,}).*$|^$";
	public static final String PATTERN_SCHEDULE_TIME_HM_MUST_INPUT = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
	public static final String PATTERN_SCHEDULE_TIME_HM_ELECTIVE_INPUT = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$|^$";


	//user
	public static final String PATTERN_USER_ADDRESS_INPUT = "^(?![\\s\\S]{101,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$|^$";
	public static final String PATTERN_USER_ADMIN_FLG = "1";
	public static final String PATTERN_USER_ADMIN_FLG_INPUT = "1|^$";
	public static final String PATTERN_USER_DEL_FLG = "1";
	public static final String PATTERN_USER_EMAIL_INPUT = "^[a-zA-Z0-9-_\\.]+@[a-zA-Z0-9-_\\.]+$|^$";
	public static final String PATTERN_USER_GENDER_INPUT = "1|2";
	public static final String PATTERN_USER_GENDER_1_MAN = "1";
	public static final String PATTERN_USER_GENDER_2_WOMEN = "2";
	public static final String PATTERN_USER_ICON_KBN_ALL = "1|2|3";
	public static final String PATTERN_USER_ICON_KBN_JPG = "1";
	public static final String PATTERN_USER_ICON_KBN_JPEG = "2";
	public static final String PATTERN_USER_ICON_KBN_PNG = "3";
	public static final String PATTERN_USER_ID_INPUT = "^[A-Za-z0-9]{4}";
	public static final String PATTERN_USER_NAME_INPUT = "^(?![\\s\\S]{21,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$";
	public static final String PATTERN_USER_NAME_KANA_INPUT = "^(?![\\s\\S]{41,})(^(\\s+)([ァ-ンー]+).*$|^([ァ-ンー]+)(\\s+).*$|[ァ-ンー]+).*$";
	public static final String PATTERN_USER_NOTE_INPUT = "^(?![\\s\\S]{401,})(^(\\s+)(\\S+).*$|^(\\S+)(\\s+).*$|\\S).*$|^$";
	public static final String PATTERN_USER_PASSWORD_INPUT = "^[A-Za-z0-9]{4,}";
	public static final String PATTERN_USER_TEL_INPUT = "^0\\d{9,10}+$|^$";
}
