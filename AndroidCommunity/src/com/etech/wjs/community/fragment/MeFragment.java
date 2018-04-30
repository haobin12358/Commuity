package com.etech.wjs.community.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.global.AppConst;
import com.etech.wjs.community.activity.MainActivity;
import com.etech.wjs.community.activity.MyBookActivity;
import com.etech.wjs.community.activity.MyLocationsActivity;
import com.etech.wjs.community.activity.MyWaterActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MeFragment extends Fragment{
	
	private HttpgetEntity getEntity;
	private HttppostEntity postEntity;
	
	private String get_personal_url = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/users/user_info?Uid=";
	
	private String update_personal_url = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/users/update_info?Uid=";
	
	private String Uid;
	
	private TextView tv1, tv2, tv3;
	private EditText et1, et2, et3, et4, et5;
	private ViewGroup tv_top;
	private Button btn1, btn2, btn3;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getBd();
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		new Thread(){
			public void run(){
				getMeText();
			}
		}.start();
		while(true){
			if(personal_text != null){
				break;
			}
		}
		init(view);
		return view;
	}
	
	private String personal_text = null;
	private void getMeText(){
		getEntity = new HttpgetEntity();
		try {
			Log.e("Url", get_personal_url + Uid);
			personal_text = getEntity.doGet(get_personal_url + Uid);
			Log.e("personal_text", personal_text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("getpersonal", "error");
			e.printStackTrace();
		}
	}
	
	private void getBd(){
		Uid = ((MainActivity)getActivity()).getUid();
	}
	
	private void init(View view){
		tv1 = (TextView)view.findViewById(R.id.tv_1);
		et1 = (EditText)view.findViewById(R.id.et_1);
		et2 = (EditText)view.findViewById(R.id.et_2);
		et3 = (EditText)view.findViewById(R.id.et_3);
		et4 = (EditText)view.findViewById(R.id.et_4);
		et5 = (EditText)view.findViewById(R.id.et_5);
		btn1 = (Button)view.findViewById(R.id.btn_1);
		btn2 = (Button)view.findViewById(R.id.btn_2);
		btn3 = (Button)view.findViewById(R.id.btn_3);
		tv_top = (ViewGroup)view.findViewById(R.id.tv_group);
		tv2 = (TextView)view.findViewById(R.id.tv_group).findViewById(R.id.tv_title);
		tv3 = (TextView)view.findViewById(R.id.tv_group).findViewById(R.id.tv_edit);
		tv2.setText("个人中心");
		tv3.setText("编辑");
		tv3.setOnClickListener(edit);
		btn1.setOnClickListener(mybook);
		btn2.setOnClickListener(mylocation);
		btn3.setOnClickListener(mywater);
		et1.setFocusable(false);
		et1.setFocusableInTouchMode(false);
		et2.setFocusable(false);
		et2.setFocusableInTouchMode(false);
		et3.setFocusable(false);
		et3.setFocusableInTouchMode(false);
		et4.setFocusable(false);
		et4.setFocusableInTouchMode(false);
		et5.setFocusable(false);
		et5.setFocusableInTouchMode(false);
		
		setMeText(personal_text);
	}
	
	String result_of_post;
	private OnClickListener edit = new OnClickListener(){

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if(tv3.getText().toString().equals("编辑")){
				tv3.setText("确定");
				et1.setFocusable(true);
				et1.setFocusableInTouchMode(true);
				et2.setFocusable(true);
				et2.setFocusableInTouchMode(true);
				et3.setFocusable(true);
				et3.setFocusableInTouchMode(true);
				et4.setFocusable(true);
				et4.setFocusableInTouchMode(true);
				et5.setFocusable(true);
				et5.setFocusableInTouchMode(true);
			}else if(tv3.getText().toString().equals("确定")){
				getText();
				new Thread(){
					public void run(){
						JSONObject obj = new JSONObject();
						try {
							obj.put("Uname", Uname);
							obj.put("Usex", Usex);
							obj.put("Utel", Utel);
							obj.put("Ucardno", Ucardno);
							obj.put("Ulive", Ulive);
							postEntity = new HttppostEntity();
							result_of_post = postEntity.doPost(obj, update_personal_url + Uid);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
				tv3.setText("编辑");
				et1.setFocusable(false);
				et1.setFocusableInTouchMode(false);
				et2.setFocusable(false);
				et2.setFocusableInTouchMode(false);
				et3.setFocusable(false);
				et3.setFocusableInTouchMode(false);
				et4.setFocusable(false);
				et4.setFocusableInTouchMode(false);
				et5.setFocusable(false);
				et5.setFocusableInTouchMode(false);
			}
			
		}
		
	};
	
	private String Uname, string_Usex, Ucardno, Utel, Ulive;
	private int Usex;
	private void getText(){
		Uname = et1.getText().toString();
		string_Usex = et2.getText().toString();
		if(string_Usex.equals("男")){
			Usex = 201;
		}else if(string_Usex.equals("女")){
			Usex = 202;
		}else{
			Usex = 203;
		}
		Utel = et3.getText().toString();
		Ucardno = et4.getText().toString();
		Ulive = et5.getText().toString();
	}
	
	private OnClickListener mybook = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), MyBookActivity.class);
			intent.putExtra("index", 3);
			intent.putExtra("Uid", Uid);
			startActivity(intent);
			getActivity().finish();
		}
		
	};
	
	private OnClickListener mylocation = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), MyLocationsActivity.class);
			intent.putExtra("index", 3);
			intent.putExtra("Uid", Uid);
			startActivity(intent);
			getActivity().finish();
		}
		
	};
	
	private OnClickListener mywater = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), MyWaterActivity.class);
			intent.putExtra("index", 3);
			intent.putExtra("Uid", Uid);
			startActivity(intent);
			getActivity().finish();
		}
		
	};
	
	private void setMeText(String personal_text){
		if(personal_text == null){
			Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT);
		}else{
			final JSONObject json_obj = StringToJSON.toJSONObject(personal_text);
			if(json_obj.optInt("status") == 200){
				new Thread(){
					public void run(){
						String string_data = json_obj.optString("data");
						JSONObject json_data = StringToJSON.toJSONObject(string_data);
						if(json_data.optString("Uname").length() == 0){
							et1.setText(json_data.optString("Utel"));
						}else{
							et1.setText(json_data.optString("Uname"));
						}
						if(json_data.optInt("Usex") == 301){
							et2.setText("男");
						}else if(json_data.optInt("Usex") == 302){
							et2.setText("女");
						}else{
							
						}
						tv1.setText("￥" + json_data.optDouble("Ucoin"));
						et3.setText(json_data.optString("Utel"));
						et4.setText(json_data.optString("Ucardno"));
						et5.setText(json_data.optString("Ulive"));
						
					}
				}.start();
			}else{
				Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT);
			}
		}
	}

}
