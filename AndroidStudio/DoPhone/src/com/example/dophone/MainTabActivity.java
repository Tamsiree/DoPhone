package com.example.dophone;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dophone.R;
import com.example.dophone.fragment.ContactFragment;
import com.example.dophone.fragment.MainFragment;
import com.example.dophone.fragment.SurroundFragment;
import com.example.dophone.fragment.UserFragment;

/**
 * @author yangyu 功能描述：自定义TabHost
 */
public class MainTabActivity extends FragmentActivity {
	// 定義gridview
	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;

	// 定义一个布局
	private LayoutInflater layoutInflater;
	// 定义数组来存放Fragment界面
	private Class fragmentArray[] = { MainFragment.class, ContactFragment.class,
			SurroundFragment.class, UserFragment.class };

	// 定义数组来存放按钮图片
	// private int mImageViewArray[] =
	// {R.drawable.tab_home_btn,R.drawable.tab_message_btn,R.drawable.tab_selfinfo_btn,
	// R.drawable.tab_square_btn,R.drawable.tab_more_btn};

	private int mImageViewArray[] = { R.drawable.tab_one_btn,
			R.drawable.tab_two_btn, R.drawable.tab_three_btn,
			R.drawable.tab_four_btn };

	// Tab选项卡的文字
	private String mTextviewArray[] = { "功能", "联系", "杀毒", "个人中心", };
	private TelephonyManager tm;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_tab);
		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		initView();

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();
		sb.append("\nDeviceId(IMEI) =" + tm.getDeviceId());
		// sb.append("\nDeviceSoftwareVersion = " +
		// tm.getDeviceSoftwareVersion());
		// sb.append("\nLine1Number = " + tm.getLine1Number());
		// sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
		// sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
		sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
		// sb.append("\nNetworkType = " + tm.getNetworkType());
		// sb.append("\nPhoneType = " + tm.getPhoneType());
		// sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
		// sb.append("\nSimOperator = " + tm.getSimOperator());
		// sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
		sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
		// sb.append("\nSimState = " + tm.getSimState());
		sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
		// sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
		String Android_id = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);//
		String phoneName = Build.MODEL;// Galaxy nexus 品牌类型
		String phoneBrand = Build.BRAND;// google
		String manuFacturer = Build.MANUFACTURER;// samsung 品牌
		Log.d("info====================", sb.toString());
		Log.d("info====================", Android_id.toString());
		Log.d("info====================", phoneName.toString());
		Log.d("info====================", phoneBrand.toString());
		Log.d("info====================", manuFacturer.toString());
		Log.v("getSerialNumber()", getSerialNumber());
		System.out.println(manuFacturer + "-" + phoneName + "-" + getSerialNumber());
	}

	/**
	 * 序列号
	 * @return
	 */
	public static String getSerialNumber() {

		String serial = null;

		try {

			Class<?> c = Class.forName("android.os.SystemProperties");

			Method get = c.getMethod("get", String.class);

			serial = (String) get.invoke(c, "ro.serialno");

		} catch (Exception e) {

			e.printStackTrace();

		}

		return serial;

	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 实例化布局对象
		layoutInflater = LayoutInflater.from(this);

		// 实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		// 得到fragment的个数
		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景
			// mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		}
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.item_tab_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);

		return view;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	private long before = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				long curruttime = System.currentTimeMillis();
				if (curruttime - before < 2000) {
				} else {
					Toast.makeText(this, "再次点击返回退出", 0).show();
					before = curruttime;
					return true;
				}
				break;
		}
		// return true;
		return super.onKeyDown(keyCode, event);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// getMenuInflater().inflate(R.menu.main, menu);
	// PhoneInfo siminfo=new PhoneInfo(MainTabActivity.this);
	//
	// //
	// System.out.println("\ngetprovider==================:"+siminfo.getProvidersName());
	// //
	// System.out.println("\ngeNativePhoneNumber==================:"+siminfo.getNativePhoneNumber());
	// System.out.println("--------w---x---------");
	// System.out.println("\ngetphoneinfo****************************:"+siminfo.getPhoneInfo());
	// System.out.println("\ngetphoneinfo****************************:"+siminfo.PhoneInfo(this));
	// return true;
	//
	//
	// }
}

