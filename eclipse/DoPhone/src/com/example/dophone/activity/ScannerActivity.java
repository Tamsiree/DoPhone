package com.example.dophone.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import com.example.dophone.R;
import com.example.dophone.Dao.AntivirusDao;
import com.example.dophone.R.id;
import com.example.dophone.R.layout;
import com.example.dophone.R.menu;
import com.example.dophone.R.raw;
import com.example.dophone.bean.AppInfo;
import com.example.dophone.bean.ScanInfo;
import com.example.dophone.bean.VirusInfo;
import com.example.dophone.provider.AppInfoProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ScannerActivity extends Activity {

	ImageView iv_show;
	Button bt_yes,bt_no,btn_showvirus;
	TextView tv_scan_status;
	boolean btn_start = true;
	boolean btn_flag = false;
	RotateAnimation ra;
	int exeFlag = -1;
	ProgressBar progressBar1;
	LinearLayout virus_sv_content;
	List<AppInfo> listAppInfos;
	List<VirusInfo> listVirusInfos;
	ArrayList<ScanInfo> list_virus = new ArrayList<ScanInfo>(); 
	protected static final int SCANING = 0;
	protected static final int FINISH = 1;
	Object obj = new Object();
	MyThread myThread;
	PackageManager pm;
/*	BadgeView badge;*/
	
	
	int posion ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scanner);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getDB();
		findView();
		initView();
		queryVirus();
		queryApp();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		
		bt_yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_start){
					exeFlag = -1;
					progressBar1.setProgress(0);
					virus_sv_content.removeAllViews();
					iv_show.startAnimation(ra);
					scanVirus();
					list_virus.clear();
				    btn_start = false;
				    bt_yes.setText("终止");
				    bt_no.setEnabled(true);
				    btn_showvirus.setVisibility(View.INVISIBLE);
			    	btn_showvirus.setEnabled(false);
				}else{
					bt_no.setEnabled(false);
					exeFlag = 1;
					btn_start = true;
					iv_show.clearAnimation();
					bt_yes.setText("开始");
				}
			}
		});
		
		bt_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_flag){ //继续
					btn_flag = false;
					exeFlag = -1;
					iv_show.startAnimation(ra);
					new Thread(){
						public void run() {
							synchronized (obj) {
								System.out.println("goOn");
								obj.notifyAll();
							}
						};
					}.start();
					bt_no.setText("暂停");
				}else{ //暂停
					exeFlag = 0;
					iv_show.clearAnimation();
					btn_flag = true;
					bt_no.setText("继续");
				}
			}
		});
	}

	private void findView() {
		// TODO Auto-generated method stub
		iv_show = (ImageView) findViewById(R.id.iv_act_scanning);
		bt_yes = (Button) findViewById(R.id.bt_stop);
		bt_no = (Button) findViewById(R.id.bt_pasuse);
		progressBar1 = (ProgressBar)findViewById(R.id.virus_pb);
		ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
	    ra.setDuration(400);
	    ra.setRepeatCount(Animation.INFINITE);
	    tv_scan_status = (TextView) findViewById(R.id.tv_process_name);
	    btn_showvirus = (Button)findViewById(R.id.btn_showvirus);
	    virus_sv_content = (LinearLayout)findViewById(R.id.virus_sv_content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scanner, menu);
		return true;
	}
	
	private void queryApp() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run(){
				listAppInfos = new ArrayList<AppInfo>();
				listAppInfos = AppInfoProvider.getAppInfoMd5(getApplicationContext());
				for (AppInfo appinfo : listAppInfos) {
					appinfo.md5 = getFileMd5(appinfo.sourceDir);
				}
			}
		}.start();
	}
	
	private void queryVirus() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run(){
				listVirusInfos = AntivirusDao.queryAll();
			}
		}.start();
	}
	
	
	class MyThread extends Thread{
		public void run(){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			progressBar1.setMax(listAppInfos.size());
			int progress = 0;
			
			//System.out.println("--listVirusInfos.size()=="+listVirusInfos.size());
			
			for (AppInfo appInfo : listAppInfos) {
			    ScanInfo scaninfo = new ScanInfo();
				scaninfo.setName( appInfo.getName());
				scaninfo.setPackname(appInfo.getPackname());
				//查询md5信息，验证是否存在病毒数据库
				
				if(checkVirus(appInfo.md5)){
					//发现病毒
					scaninfo.setVirus(1);
					list_virus.add(scaninfo);
				}else{
					//扫描安全
					scaninfo.setVirus(0);
				}
				Message msg = Message.obtain();
				msg.obj = scaninfo;
				msg.what = SCANING;
				handler.sendMessage(msg);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (exeFlag==0) {
					synchronized (obj) {
						try {
							System.out.println("wait");
							obj.wait();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else if (exeFlag == 1) {
					myThread.interrupt();
					myThread= null;
					progressBar1.setProgress(listAppInfos.size()-1);
					Message msg2 = Message.obtain();
					msg2.what = FINISH;
					handler.sendMessage(msg2);
					return;
				}
				
				progress++;
				progressBar1.setProgress(progress);
			}
			Message msg = Message.obtain();
			msg.what = FINISH;
			handler.sendMessage(msg);
		}
	}
	
	public boolean checkVirus(String md5){
		for (VirusInfo virus : listVirusInfos) {
			if(virus.getMd5().equals(md5)){
				System.out.println("===checkVirus=="+virus.getMd5());
				return true;
			}
		}
		return false;
	}
	
	private void scanVirus() {
		// TODO Auto-generated method stub
		pm = getPackageManager();
		tv_scan_status.setText("正在初始化4核杀毒引擎...");
		myThread = new MyThread();
		myThread.start();
	}
	
	private String getFileMd5(String path){
		StringBuffer sb= new StringBuffer();
		sb.append("");
		try {
			//获取文件的md5值
			File file = new File(path);
			//md5
			MessageDigest digest = MessageDigest.getInstance("sha1");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = fis.read(buffer)) != -1){
				digest.update(buffer, 0 ,len);
			}
			byte[] result = digest.digest();
			
			for (byte b : result) {
				//与运算 
				int number = b & 0xff;//
				String str = Integer.toHexString(number);
				if(str.length() == 1){
					sb.append("0");
				}
				sb.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	
	public void getDB(){
		String path = "/data/data/com.example.dophone/databases";
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdir();
		}
		File dbfile = new File(path,"antivirus.db");
		if(!dbfile.exists()){
			try {
				dbfile.createNewFile();
				InputStream is = getResources().openRawResource(R.raw.antivirus);
				FileOutputStream fos = new FileOutputStream(dbfile);
				int len = -1;
				byte [] buffer = new byte[1024];
				while((len=is.read(buffer))!=-1){
					fos.write(buffer);
				}
				fos.close();
				is.close();
				Toast.makeText(this, "病毒数据库导入成功", 0).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, "病毒数据库导入失败", 0).show();
			}
		}else{
			Toast.makeText(this, "病毒数据库已存在,导入失败", 0).show();
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				ScanInfo scanindo = (ScanInfo) msg.obj;
				tv_scan_status.setText("正在扫描："+scanindo.getName());
				TextView tv = new TextView(getApplicationContext());
				tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				if(list_virus.size() > 0 ){
					//存在病毒
					/*if(badge.isShown()){
						badge.setText(list_virus.size()+"");
					}else{
						badge.setText(list_virus.size()+"");
						badge.show();
						btn_showvirus.setVisibility(View.VISIBLE);
						btn_showvirus.setEnabled(true);
					}*/
				}
				if(scanindo.isVirus() == 1){
					tv.setTextColor(Color.RED);
					tv.setText("发现病毒："+scanindo.getName());
				}else{
					if(posion%2 == 0)
					{
						tv.setTextColor(Color.DKGRAY);
					}else{
						tv.setTextColor(Color.GRAY);
					}
					tv.setText("扫描安全："+scanindo.getName());
					posion++;
				}
				virus_sv_content.addView(tv,0);
				break;
			case FINISH:
				tv_scan_status.setText("扫描完毕");
				btn_start = true;
				iv_show.clearAnimation();
				bt_yes.setText("开始");
				break;
			default:
				break;
			}
		};
	};
}
