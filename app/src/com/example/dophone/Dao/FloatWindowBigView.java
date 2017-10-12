package com.example.dophone.Dao;


import com.example.dophone.R;
import com.example.dophone.SplashActivity;
import com.example.dophone.activity.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

	public FloatWindowBigView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Button close = (Button) findViewById(R.id.close);
		Button backToMain = (Button) findViewById(R.id.backToMain);
		Button back = (Button) findViewById(R.id.back);
		backToMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
				WindowManagerDao.removeBigWindow(context);
				WindowManagerDao.removeSmallWindow(context);
				Intent intent = new Intent(getContext(), FloatWindowService.class);
				context.stopService(intent);
				Intent intent1 = new Intent();
				intent1.setClass(context, MainActivity.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent1.setAction(android.content.Intent.ACTION_VIEW);
				intent1.addCategory("android.intent.category.DEFAULT");
				context.startActivity(intent1);
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击返回的时候，移除大悬浮窗，创建小悬浮窗
				WindowManagerDao.removeBigWindow(context);
				WindowManagerDao.createSmallWindow(context);
			}
		});

		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
				WindowManagerDao.removeBigWindow(context);
				WindowManagerDao.removeSmallWindow(context);
				Intent intent = new Intent(getContext(), FloatWindowService.class);
				context.stopService(intent);
			}
		});
	}
}

