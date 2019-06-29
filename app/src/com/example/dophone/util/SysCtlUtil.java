package com.example.dophone.util;

import java.io.File;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @see 超级钱包 系统公共控制类
 * @version 2016/1/23
 * @author Vondear
 */
public class SysCtlUtil {

	/**
	 * @param 点击隐藏软键盘
	 * @param activity
	 * @param view
	 */
	public static void onClickHideKeyboard(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * @see 拨打电话
	 * @param activity
	 * @param str
	 */
	public static void CallPhone(Activity activity, String str) {
		str = str.trim();// 删除字符串首部和尾部的空格

		if (str != null && !str.equals("")) {
			// 调用系统的拨号服务实现电话拨打功能
			// 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ str));
			activity.startActivity(intent);// 内部类
		}
	}

	private static Runnable mRunnable = null;
	private static Handler Handler = new Handler();

	/**
	 * @param 60s倒计时
	 * @param view
	 *            :View控件
	 * @param getcode
	 */
	public static void AuthCode(final TextView view, final String getcode) {

		mRunnable = new Runnable() {
			int mSumNum = 60;

			@Override
			public void run() {
				Handler.postDelayed(mRunnable, 1000);
				view.setText("剩余" + mSumNum + " s");
				view.setEnabled(false);
				mSumNum--;
				if (mSumNum < 0) {
					view.setText(getcode);
					view.setEnabled(true);
					// 干掉这个定时器，下次减不会累加
					Handler.removeCallbacks(mRunnable);
				}
			}

		};
		Handler.postDelayed(mRunnable, 1000);
	}

	/**
	 * @param 60s倒计时
	 * @param view
	 *            :View控件
	 */
	public static void AuthCode(final Button view) {

		mRunnable = new Runnable() {
			int mSumNum = 60;

			@Override
			public void run() {
				Handler.postDelayed(mRunnable, 1000);
				view.setText("剩余" + mSumNum + " s");
				view.setEnabled(false);
				mSumNum--;
				if (mSumNum < 0) {
					view.setText("获取验证码");
					view.setEnabled(true);
					// 干掉这个定时器，下次减不会累加
					Handler.removeCallbacks(mRunnable);
				}
			}

		};

		Handler.postDelayed(mRunnable, 1000);
	}



	/**
	 * @param 生成MD5加密32位字符串
	 * @param MStr
	 *            :需要加密的字符串
	 * @return
	 */
	public static String Md5(String MStr) {
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(MStr.getBytes());
			return bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			return String.valueOf(MStr.hashCode());
		}
	}

	// MD5内部算法---------------不能修改!
	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * @param 封装了Toast的方法
	 * @param activity
	 * @param str
	 * @param isLong
	 */
	public static void ShowToast(Activity activity, String str, boolean isLong) {
		if (isLong) {
			Toast.makeText(activity, str, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressWarnings("rawtypes")
	public static String ShowURL(String url, List<String> keylist, Map valueMap) {
		String urls = url + "?";
		for (int i = 0; i < keylist.size(); i++) {
			urls += keylist.get(i).toString() + "="
					+ valueMap.get(keylist.get(i)).toString() + "&";
		}

		return urls;
	}


	/**
	 * 暂未验证功能是否正确
	 *
	 * @param from
	 * @param to
	 * @param source
	 * @return
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer bf = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			bf.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		bf.append(source);
		return bf.toString();
	}

	/**
	 * 隐藏手机中间4位号码
	 *
	 * @param mobile_phone
	 * @return
	 */
	public static String hideMobilePhone(String mobile_phone) {
		return mobile_phone.substring(0, 3) + "****"
				+ mobile_phone.substring(7, 11);
	}

	/**
	 * 判断字符串是否为空 为空即true
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNullString(String str) {
		return (("").equals(str) || str == null || ("null").equals(str));
	}

	public static int StringToInt(String str) {
		if (SysCtlUtil.isNullString(str)) {
			str = "0";
		}
		return Integer.parseInt(str);
	}

	public static double StringToDouble(String str) {
		if (SysCtlUtil.isNullString(str)) {
			str = "0";
		}
		return Double.parseDouble(str);
	}

	/**
	 * 将字符串格式化为带两位小数的字符串
	 *
	 * @param str
	 * @return
	 */
	public static String format2Decimals(String str) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (df.format(StringToDouble(str)).startsWith(".")) {
			return "0" + df.format(StringToDouble(str));
		} else {
			return df.format(StringToDouble(str));
		}
	}

	/**
	 * 判断是否为真实手机号
	 *
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}

	/**
	 * 验证银卡卡号
	 *
	 * @param cardNo
	 * @return
	 */
	public static boolean isBankCard(String cardNo) {
		Pattern p = Pattern
				.compile("^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$");
		Matcher m = p.matcher(cardNo);

		return m.matches();
	}

	/**
	 * 15位和18位身份证号码的正则表达式 身份证验证
	 *
	 * @param idCard
	 * @return
	 */
	public static boolean validateIdCard(String idCard) {
		// 15位和18位身份证号码的正则表达式
		String regIdCard = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
		Pattern p = Pattern.compile(regIdCard);
		return p.matcher(idCard).matches();
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhMMss");
		return sdf.format(new Date());
	}

	/**
	 * 获取当前日期
	 *
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 格式化银行卡 加* 3749 **** **** 330
	 *
	 * @param cardNo
	 * @return
	 */
	public static String formatCard(String cardNo) {
		String card = "";
		card = cardNo.substring(0, 4) + " **** **** ";
		card += cardNo.substring(cardNo.length() - 4);
		return card;
	}

	/**
	 * 银行卡后四位
	 *
	 * @param cardNo
	 * @return
	 */
	public static String formatCardEndFour(String cardNo) {
		String card = "";
		card += cardNo.substring(cardNo.length() - 4);
		return card;
	}

	/**
	 * 获取本应用图片缓存目录
	 *
	 * @param activity
	 * @return
	 */
	public static File getCecheFolder(Activity activity) {
		File folder = new File(activity.getCacheDir(), "IMAGECACHE");
		if (!folder.exists()) {
			folder.mkdir();
		}
		return folder;
	}

	/**
	 * 获取本应用图片缓存目录
	 *
	 * @param activity
	 * @return
	 */
	public static File getCecheFolder(Context context) {
		File folder = new File(context.getCacheDir(), "IMAGECACHE");
		if (!folder.exists()) {
			folder.mkdir();
		}
		return folder;
	}

	public static void ShowToast(Context cxt, String str, boolean isLong) {
		if (isLong) {
			Toast.makeText(cxt, str, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(cxt, str, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 时间戳转换 String格式为：2016年01月
	 *
	 * @param times
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimes(String times) {
		String time = "";
		try {
			Date epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
					.parse("01/" + times.toString().substring(5, 7) + "/"
							+ times.toString().substring(0, 4) + " 01:00:00");
			time = epoch.getTime() / 1000 + "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}




	/**
	 * 手动计算出listView的高度，但是不再具有滚动效果
	 * @param listView
	 */
	public static void fixListViewHeight(ListView listView) {

		// 如果没有设置数据适配器，则ListView没有子项，返回。

		ListAdapter listAdapter = listView.getAdapter();

		int totalHeight = 0;

		if (listAdapter == null) {

			return;

		}

		for (int index = 0, len = listAdapter.getCount(); index < len; index++) {

			View listViewItem = listAdapter.getView(index , null, listView);

			// 计算子项View 的宽高

			listViewItem.measure(0, 0);

			// 计算所有子项的高度和

			totalHeight += listViewItem.getMeasuredHeight();

		}



		ViewGroup.LayoutParams params = listView.getLayoutParams();

		// listView.getDividerHeight()获取子项间分隔符的高度

		// params.height设置ListView完全显示需要的高度

		params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);

	}

	/**
	 * 获取手机唯一标识序列号
	 * @param context
	 * @return
	 */
	public static String getSerialNumber(Context context){

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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
		String Android_id = Secure.getString(context.getContentResolver(),
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
		return manuFacturer + "-" + phoneName + "-" + getSerialNumber();
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
}
