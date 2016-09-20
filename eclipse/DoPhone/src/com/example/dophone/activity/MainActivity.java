package com.example.dophone.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.dophone.FunctionUsageActivity;
import com.example.dophone.R;
import com.example.dophone.Dao.WindowManagerDao;
import com.example.dophone.adapter.GridViewAdapter;
import com.example.dophone.ui.Safe_UI;



public class MainActivity extends Activity {
	GridView gv_home;
	List<String> sList = new ArrayList<String>();
	//String[] downApk = {"http://kuai.xunlei.com/d/4PlcGuqFqPX7VQQA7f7"};
	//Resources.openRawResource();
	String[] packageApk = {	"com.example.filemanager",
							"com.example.dictionary",
							"com.tanyaoxiang.chinese_knot",
							"com.example.softmanager",
							"com.example.progressmanager"
							};
	public static String[] name = {	
						"文件管理器",
						"电子词典",
						"中国结",
						"App分析",
						"内存诊断",
						"大扫除",
						"智能保护",
						"功能分析",
						"敬请期待"
						};
	public static int [] appUseCount = {
		1,
		1,
		1,
		1,
		1,
		1,
		1,
		1
	};
	private String[] appName = {"filemanager.apk",
						"dictionary.apk",
						"chineseknot.apk",
						"softmanager.apk",
						"progressmanager.apk"
	};
	private int[] rawIds = {
					R.raw.filemanager,
					R.raw.dictionary,
					R.raw.chineseknot,
					R.raw.softmanager,
					R.raw.progressmanager
	
	};
	
	public static final int[] appIcon = {
					R.drawable.app_filemanager,
					R.drawable.app_dictionary,
					R.drawable.app_chineseknot,
					R.drawable.app_softmanager,
					R.drawable.app_progress,
					R.drawable.app_viurs,
					R.drawable.ic_launcher,
					R.drawable.safe_phone,
					R.drawable.ico_ijobs_wkstu
			
	};
	
	private SharedPreferences sp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		findView();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		GridViewAdapter ga = new GridViewAdapter(this,sList);
		gv_home.setAdapter(ga);
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 5:
					appUseCount[position]++;
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ScannerActivity.class);
					startActivity(intent);
					break;
				case 6:
					appUseCount[position]++;
					if(isSetupPwd()){
						//已经设置密码了，弹出的是输入对话框
						new Safe_UI(sp).showEnterDialog(MainActivity.this);
					}else{
						//没有设置密码，弹出的是设置密码对话框
						new Safe_UI(sp).showSetupPwdDialog(MainActivity.this);
					}
					break;
				case 7:
					appUseCount[position]++;
					functionMap(null);
					break;
				case 8:
					Toast.makeText(MainActivity.this, "更多功能，敬请期待!", 0).show();
					break;
				default:
					appUseCount[position]++;
					openApp(position);
					break;
				}
			}
		});
	}

	private void findView() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences("config", MODE_PRIVATE);
		gv_home = (GridView) findViewById(R.id.list_home);
		for (int i = 0; i < name.length; i++) {
			sList.add(name[i]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WindowManagerDao.createSmallWindow(getApplicationContext());
		super.onDestroy();
	}
	
	public void openApp(final int position){
		Intent intent1 = new Intent();
		if (isAvilible(MainActivity.this, packageApk[position])) {
			intent1 = getPackageManager().getLaunchIntentForPackage(packageApk[position]);
			startActivity(intent1);
		}else{
			File file = new File(this.getFilesDir(),appName[position]);
			if(file.exists()){
				String command = "chmod " + 777 + " " + file.getPath();
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec(command);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
				intent.addCategory("android.intent.category.DEFAULT");  
				startActivity(intent);
				
				/*Intent intent = new Intent(Intent.ACTION_VIEW);  
		        intent.setDataAndType(Uri.fromFile(file),  
		                "application/vnd.android.package-archive");  
		        startActivity(intent); */ 
			}else{
				Builder builder = new Builder(this);
				builder.setTitle(appName[position]+"下载提示:");
				builder.setMessage("是否下载");
				builder.setNegativeButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getApk(appName[position],rawIds[position]);
					}
				});
				
				builder.setPositiveButton("取消", null);
				builder.show();
			}
			
			
			
		}
	}
	
	private boolean isAvilible(Context context,String packageName){
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
	
	
	public void getApk(String name,int rawId){
		File dir = this.getFilesDir();
		if(!dir.exists()){
			dir.mkdir();
		}
		File dbfile = new File(dir,name);
		if(!dbfile.exists()){
			try {
				dbfile.createNewFile();
				InputStream is = getResources().openRawResource(rawId);
				FileOutputStream fos = new FileOutputStream(dbfile);
				int len = -1;
				byte [] buffer = new byte[1024];
				while((len=is.read(buffer))!=-1){
					fos.write(buffer, 0, len); 
				}
				fos.close();
				is.close();
				Toast.makeText(this, name+"下载成功,再次点击安装", 0).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, name+"下载失败", 0).show();
			}
		}else{
			Toast.makeText(this, name+"已存在,下载失败", 0).show();
		}
	}
	
	/**
	 * 判断是否设置过密码
	 * @return
	 */
	private boolean isSetupPwd(){
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
		
	}
	
	public void functionMap(View v){
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, FunctionUsageActivity.class);
		startActivity(intent);
	}
	

}
