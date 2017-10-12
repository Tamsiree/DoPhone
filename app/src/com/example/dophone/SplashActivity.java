package com.example.dophone;

import com.example.dophone.util.PreferenceOperator;
import com.example.dophone.util.VonUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private TextView tv_time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		initView();
		CheckUpdate();

	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_time = (TextView) findViewById(R.id.tv_time);
		VonUtil.AuthCode(tv_time, "", 3);
	}

	public void toMain() {
		Intent intent = new Intent();
		if (PreferenceOperator.getContent(SplashActivity.this, "user_id")
				.equals("")) {
			intent.setClass(SplashActivity.this, LoginActivity.class);
		} else {
			intent.setClass(SplashActivity.this, MainTabActivity.class);
		}
		startActivity(intent);
		finish();
	}

	/**
	 * 检查是否有新版本，如果有就升级
	 */
	private void CheckUpdate() {

		new Thread() {
			public void run() {
				Message msg = checkhandler.obtainMessage();
				checkhandler.sendMessage(msg);
				try {
					Thread.sleep(3000);
					toMain();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler checkhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

}
