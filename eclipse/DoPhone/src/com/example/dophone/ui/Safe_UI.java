package com.example.dophone.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dophone.R;
import com.example.dophone.activity.MainActivity;
import com.example.dophone.activity.Safe_Phone;
import com.example.dophone.util.MD5Utils;

public class Safe_UI {
	
	
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;
	private SharedPreferences sp;
	/**
	 * 设置密码对话框
	 */
	
	public Safe_UI(SharedPreferences sp){
		this.sp = sp;
	}
	
	public void showSetupPwdDialog(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		// 自定义一个布局文件
		View view = View.inflate(context, R.layout.setpassword_dialog, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//把这个对话框取消掉
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//  取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String password_confirm = et_setup_confirm.getText().toString().trim();
				if(TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)){
					Toast.makeText(context, "密码为空", 0).show();
					return;
				}
				//判断是否一致才去保存
				if(password.equals(password_confirm)){
					//一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5Password(password));//保存加密后的
					editor.commit();
					dialog.dismiss();
					//Log.i(TAG, "一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面");
					Intent intent = new Intent(context,Safe_Phone.class);
					context.startActivity(intent);
				}else{
					
					Toast.makeText(context, "密码不一致", 0).show();
					return ;
				}
				
				
				
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		
	}
	
	/**
	 * 输入密码对话框
	 */
	public void showEnterDialog(final Context context) {

		AlertDialog.Builder builder = new Builder(context);
		// 自定义一个布局文件
		View view = View.inflate(context, R.layout.inputpassword_dialog, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//把这个对话框取消掉
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//  取出密码
				String password = et_setup_pwd.getText().toString().trim();
				String savePassword = sp.getString("password", "");//取出加密后的
				if(TextUtils.isEmpty(password)){
					Toast.makeText(context, "密码为空", 1).show();
					return;
				}
				
				if(MD5Utils.md5Password(password).equals(savePassword)){
					//输入的密码是我之前设置的密码
					//把对话框消掉，进入主页面；
					dialog.dismiss();
					Intent intent = new Intent(context,Safe_Phone.class);
					context.startActivity(intent);
					
				}else{
					Toast.makeText(context, "密码错误", 1).show();
					et_setup_pwd.setText("");
					return;
				}
				
				
				
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		
	
		
	}
	
}
