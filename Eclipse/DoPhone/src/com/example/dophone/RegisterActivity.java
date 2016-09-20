package com.example.dophone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.dophone.service.UserService;
import com.example.dophone.ui.LoadingDialog;
import com.example.dophone.util.SysCtlUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private TextView tv_back;
	private TextView ed_mobile;
	private TextView ed_pwd,ed_repwd;
	private TextView ed_code;
	private Button btn_register;
	private LoadingDialog loadingDialog;
	private TextView tv_getCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		initView();
		initData();
	}

	private void initView() {
		loadingDialog = new LoadingDialog(this);
		btn_register = (Button) findViewById(R.id.btn_register);
		tv_back = (TextView) findViewById(R.id.tv_back);
		ed_mobile = (TextView) findViewById(R.id.ed_mobile);
		ed_pwd = (TextView) findViewById(R.id.ed_pwd);
		ed_repwd = (TextView) findViewById(R.id.ed_repwd);
		ed_code = (TextView) findViewById(R.id.ed_regcode);
		tv_getCode = (TextView) findViewById(R.id.tv_getCode);
	}

	private void initData() {
		// TODO Auto-generated method stub
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_getCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(SysCtlUtil.isNullString(ed_mobile.getText().toString())){
					SysCtlUtil.ShowToast(RegisterActivity.this, "请填写手机号码", false);
				}else if(!SysCtlUtil.isMobileNO(ed_mobile.getText().toString())){
					SysCtlUtil.ShowToast(RegisterActivity.this, "请填写正确的手机号码", false);
				}else{
					
				}*/
				SysCtlUtil.ShowToast(RegisterActivity.this, "将会在下一个版本中加入", false);
			}
		});
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SysCtlUtil.isNullString(ed_mobile.getText().toString())){
					SysCtlUtil.ShowToast(RegisterActivity.this, "请填写用户名", false);
				}else /*if(!SysCtlUtil.isMobileNO(ed_mobile.getText().toString())){
					SysCtlUtil.ShowToast(RegisterActivity.this, "请填写正确的手机号码", false);
				}else*/ if(SysCtlUtil.isNullString(ed_pwd.getText().toString())){
					SysCtlUtil.ShowToast(RegisterActivity.this, "请填写登录密码", false);
				}else if(!ed_pwd.getText().toString().equals(ed_repwd.getText().toString())){
					SysCtlUtil.ShowToast(RegisterActivity.this, "两次密码不一致", false);
				}/*else if(SysCtlUtil.isNullString(ed_code.getText().toString())){
					SysCtlUtil.ShowToast(RegisterActivity.this, "请填写验证码", false);
				}*/else{
					String name=ed_mobile.getText().toString().trim();
					String pass=ed_repwd.getText().toString().trim();
					Log.i("TAG",name+"_"+pass);
					UserService uService=new UserService(RegisterActivity.this);
					boolean flag=uService.register(name, pass);
					if(flag){
						Log.i("TAG","注册成功");
						Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
						RegisterActivity.this.finish();
					}else{
						Log.i("TAG","注册失败");
						Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
					}	
				}
			}
		});
	}
	
	
}
