package com.example.dophone.fragment;

import com.example.dophone.LoginActivity;
import com.example.dophone.R;
import com.example.dophone.ui.CircleImageView;
import com.example.dophone.util.PreferenceOperator;
import com.example.dophone.util.VonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserFragment extends Fragment implements OnClickListener{
	private View v;
	private LinearLayout ll_product_info;
	private LinearLayout ll_cashier_info;
	private LinearLayout ll_employee_info;
	private LinearLayout ll_notification_msg;
	private TextView tv_my_wallet;
	private TextView tv_title;
	private CircleImageView circle_img;
	private Button btn_exit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_user, null);
		initView();
		return v;
	}

	private void initView() {
		circle_img = (CircleImageView) v.findViewById(R.id.circle_img);
		circle_img.setImageResource(R.drawable.boy);
		btn_exit = (Button) v.findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);
		ll_product_info = (LinearLayout) v.findViewById(R.id.ll_product_info);
		ll_product_info.setOnClickListener(this);
		ll_cashier_info = (LinearLayout) v.findViewById(R.id.ll_cashier_info);
		ll_cashier_info.setOnClickListener(this);
		ll_employee_info = (LinearLayout) v.findViewById(R.id.ll_employee_info);
		ll_employee_info.setOnClickListener(this);
		ll_notification_msg = (LinearLayout) v
				.findViewById(R.id.ll_notification_msg);
		ll_notification_msg.setOnClickListener(this);
		tv_my_wallet = (TextView) v.findViewById(R.id.tv_my_wallet);
		tv_my_wallet.setOnClickListener(this);
		tv_title=(TextView) v.findViewById(R.id.tv_title);
		tv_title.setText("个人信息");
	}


	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
			case R.id.btn_exit:
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
				PreferenceOperator.clearPreference(getActivity(), "user_id", null);
				break;

			default:
				break;
		}
	}
}
