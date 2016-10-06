package com.example.dophone.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


/**
 * 超级钱包 系统公共控制类
 * @version 2016/1/23
 * @author 谭耀湘
 */
public class VonUtil {

	/**
	 * 点击隐藏软键盘
	 * @param activity
	 * @param view
	 */
	public static void onClickHideKeyboard(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 拨打电话
	 * @param context
	 * @param str
	 */
	public static void CallPhone(Context context, String str) {
		str = str.trim();// 删除字符串首部和尾部的空格
		if (str != null && !str.equals("")) {
			// 调用系统的拨号服务实现电话拨打功能
			// 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ str));
           /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }*/
			context.startActivity(intent);// 内部类
		}
	}

	private static Runnable mRunnable = null;
	private static Handler Handler = new Handler();

	/**
	 * 60s倒计时
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
				view.setText("剩余 " + mSumNum + " s");
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
	 *  60s倒计时
	 * @param view
	 *            :View控件
	 * @param getcode
	 */
	public static void AuthCode(final TextView view, final String getcode,
								final int time_second) {

		mRunnable = new Runnable() {
			int mSumNum = time_second;

			@Override
			public void run() {
				Handler.postDelayed(mRunnable, 1000);
				view.setText(mSumNum + " s");
				view.setEnabled(false);
				mSumNum--;
				if (mSumNum < -1) {
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
	 * 60s倒计时
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
	 * 生成MD5加密32位字符串
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

	// MD5内部算法---------------不能修改!!!
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
	 * 封装了Toast的方法
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


	/**
	 *	此方法用于线程内更新UI
	 *
	 */
	public static void updateUI(final Activity activity,Handler mHandler,final View v,final String str){
		Runnable runnableUI = new Runnable() {
			public void run() {
				if(v == null){
					VonUtil.ShowToast(activity, str, false);
				}else if(v instanceof TextView){
					((TextView) v).setText(str);
				}
			}
		};
		mHandler.post(runnableUI);
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

	/**
	 * 将字符串转换成int，如果字符串为空则返回0
	 * @param str
	 * @return
	 */
	public static int StringToInt(String str) {
		if (VonUtil.isNullString(str)) {
			str = "0";
		}
		return Integer.parseInt(str);
	}

	/**
	 * 将字符串转换成double，如果字符串为空则返回0
	 * @param str
	 * @return
	 */
	public static double StringToDouble(String str) {
		if (VonUtil.isNullString(str)) {
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
	 * yyyyMMddhhMMss
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhMMss");
		return sdf.format(new Date());
	}

	/**
	 * 获取当前日期
	 * yyyyMMdd
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
	 * @param context
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
	 * 时间戳转换 2016年01月01日 转换成时间戳
	 *
	 * @param times
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimes(String times) {
		String time = "";
		try {
			Date epoch = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
					.parse(times.toString().substring(8, 10) + "/"
							+ times.toString().substring(5, 7) + "/"
							+ times.toString().substring(0, 4) + " 01:00:00");
			time = epoch.getTime() / 1000 + "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 时间戳装换成日期：dd/MM/yyyy HH:mm:ss
	 *
	 * @param times
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDate(String times) {
		String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(new Date(VonUtil.StringToInt(times) * 1000));
		return date;
	}

	/**
	 *
	 * @param url
	 *            需要转换的字符串
	 * @param QR_WIDTH
	 *            二维码的宽度
	 * @param QR_HEIGHT
	 *            二维码的高度
	 * @param iv_code
	 *            图片空间
	 */
	public static void createQRImage(String url, int QR_WIDTH, int QR_HEIGHT,
									 ImageView iv_code) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 显示到一个ImageView上面
			iv_code.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成条形码
	 *
	 * @param context
	 * @param contents
	 *            需要生成的内容
	 * @param desiredWidth
	 *            生成条形码的宽带
	 * @param desiredHeight
	 *            生成条形码的高度
	 * @return
	 */
	public static Bitmap drawLinecode(Context context, String contents,
									  int desiredWidth, int desiredHeight) {
		Bitmap ruseltBitmap = null;
		/**
		 * 条形码的编码类型
		 */
		BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
		final int WHITE = 0xFFFFFFFF;
		final int BLACK = 0xFF000000;

		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = null;
		try {
			result = writer.encode(contents, barcodeFormat, desiredWidth,
					desiredHeight, null);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		ruseltBitmap = bitmap;
		return ruseltBitmap;
	}

	/**
	 * 手动计算出listView的高度，但是不再具有滚动效果
	 *
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

			View listViewItem = listAdapter.getView(index, null, listView);

			// 计算子项View 的宽高

			listViewItem.measure(0, 0);

			// 计算所有子项的高度和

			totalHeight += listViewItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		// listView.getDividerHeight()获取子项间分隔符的高度

		// params.height设置ListView完全显示需要的高度

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);

	}

	/**
	 * 获取手机唯一标识序列号
	 *
	 * @param context
	 * @return
	 */
	public static String getSerialNumber(Context context) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
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
		Log.d("info===================", sb.toString());
		Log.d("info===================", Android_id.toString());
		Log.d("info===================", phoneName.toString());
		Log.d("info===================", phoneBrand.toString());
		Log.d("info===================", manuFacturer.toString());
		Log.v("getSerialNumber()", getSerialNumber());
		System.out.println(manuFacturer + "-" + phoneName + "-"
				+ getSerialNumber());
		Log.d("info==================1", manuFacturer + "-" + phoneName + "-"
				+ getSerialNumber());
		return manuFacturer + "-" + phoneName + "-" + getSerialNumber();
	}

	/**
	 * 序列号
	 *
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
	 * 获取当月的 天数
	 * */
	public static int getCurrentMonthDay() {
		// calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 根据年 月 获取对应的月份 天数
	 * */
	public static int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 根据日期 找到对应日期的 星期
	 */
	public static String getDayOfWeekByDate(String date) {
		String dayOfweek = "-1";
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date myDate = myFormatter.parse(date);
			SimpleDateFormat formatter = new SimpleDateFormat("E");
			String str = formatter.format(myDate);
			dayOfweek = str;

		} catch (Exception e) {
			System.out.println("错误!");
		}
		return dayOfweek;
	}

	public static boolean isNumeric(String str){
		if(str == null){
			return false;
		}
		for (int i = str.length();--i>=0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}

	/**
	 * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
	 *
	 * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
	 *
	 * B.本地路径:url="file://mnt/sdcard/photo/image.png";
	 *
	 * C.支持的图片格式 ,png, jpg,bmp,gif等等
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap GetLocalOrNetBitmap(String url)
	{
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try
		{
			in = new BufferedInputStream(new URL(url).openStream(), 1024);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, 1024);
			copy(in, out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[1024];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}
}

