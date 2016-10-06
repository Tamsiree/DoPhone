package com.example.dophone.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.example.dophone.Contants;
import com.example.dophone.R;
import com.example.dophone.speech.SpeechUtil;
import com.example.dophone.util.VonUtil;

public class ContactFragment extends Fragment {
	private View v;
	private SearchView searchview1;
	private ImageView iv_mic;

	private WebView web_base;
	private ProgressBar pb_web_base;
	private String webPath;
	private LinearLayout ll_back_web;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_contact, null);
		hideSoftInput();
		initView();
		initData();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		initSearch();
		web_base = (WebView) v.findViewById(R.id.web_base);
		pb_web_base = (ProgressBar) v.findViewById(R.id.pb_web_base);
		ll_back_web = (LinearLayout) v.findViewById(R.id.ll_back_web);
		ll_back_web.setVisibility(View.VISIBLE);
		ll_back_web.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 预留下来的，交给webView自己判断
				if (web_base.canGoBack()) {
					web_base.goBack();
				} else {
					VonUtil.ShowToast(getActivity(), "无法再次返回", false);
				}
			}
		});

		initWebView("");
	}

	private void initSearch() {
		// TODO Auto-generated method stub
		iv_mic = (ImageView) v.findViewById(R.id.iv_mic);
		searchview1 = (SearchView) v.findViewById(R.id.searchView1);
		int searchPlateId1 = searchview1.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View searchPlateView1 = searchview1.findViewById(searchPlateId1);
		if (searchPlateView1 != null) {
			searchPlateView1.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		new SpeechUtil(getActivity(), searchview1, iv_mic);
		searchview1.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				VonUtil.ShowToast(getActivity(), "搜索提交:" + query, false);
				initWebView(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	private void initWebView(String str) {
		// TODO Auto-generated method stub
		pb_web_base.setMax(100);
		webPath = Contants.URL_BAIDU_SEARCH + str;

		if (Build.VERSION.SDK_INT >= 19) {
			web_base.getSettings().setCacheMode(
					WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}

		WebSettings webSettings = web_base.getSettings();
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
		webSettings.setSupportZoom(true);// 设置可以支持缩放
		webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具
		webSettings.setDisplayZoomControls(false);// 隐藏缩放工具
		webSettings.setUseWideViewPort(true);// 扩大比例的缩放
		// 自适应屏幕
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);

		web_base.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				pb_web_base.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}
		});
		web_base.setWebViewClient(new WebViewClient() {
			// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				pb_web_base.setVisibility(View.VISIBLE);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				pb_web_base.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});
		web_base.loadUrl(webPath);
		Log.v("帮助类完整连接", webPath);
	}

	protected void hideSoftInput() {
		View v = getActivity().getCurrentFocus();
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = manager.isActive();
			if (isOpen) {
				manager.hideSoftInputFromWindow(v.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

}
