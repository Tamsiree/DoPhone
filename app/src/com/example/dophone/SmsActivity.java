package com.example.dophone;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dophone.R;
import com.example.dophone.Dao.ContactManager;
import com.example.dophone.Dao.UserContactDao;
import com.example.dophone.bean.ContactEntity;

public class SmsActivity extends Activity {
	EditText ed_sms;
	TextView tv_sms_tel;
	ContentResolver cr;
	ContactManager contactManager;
	VonRecevier vReceiver;
	VonRecevier vdReceiver;
	int contact_id;
	public static final String action1 = "com.tanyaoxiang.sms";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		cr = getContentResolver();
		contactManager = new ContactManager();
		vReceiver = new VonRecevier();
		registerReceiver(vReceiver, new IntentFilter("com.broadcast.send"));
		vdReceiver = new VonRecevier();
		registerReceiver(vdReceiver, new IntentFilter("com.broadcast.deli"));
		findView();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		contact_id = getIntent().getIntExtra(UserContactDao.COL_ID, -1);
		if (contact_id > 0) {
			ContactEntity ce = contactManager.getOneContactById(contact_id,cr);
			tv_sms_tel.setText(ce.getContact_tel());
		}
	}

	public void findView(){
		ed_sms = (EditText) findViewById(R.id.ed_sms);
		tv_sms_tel = (TextView) findViewById(R.id.tv_sms_tel);
	}


	public void submit(View v){
		SmsManager smsManager = SmsManager.getDefault();
		Intent sentIntent  = new Intent();
		sentIntent.setAction("com.broadcast.send");
		sentIntent.putExtra("content", "短信发送成功");
		PendingIntent sentPI = PendingIntent.getBroadcast(
				this,
				1,//广播的请求码
				sentIntent,
				0);
		Intent deliIntent  = new Intent();
		sentIntent.setAction("com.broadcast.deli");
		sentIntent.putExtra("content", "对方成功收到短信");
		PendingIntent deliPI = PendingIntent.getBroadcast(
				this,
				1,//广播的请求码
				deliIntent,
				0);
		String tel = tv_sms_tel.getText().toString();
		smsManager.sendTextMessage(tel,//短信的发送目标号码
				null,  //短信中心号码
				ed_sms.getText().toString(),//短信内容
				sentPI,//短信是否发送成功的意图
				deliPI); //短信是否被对方收到的意图
		finish();
	}


	public void cancel(View v){
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(vReceiver);
		super.onDestroy();
	}

}

class VonRecevier extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, intent.getStringExtra("content"), 0).show();
	}
}
