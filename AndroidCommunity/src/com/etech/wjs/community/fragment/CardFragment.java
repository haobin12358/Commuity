package com.etech.wjs.community.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.activity.CardActivity;
import com.etech.wjs.community.activity.MainActivity;
import com.etech.wjs.community.adapter.CardAdapter;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.entity.CardEntity;
import com.etech.wjs.community.global.AppConst;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CardFragment extends Fragment{
	
	private String Uid;
	private HttpgetEntity getEntity;
	private HttppostEntity postEntity;
	private ViewGroup tv_top;
	private TextView tv1, tv2;
	
	private ListView lst;
	private CardAdapter adapter;
	private List<CardEntity> entitys = new ArrayList<CardEntity>();
	
	private String url_get_cardlist = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/cards/card_list";
	
	private String url_new_card = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/cards/new_card?Uid=";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getBd();
		View view = inflater.inflate(R.layout.activity_list, container, false);
		new Thread(){
			public void run(){
				getCardList();
			}
		}.start();
		while(true){
			if(card_list != null){
				break;
			}
		}
		init(view);
		return view;
	}
	
	private void init(View view){
		tv_top = (ViewGroup)view.findViewById(R.id.tv_top);
		lst = (ListView)view.findViewById(R.id.lst);
		adapter = new CardAdapter(getActivity(), entitys);
		lst.setAdapter(adapter);
		lst.setOnItemClickListener(get_abo);
		tv1 = (TextView)view.findViewById(R.id.tv_top).findViewById(R.id.tv_title);
		tv2 = (TextView)view.findViewById(R.id.tv_top).findViewById(R.id.tv_edit);
		tv1.setText("论坛");
		tv2.setText("发帖");
		tv2.setOnClickListener(add_card);
		setText(card_list);
	}
	
	private OnClickListener add_card = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog("发布帖子");
		}
		
	};
	
	String response;
	private void showDialog(String title){
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.layout_alert, null);
		builder.setTitle(title);
		final EditText et_name = (EditText)view.findViewById(R.id.alert_1);
		final EditText et_no = (EditText)view.findViewById(R.id.alert_2);
		et_name.setHint("请输入帖子主题");
		et_no.setHint("请输入帖子内容");
		builder.setView(view);
		builder.setPositiveButton("发布", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String name = et_name.getText().toString();
				String no = et_no.getText().toString();
				final JSONObject obj = new JSONObject();
				try {
					obj.put("Cname", name);
					obj.put("Cabo", no);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new Thread(){
					public void run(){
						postEntity = new HttppostEntity();
						try {
							response = postEntity.doPost(obj, url_new_card + Uid);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
				while(true){
					if(response != null){
						break;
					}
				}
			}
		});
		builder.show();
	}
	
	private OnItemClickListener get_abo = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), CardActivity.class);
			intent.putExtra("Uid", Uid);
			intent.putExtra("Cid", entitys.get(position).getCid());
			intent.putExtra("index", 0);
			startActivity(intent);
			getActivity().finish();
		}
		
	};
	
	private void getBd(){
		Uid = ((MainActivity)getActivity()).getUid();
	}
	
	private String card_list;
	private void getCardList(){
		getEntity = new HttpgetEntity();
		try {
			card_list = getEntity.doGet(url_get_cardlist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setText(String task_list){
		if(task_list == null){
			Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT);
		}else{
			JSONObject json_response = StringToJSON.toJSONObject(task_list);
			if(json_response.optInt("status") == 200){
				try{
					String string_data = json_response.optString("data");
					JSONArray json_data = StringToJSON.toJSONArray(string_data);
					entitys.clear();
					for(int i = 0; i < json_data.length(); i++){
						JSONObject json_item = json_data.getJSONObject(i);
						CardEntity entity = new CardEntity();
						entity.setCid(json_item.optString("Cid"));
						entity.setCname(json_item.optString("Cname"));
						entity.setCtime(json_item.optString("Ctime"));
						entity.setUname(json_item.optString("Uname"));
						entitys.add(entity);
					}
				}catch(Exception e){
					
				}
			}else{
				Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT);
			}
		}
	}

}
