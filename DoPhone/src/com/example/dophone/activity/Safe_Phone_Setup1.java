package com.example.dophone.activity;

import com.example.dophone.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;




public class Safe_Phone_Setup1 extends BaseSetupActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_safe_phone_setup1);


	}




	@Override
	public void showNext() {
		Intent intent = new Intent(this,Safe_Phone_Setup2.class);
		startActivity(intent);
		finish();
		//要求在finish()或者startActivity(intent);后面执行；
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

	}



	@Override
	public void showPre() {
		// TODO Auto-generated method stub

	}



}

