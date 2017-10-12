package com.example.dophone.activity;

import com.example.dophone.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




public class Safe_Phone extends Activity {

	private SharedPreferences sp;

	private TextView tv_safenumber;
	private ImageView iv_protecting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//判断一下，是否做过设置向导，如果没有做过，就跳转到设置向导页面去设置，否则就留着当前的页面
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			// 就在手机防盗页面
			setContentView(R.layout.safe_phone);
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
			//得到我们设置的安全号码
			String safenumber = sp.getString("safenumber", "");
			tv_safenumber.setText(safenumber);
			//设置防盗保护的状态
			boolean protecting = sp.getBoolean("protecting", false);
			if(protecting){
				//已经开启防盗保护
				iv_protecting.setImageResource(R.drawable.lock);
			}else{
				//没有开启防盗保护
				iv_protecting.setImageResource(R.drawable.unlock);
			}



		}else{
			//还没有做过设置向导
			Intent intent = new Intent(this,Safe_Phone_Setup1.class);
			startActivity(intent);
			//关闭当前页面
			finish();
		}


	}
	/**
	 * 重新进入手机防盗设置向导页面
	 * @param view
	 */
	public void reEnterSetup(View view){
		Intent intent = new Intent(this,Safe_Phone_Setup1.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}


	//返回主界面
	public void SturnToMain(View view){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}

	public void offS(View view){
		Editor editor = sp.edit();
		editor.remove("password");
		//editor.clear();
		editor.commit();
		Toast.makeText(this, "智能保护关闭成功！", 1000).show();
		//关闭当前页面
		finish();
	}

}

