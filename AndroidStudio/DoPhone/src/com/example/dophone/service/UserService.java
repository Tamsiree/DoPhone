package com.example.dophone.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class UserService {
    private DatabaseHelper dbHelper;
    public UserService(Context context){
        dbHelper=new DatabaseHelper(context);
    }

    //登录用
    public boolean login(String username,String password){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select * from user where userName=? and password=?";
        Cursor cursor=sdb.rawQuery(sql, new String[]{username,password});
        if(cursor.moveToFirst()==true){
            cursor.close();
            return true;
        }
        return false;
    }
    //注册用
    public boolean register(String userName,String password){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="insert into user(userName,password,levelId) values(?,?,?)";
        Object obj[]={userName,password,0};
        sdb.execSQL(sql, obj);
        return true;
    }
}