package com.example.dophone.bean;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.dophone.Dao.UserContactDao;
import com.example.pinyinlib.PinyingBean;

public class ContactEntity extends PinyingBean {
	private int _id;
	private String contact_name;
	private String contact_tel;
	private String contact_birthday;
	private Bitmap contact_headicon;
	private int contact_system_id;
	private int contact_group;
	private int contact_parent_id;
	private String pinyin_name;
	
	
	
	
	public ContactEntity(Cursor c) {
		this._id = c.getInt(c.getColumnIndex(UserContactDao.COL_ID));
		this.contact_name = c.getString(c.getColumnIndex(UserContactDao.COL_NAME));
		this.contact_tel = c.getString(c.getColumnIndex(UserContactDao.COL_TEL));
		this.contact_birthday = c.getString(c.getColumnIndex(UserContactDao.COL_BIRTHDAY));
		
		byte[] icon = c.getBlob(c.getColumnIndex(UserContactDao.COL_HEADICON));
		if(icon != null){
			this.contact_headicon = BitmapFactory.decodeByteArray(icon, 0, icon.length) ;
		}
		this.contact_system_id = c.getInt(c.getColumnIndex(UserContactDao.COL_SYSTEM_ID));
		this.contact_group = c.getInt(c.getColumnIndex(UserContactDao.COL_GROUP));
		this.contact_parent_id = c.getInt(c.getColumnIndex(UserContactDao.COL_PARENT_ID));
		this.pinyin_name = c.getString(c.getColumnIndex(UserContactDao.COL_PINYIN_NAME));
	}
	public String getPinyin_name() {
		return pinyin_name;
	}
	public void setPinyin_name(String pinyin_name) {
		this.pinyin_name = pinyin_name;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_tel() {
		return contact_tel;
	}
	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}
	public String getContact_birthday() {
		return contact_birthday;
	}
	public void setContact_birthday(String contact_birthday) {
		this.contact_birthday = contact_birthday;
	}
	public Bitmap getContact_headicon() {
		return contact_headicon;
	}
	public void setContact_headicon(Bitmap contact_headicon) {
		this.contact_headicon = contact_headicon;
	}
	public int getContact_system_id() {
		return contact_system_id;
	}
	public void setContact_system_id(int contact_system_id) {
		this.contact_system_id = contact_system_id;
	}
	public int getContact_group() {
		return contact_group;
	}
	public void setContact_group(int contact_group) {
		this.contact_group = contact_group;
	}
	public int getContact_parent_id() {
		return contact_parent_id;
	}
	public void setContact_parent_id(int contact_parent_id) {
		this.contact_parent_id = contact_parent_id;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return contact_name;
	}
}
