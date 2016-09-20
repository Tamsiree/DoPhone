package com.example.dophone;



import com.example.dophone.R;
import com.example.dophone.activity.MainActivity;
import com.example.dophone.util.PreferenceOperator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class SplashActivity extends Activity {
	
	private TextView tv_splash_version;
	private TextView tv_update_info;
	ProgressBar pg;
	boolean update = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		pg = (ProgressBar) findViewById(R.id.pg);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号" + 1.0);
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		CheckUpdate();
		
	}
	
	public void toMain(){
		Intent intent = new Intent();
		if(PreferenceOperator.getContent(SplashActivity.this, "user_id").equals("")){
			intent.setClass(SplashActivity.this, LoginActivity.class);
		}else{
			intent.setClass(SplashActivity.this, MainTabActivity.class);
		}
		startActivity(intent);
		finish();
	}
	
	/**
	 * 检查是否有新版本，如果有就升级
	 */
	private void CheckUpdate (){
	
		new Thread() {
			public void run() {
				Message msg = checkhandler.obtainMessage();
				checkhandler.sendMessage(msg);
				try {
					Thread.sleep(2000);
					toMain();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				update = true;
				checkhandler.sendMessage(new Message());
			}
		}.start();
	}

	
	private Handler checkhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(!update){
				Toast.makeText(getApplicationContext(), "正在检查版本更新...", 100).show();
			}else{
				Toast.makeText(getApplicationContext(), "当前为最新版本，无需更新!", 2000).show();
				pg.setVisibility(View.GONE);
			}
		}
	};
	
	
}
