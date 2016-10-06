package com.example.dophone.Dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDBHelper extends SQLiteOpenHelper {

	public ContactDBHelper(Context context, String name, CursorFactory factory,
						   int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table "+UserContactDao.TABLE_NAME+" ("
				+UserContactDao.COL_ID+" integer primary key autoincrement,"
				+UserContactDao.COL_NAME+" varchar(20),"
				+UserContactDao.COL_TEL+" varchar(11),"
				+UserContactDao.COL_BIRTHDAY+" data,"
				+UserContactDao.COL_HEADICON+" blob,"
				+UserContactDao.COL_GROUP+" integer,"
				+UserContactDao.COL_SYSTEM_ID+" integer,"
				+UserContactDao.COL_PARENT_ID+" integer,"
				+UserContactDao.COL_PINYIN_NAME+" varchar(50))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
