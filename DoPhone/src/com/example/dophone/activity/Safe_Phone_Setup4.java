package com.example.dophone.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.dophone.R;

public class Safe_Phone_Setup4 extends BaseSetupActivity {

	private SharedPreferences sp;

	private CheckBox cb_proteting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_safe_phone_setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_proteting = (CheckBox) findViewById(R.id.cb_proteting);

		boolean  protecting = sp.getBoolean("protecting", false);
		if(protecting){
			//手机防盗已经开启了
			cb_proteting.setText("手机防盗已经开启");
			cb_proteting.setChecked(true);
		}else{
			//手机防盗没有开启
			cb_proteting.setText("手机防盗没有开启");
			cb_proteting.setChecked(false);

		}

		cb_proteting.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cb_proteting.setText("手机防盗已经开启");
				}else{
					cb_proteting.setText("手机防盗没有开启");
				}

				//保存选择的状态
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();


			}
		});
	}


	@Override
	public void showNext() {
		Editor editor = sp.edit();
		editor.putBoolean("configed", true);
		editor.commit();

		Intent intent = new Intent(this,Safe_Phone.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

	}

	@Override
	public void showPre() {
		Intent intent = new Intent(this,Safe_Phone_Setup3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);

	}

}
