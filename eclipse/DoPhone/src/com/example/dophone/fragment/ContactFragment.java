package com.example.dophone.fragment;

import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dophone.Contants;
import com.example.dophone.EditContactActivity;
import com.example.dophone.R;
import com.example.dophone.SmsActivity;
import com.example.dophone.Dao.ContactManager;
import com.example.dophone.Dao.UserContactDao;
import com.example.dophone.adapter.ContactAdapter;
import com.example.dophone.bean.ActionItem;
import com.example.dophone.ui.TitlePopup;
import com.example.dophone.ui.TitlePopup.OnItemOnClickListener;
import com.example.pinyinlib.AssortView;
import com.example.pinyinlib.AssortView.OnTouchAssortListener;
import com.example.pinyinlib.PinyingBean;

public class ContactFragment extends Fragment {
	ContactManager contactManager =new ContactManager();
	ContentResolver cr;
	ExpandableListView elv;
	SearchView searchView;
	ViewPager vp;
	List<View> listViews;
	AssortView assortView;
	ContactAdapter adapter;
	
	private View v;
	private TextView tv_title;
	private LinearLayout ll_menu;
	private ImageView iv_menu;
	private TitlePopup titlePopup;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cr = getActivity().getContentResolver();
		v = inflater.inflate(R.layout.fragment_contact, null);
		initView();
		createContactList(contactManager.queryContactAll(cr));
		initData();
		return v;
	}


	private void initView() {
		// TODO Auto-generated method stub
		elv = (ExpandableListView)v.findViewById(R.id.expandableListView1);
		tv_title = (TextView) v.findViewById(R.id.tv_title);
		tv_title.setText("联系人");
	}
	private void initData() {
		// TODO Auto-generated method stub
		assortView = (AssortView) v.findViewById(R.id.assort);
		
		//assortView.setTextSize(12f);
		// 字母按键回调
		assortView.setOnTouchAssortListener(new OnTouchAssortListener() {

			View layoutView = LayoutInflater.from(getActivity()).inflate(
					R.layout.alter_zimu, null);
			TextView text = (TextView) layoutView.findViewById(R.id.content);
			PopupWindow popupWindow;

			public void onTouchAssortListener(String str) {
				int index = adapter.getAssort().getHashList().indexOfKey(str);
				if (index != -1) {
					elv.setSelectedGroup(index);
				}
				if (popupWindow != null) {
					text.setText(str);
				} else {
					popupWindow = new PopupWindow(layoutView, 200, 200, false);
					// 显示在Activity的根视图中心
					popupWindow.showAtLocation(getActivity().getWindow().getDecorView(),Gravity.CENTER, 0, 0);
				}
				text.setText(str);
			}

			public void onTouchAssortUP() {
				if (popupWindow != null)
					popupWindow.dismiss();
				popupWindow = null;
			}
		});
		elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					final int groupPosition, final int childPosition, final long id) {
				// TODO Auto-generated method stub
				Builder builder = new Builder(getActivity());
				
				builder.setItems(new String[]{"编辑联系人资料","呼叫联系人","给联系人发送短信"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String contact_tel = contactManager.getOneContactById((int)id,cr).getContact_tel();
						switch (which) {
						case 0:
							Intent intent = new Intent();
							intent.putExtra(UserContactDao.COL_ID, (int)id);
							intent.putExtra(Contants.TYPE, Contants.TYPE_EDIT);
							intent.setClass(getActivity(), EditContactActivity.class);
							startActivityForResult(intent,Contants.TYPE_EDIT);
							break;
						case 1:
							Intent call = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+contact_tel));
						    startActivity(call);
							break;
						case 2:
							Intent intent1 = new Intent();
							intent1.putExtra(UserContactDao.COL_ID, (int)id);
							intent1.setClass(getActivity(), SmsActivity.class);
							startActivityForResult(intent1,Contants.TYPE_SMS);	
							break;
						default:
							break;
						}
					}
				});
				
				builder.show();
				
				return false;
			}
		});
		
		
		searchView = (SearchView) v.findViewById(R.id.searchView1);
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				// 刷新界面
				createContactList(contactManager.getContantsByName(query,cr));

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				createContactList(contactManager.getContantsByName(newText,cr));

				return false;
			}
		});
		
		initTitleMenu();
	}
	
	private void initTitleMenu() {
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		iv_menu = (ImageView) v.findViewById(R.id.iv_menu);
		iv_menu.setImageResource(R.drawable.menu_add);
		ll_menu = (LinearLayout) v.findViewById(R.id.ll_menu);
		ll_menu.setVisibility(View.VISIBLE);
		ll_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				titlePopup.show(arg0);
			}
		});
		titlePopup.addAction(new ActionItem(getActivity(), "同步系统联系人",
				R.drawable.menu_add));
		titlePopup.addAction(new ActionItem(getActivity(), "添加联系人",
				R.drawable.menu_add));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {

			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (position) {
				case 0:
					try {
						contactManager.syncSystemContact(cr);
						createContactList(contactManager.queryContactAll(cr));
						Toast.makeText(getActivity(), "系统联系人同步成功", 0).show();
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), "系统联系人同步失败", 0).show();
					}
					break;
				case 1:
					intent.putExtra(Contants.TYPE, Contants.TYPE_NEW);
					intent.setClass(getActivity(), EditContactActivity.class);
					startActivityForResult(intent, Contants.TYPE_NEW);
					searchView.setIconified(true);// 设置
					break;
				default:
					break;
				}
			}
		});
	}
	

	public void createContactList(List<PinyingBean> list){
		
		//创建适配器
		adapter = new ContactAdapter(getActivity(), list);
		
		//绑定适配器
		elv.setAdapter(adapter);
		
		//要让expandlistview自动展开
		for(int i=0;i<adapter.getGroupCount();i++)
		{
			elv.expandGroup(i);
		}
		
	}
	
	public void getAllContactList(){
		createContactList(contactManager.getContantsByName("", cr));
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != getActivity().RESULT_OK) {
			return;
		}
		createContactList(contactManager.queryContactAll(cr));
		super.onActivityResult(requestCode, resultCode, data);
		searchView.clearFocus();
		
	}
}
