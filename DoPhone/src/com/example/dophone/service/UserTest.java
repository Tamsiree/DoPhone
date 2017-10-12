package com.example.dophone.service;

import android.test.AndroidTestCase;
import android.util.Log;

public class UserTest extends AndroidTestCase {
	public void datatest() throws Throwable{
		DatabaseHelper dbhepler=new DatabaseHelper(this.getContext());
		dbhepler.getReadableDatabase();
	}
	//注册
	public void registerTest(String userName,String password) throws Throwable{
		UserService uService=new UserService(this.getContext());
		boolean flag=uService.register(userName,password);
		if(flag){
			Log.i("TAG","注册成功");
		}else{
			Log.i("TAG","注册失败");
		}
	}
	//登录
	public void loginTest(String userName,String password) throws Throwable{
		UserService uService=new UserService(this.getContext());
		boolean flag=uService.login(userName, password);
		if(flag){
			Log.i("TAG","登录成功");
		}else{
			Log.i("TAG","登录失败");
		}
	}

}
