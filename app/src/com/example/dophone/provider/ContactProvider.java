package com.example.dophone.provider;


import com.example.dophone.Contants;
import com.example.dophone.Dao.ContactDBHelper;
import com.example.dophone.Dao.UserContactDao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ContactProvider extends ContentProvider {
	SQLiteDatabase db;
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		ContactDBHelper cdbHelper = new ContactDBHelper(this.getContext(), Contants.DB_NAME, null, Contants.DB_VERSION);
		db = cdbHelper.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor c = db.query(UserContactDao.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long id = db.insert(UserContactDao.TABLE_NAME, null, values);
		return ContentUris.withAppendedId(uri, id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		// TODO Auto-generated method stub
		int result= db.update(UserContactDao.TABLE_NAME, values, selection, selectionArgs);

		return result;
	}

}
