package com.example.dophone.Dao;


import java.util.ArrayList;
import java.util.List;

import com.example.dophone.bean.VirusInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 病毒数据库的增删改查业务类
 * @author Administrator
 *
 */
public class AntivirusDao {
	
	private final static String PATH = "data/data/com.example.dophone/databases/antivirus.db";
	
	public static boolean isVirus(String md5){
		boolean result = false;
		//打开病毒数据库
		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select * from datable where md5=?", new String[]{md5});
		if(cursor.moveToNext()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	
	public static List<VirusInfo> queryAll(){
		List<VirusInfo> listVirusInfos = new ArrayList<VirusInfo>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select * from datable",null);
		while(cursor.moveToNext()){
			VirusInfo virus = new VirusInfo();
			virus.setVirusId(cursor.getInt(0));
			virus.setMd5(cursor.getString(1));
			virus.setType(cursor.getString(2));
			virus.setName(cursor.getString(3));
			virus.setDesc(cursor.getString(4));
			listVirusInfos.add(virus);
		}
		cursor.close();
		db.close();
		return listVirusInfos;
	}
}

