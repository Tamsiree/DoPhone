package com.example.dophone.adapter;

import java.util.List;

import com.example.dophone.MainTabActivity;
import com.example.dophone.R;
import com.example.dophone.activity.MainActivity;
import com.example.dophone.fragment.MainFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	Context context;
	List<String> list;

	public GridViewAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
		}

		TextView tv_appName = (TextView) convertView.findViewById(R.id.tv_item);
		ImageView iv_appIcon =  (ImageView) convertView.findViewById(R.id.iv_item);

		tv_appName.setText(list.get(position));
		iv_appIcon.setImageResource(MainFragment.appIcon[position]);

		return convertView;
	}

}
