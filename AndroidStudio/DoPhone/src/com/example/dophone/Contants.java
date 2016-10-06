package com.example.dophone;

public class Contants {
	public static final int MSG_WHAT_LOGIN = 0;
	public static final int MSG_WHAT_REGISTER = 1;

	public static final int SPLAH_TIME = 1000;
	public static final String DB_NAME = "dophone_contact.db";
	public static final int DB_VERSION = 1;
	public static final String URI_STRING_CONTACT = "content://com.android.contacts/raw_contacts";
	public static final String COL_DATA1 = "data1";
	public static final String COL_MIMETYPE = "mimetype";
	public static final String MIMETYPE_PHONE = "vnd.android.cursor.item/phone_v2";
	public static final String MIMETYPE_NAME = "vnd.android.cursor.item/name";
	public static final String PROVIDER_CONTACT_HOST = "com.tanyaoxiang.contact.provider";
	public static final String PROVIDER_CONTACT_URI = "content://"
			+ PROVIDER_CONTACT_HOST;
	public static final int ACTIVITY_REQUEST_EDIT = 100;
	public static final int ACTIVITY_REQUEST_PICK = 100;
	public static final String TYPE = "type";
	public static final int ZOO_RQUEST_CODE = 102;
	public static final int GETCONTENT_RQUEST_CODE = 100;
	public static final int TYPE_NEW = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_SMS = 3;

	/**
	 * 百度文字搜索
	 */
	public static final String URL_BAIDU_SEARCH = "http://www.baidu.com/s?wd=";

}
