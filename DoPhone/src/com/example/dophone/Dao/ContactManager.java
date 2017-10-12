package com.example.dophone.Dao;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.dophone.Contants;
import com.example.dophone.bean.ContactEntity;
import com.example.pinyinlib.PinyingBean;



/**
 * 联系人数据的增，删，改，查
 *
 * @author 湘子
 *
 */
public class ContactManager {

	public static String[][] MIMETYPE_DATA = {
			{ Contants.MIMETYPE_PHONE, UserContactDao.COL_TEL },
			{ Contants.MIMETYPE_NAME, UserContactDao.COL_NAME }
	};

	public String getFirstChar(String value) {
		// 首字符
		char firstChar = value.charAt(0);
		// 首字母分类
		String first = null;
		// 是否是非汉字
		String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

		if (print != null) {
			// 如果是中文 分类大写字母
			first = String.valueOf((char) (print[0].charAt(0) - 32));
			first = first + String.valueOf((char) (print[0].charAt(0)));
		} else {
			first = "";
		}

		return first;
	}


	public  List<PinyingBean> queryContactAll(ContentResolver cr) {
		List<PinyingBean> list = new ArrayList<PinyingBean>();
		Cursor c =cr.query(Uri.parse(Contants.PROVIDER_CONTACT_URI), null, null, null,null);
		c.getColumnCount();
		c.getCount();
		c.moveToFirst();
		while (c.moveToNext()) {
			// 获取名字
			ContactEntity ce = new ContactEntity(c);
			list.add(ce);
		}

		return list;
	}

	public List<PinyingBean> getContantsByName(String text,ContentResolver cr) {
		List<PinyingBean> list = new ArrayList<PinyingBean>();

		//查询名字
		Cursor c = cr.query(Uri.parse(Contants.PROVIDER_CONTACT_URI), null,UserContactDao.COL_PINYIN_NAME + " like ?", new String[] { "%" + text + "%" }, null);
		if (c.getCount()==0) {
			c = cr.query(Uri.parse(Contants.PROVIDER_CONTACT_URI), null,UserContactDao.COL_NAME + " like ?", new String[] { "%" + text + "%" }, null);
		}
		while (c.moveToNext()) {
			// 获取名字
			ContactEntity ce = new ContactEntity(c);
			list.add(ce);
		}

		return list;
	}


	public void syncSystemContact(ContentResolver cr){
		Cursor c= cr.query(Uri.parse(Contants.URI_STRING_CONTACT), new String []{UserContactDao.COL_ID}, null, null, null);
		while(c.moveToNext()){
			//获取系统联系人的ID
			int _id = c.getInt(0);

			String data_uri = Contants.URI_STRING_CONTACT + "/" + _id + "/data";
			Cursor c1 = cr.query(Uri.parse(data_uri),  new String[] {Contants.COL_DATA1, Contants.COL_MIMETYPE }, null, null, null);

			ContentValues cValues = new ContentValues();
			cValues.put(UserContactDao.COL_SYSTEM_ID, _id);
			String name = null;
			while(c1.moveToNext()){
				String data1 = c1.getString(0);
				String mimeType = c1.getString(1);

				for (int i = 0; i < MIMETYPE_DATA.length; i++) {
					String type = MIMETYPE_DATA[i][0];
					if(mimeType.equals(type)){
						cValues.put(MIMETYPE_DATA[i][1], data1);
						if(Contants.MIMETYPE_NAME.equals(type)){
							name = data1;
						}
						break;
					}
				}
			}
			Cursor c_id = cr.query(Uri.parse(Contants.PROVIDER_CONTACT_URI), null, UserContactDao.COL_SYSTEM_ID+" = ?", new String [] {_id+""}, null);
			if(c_id.getCount() == 0){
				if(name != null){
					cValues.put(UserContactDao.COL_PINYIN_NAME, getFirstChar(name));
					//	int a1 =cValues.size();
					Uri uri = cr.insert(Uri.parse(Contants.PROVIDER_CONTACT_URI), cValues);
					//	long a2= ContentUris.parseId(uri);
				}
			}
			//c_id.close();
			c1.close();
		}

	}

	public ContactEntity getOneContactById(int id,ContentResolver cr) {
		Cursor c = cr.query(Uri.parse(Contants.PROVIDER_CONTACT_URI), null,
				UserContactDao.COL_ID + "= ?", new String[] { id + "" }, null);

		if (c.moveToNext()) {
			ContactEntity ce = new ContactEntity(c);
			return ce;
		} else {
			return null;
		}
	}

	public Uri insertContact(ContentValues values,ContentResolver cr) {
		return cr.insert(Uri.parse(Contants.PROVIDER_CONTACT_URI), values);
	}

	public boolean updateContact(int id, ContentValues values,ContentResolver cr) {
		int result = cr.update(Uri.parse(Contants.PROVIDER_CONTACT_URI),values, UserContactDao.COL_ID + "= ?", new String[] { id + "" });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

}
