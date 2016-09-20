package com.example.dophone.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.example.dophone.R;
import com.example.dophone.R.id;
import com.example.dophone.R.layout;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SelectContactActivity extends Activity {

	private ListView list_select_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_select_contact);
		list_select_contact = (ListView) findViewById(R.id.list_select_contact);
		final List<Map<String, String>> data = getContactInfo();
		list_select_contact.setAdapter(new SimpleAdapter(this, data,
				R.layout.contact_item_view, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone }));
		
		list_select_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String phone = data.get(position).get("phone");
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0, data);
				//当前页面关闭掉
				finish();
				
			}
		});
	}
	
	
	/**
	 * 读取手里面的联系人
	 * 
	 * @return
	 */
	private List<Map<String, String>> getContactInfo() {
		
		//把所有的联系人
		List<Map<String, String>> list  = new ArrayList<Map<String,String>>();

		// 得到一个内容解析器
		ContentResolver resolver = getContentResolver();
		// raw_contacts uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);

		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);
			
			if (contact_id != null) {
				//具体的某一个联系人
				Map<String, String> map = new HashMap<String, String>();
				
				Cursor dataCursor = resolver.query(uriData, new String[] {
						"data1", "mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);
				
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					System.out.println("data1=="+data1+"==mimetype=="+mimetype);
					
					if("vnd.android.cursor.item/name".equals(mimetype)){
						//联系人的姓名
						map.put("name", data1);
					}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
						//联系人的电话号码
						map.put("phone", data1);
					}
					
				}
				
				
				list.add(map);
				dataCursor.close();

			}

		}

		cursor.close();
		return list;
	}

}
