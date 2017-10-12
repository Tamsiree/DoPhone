package com.example.pinyinlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public abstract class PinyinAdapter extends BaseExpandableListAdapter {

	// 字符串
	private List<PinyingBean> strList;

	private AssortPinyinList assort = new AssortPinyinList();

	public Context context;

	private LayoutInflater inflater;
	// 中文排序
	private LanguageComparator_CN cnSort = new LanguageComparator_CN();

	public PinyinAdapter(Context context, List<PinyingBean> strList) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.strList = strList;


		long time = System.currentTimeMillis();
		// 排序
		sort();
		Toast.makeText(context,
				String.valueOf(System.currentTimeMillis() - time), 1).show();

	}

	private void sort() {
		// 分类
		for (PinyingBean str : strList) {
			assort.getHashList().add(str);
		}
		assort.getHashList().sortKeyComparator(cnSort);


//		for(int i=0,length=assort.getHashList().size();i<length;i++)
//		{
//			List list = (assort.getHashList().getValueListIndex(i));
//			//Collections.sort(list,cnSort);
//		}

	}

	public Object getChild(int group, int child) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueIndex(group, child);
	}

	public abstract long getChildId(int group, int child);

	public abstract View getChildView(int group, int child, boolean arg2,
									  View contentView, ViewGroup arg4);

	public int getChildrenCount(int group) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueListIndex(group).size();
	}

	public Object getGroup(int group) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueListIndex(group);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return assort.getHashList().size();
	}

	public long getGroupId(int group) {
		// TODO Auto-generated method stub
		return group;
	}
	public String getGroupName(int group)
	{
		return assort.getFirstChar(assort.getHashList()
				.getValueIndex(group, 0).getName());
	}
	public abstract View getGroupView(int group, boolean arg1, View contentView,
									  ViewGroup arg3);

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public AssortPinyinList getAssort() {
		return assort;
	}

}
