package com.example.dophone.service;

import android.test.AndroidTestCase;
import android.util.Log;

public class UserTest extends AndroidTestCase {
	public void datatest() throws Throwable{
		DatabaseHelper dbhepler=new DatabaseHelper(this.getContext());
		dbhepler.getReadableDatabase();
	}
	//×¢²á
	public void registerTest(String userName,String password) throws Throwable{	
		UserService uService=new UserService(this.getContext());
		boolean flag=uService.register(userName,password);
		if(flag){
			Log.i("TAG","×¢²á³É¹¦");
		}else{
			Log.i("TAG","×¢²áÊ§°Ü");
		}
	}
	//µÇÂ¼
	public void loginTest(String userName,String password) throws Throwable{
		UserService uService=new UserService(this.getContext());
		boolean flag=uService.login(userName, password);
		if(flag){
			Log.i("TAG","µÇÂ¼³É¹¦");
		}else{
			Log.i("TAG","µÇÂ¼Ê§°Ü");
		}
	}
	
}
