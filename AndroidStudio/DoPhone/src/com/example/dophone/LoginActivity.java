package com.example.dophone;


import com.example.dophone.service.UserService;
import com.example.dophone.ui.CircleImageView;
import com.example.dophone.ui.LoadingDialog;
import com.example.dophone.util.PreferenceOperator;
import com.example.dophone.util.VonUtil;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private Button btn_login;
	private TextView tv_regiest;
	private TextView tv_forgot_pwd;
	private EditText ed_mobile_number;
	private EditText ed_password;
	private String username;
	private String password;
	private LoadingDialog loadingDialog;
	private CircleImageView circle_img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
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
		// TODO Auto-generated method stub
		circle_img = (CircleImageView) findViewById(R.id.circle_img);
		circle_img.setImageResource(R.drawable.boy);
		tv_regiest = (TextView) findViewById(R.id.tv_regiest);
		tv_regiest.setOnClickListener(this);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		tv_forgot_pwd = (TextView) findViewById(R.id.tv_forgot_pwd);
		tv_forgot_pwd.setOnClickListener(this);
		ed_mobile_number = (EditText) findViewById(R.id.ed_mobile_number);
		ed_password = (EditText) findViewById(R.id.ed_password);
		loadingDialog = new LoadingDialog(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		btn_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (arg0.getId()) {
			case R.id.tv_regiest:
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_login:
				String name=ed_mobile_number.getText().toString();
				String pass=ed_password.getText().toString();
				Log.i("TAG",name+"_"+pass);
				if(name.equals("")||pass.equals("")){
					Toast.makeText(LoginActivity.this, "登录信息不能为空", Toast.LENGTH_LONG).show();
				}else{
					UserService uService=new UserService(LoginActivity.this);
					boolean flag=uService.login(name, pass);
					if(flag){
						Log.i("TAG","登录成功");
						Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
						intent.setClass(LoginActivity.this, MainTabActivity.class);
						startActivity(intent);
						finish();
						PreferenceOperator.putContent(LoginActivity.this, "user_id", name);
					}else{
						Log.i("TAG","登录失败");
						Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
					}
				}
				break;
			case R.id.tv_forgot_pwd:
				VonUtil.ShowToast(this, "忘记密码了？我也不知道", false);
				break;
			default:
				break;
		}
	}

/*	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Contants.MSG_WHAT_LOGIN:
				
				break;
			default:
				break;
			}
		}
	};*/

}
