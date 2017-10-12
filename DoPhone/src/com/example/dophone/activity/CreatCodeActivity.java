package com.example.dophone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dophone.R;
import com.example.dophone.util.VonUtil;

public class CreatCodeActivity extends Activity implements OnClickListener{

	private ImageView iv_code;
	private ImageView iv_linecode;
	private int time_second = 0;
	private TextView tv_time_second;
	private static Handler Handler = new Handler();
	private static Runnable mRunnable = null;
	private LinearLayout ll_refresh;
	private int second = 60;
	private ImageView iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_creat_code);
		// 透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		initView();
		initData();
		AuthCode(tv_time_second,second);
	}

	private void AuthCode(final TextView view,final int time_second) {

		mRunnable = new Runnable() {
			int mSumNum = time_second;

			@Override
			public void run() {
				Handler.postDelayed(mRunnable, 1000);
				view.setText(mSumNum+"");
				view.setEnabled(false);
				mSumNum--;
				if (mSumNum < 0) {
					view.setText(0+"");
					view.setEnabled(true);
					Message message = new Message();
					message.what = 60000;
					mHandler.sendMessage(message);
					// 干掉这个定时器，下次减不会累加
					Handler.removeCallbacks(mRunnable);
					AuthCode(tv_time_second,second);
				}
			}

		};
		Handler.postDelayed(mRunnable, 1000);
	}

	private void initView() {
		ll_refresh = (LinearLayout) findViewById(R.id.ll_refresh);
		tv_time_second = (TextView) findViewById(R.id.tv_time_second);
		iv_code = (ImageView) findViewById(R.id.iv_qrcode);
		iv_linecode = (ImageView) findViewById(R.id.iv_linecode);
		ll_refresh.setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		VonUtil.createQRImage("时间戳:"+System.currentTimeMillis(),600,600,iv_code);
		iv_linecode.setImageBitmap(VonUtil.drawLinecode(this, ""+System.currentTimeMillis(), 1000, 300));
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.ll_back:
				finish();
				break;
			case R.id.ll_refresh:
				Handler.removeCallbacks(mRunnable);
				initData();
				tv_time_second.setText(second+"");
				AuthCode(tv_time_second,second);
				break;
			case R.id.iv_back:
				finish();
				break;
			default:
				break;
		}
	}


	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 60000:
					initData();
					break;
				default:
					break;
			}
		}
	};
}
