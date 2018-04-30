package com.etech.wjs.community.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.adapter.LocationAdapter;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.entity.LocationEntity;
import com.etech.wjs.community.global.AppConst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyLocationsActivity extends Activity{
	
	private String Uid;
	private HttpgetEntity getEntity;
	private HttppostEntity postEntity;
	private ViewGroup tv_top;
	private TextView tv1, tv2;
	
	private ListView lst;
	private LocationAdapter adapter;
	private List<LocationEntity> entitys = new ArrayList<LocationEntity>();
	
	private String url_get_locationlist = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/locations/my_location?Uid=";
	private String url_update_locationstatus = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/locations/update_lstatus?Uid=";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getBd();
		setContentView(R.layout.activity_list);
		new Thread(){
			public void run(){
				getLocationList();
			}
		}.start();
		while(true){
			if(location_list != null){
				break;
			}
		}
		init();
	}
	
	private void init(){
		tv_top = (ViewGroup)findViewById(R.id.tv_top);
		lst = (ListView)findViewById(R.id.lst);
		adapter = new LocationAdapter(this, entitys);
		lst.setAdapter(adapter);
		lst.setOnItemClickListener(update_status);
		tv1 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_title);
		tv2 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_edit);
		tv1.setText("我的车位");
		tv2.setText("返回");
		tv2.setOnClickListener(back);
		setText(location_list);
	}
	
	private OnItemClickListener update_status = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			// TODO Auto-generated method stub
			new Thread(){
				public void run(){
					postStatus(position);
				}
			}.start();
			while(true){
				if(response != null){
					break;
				}
			}
			new Thread(){
				public void run(){
					getLocationList();
				}
			}.start();
			while(true){
				if(location_list != null){
					break;
				}
			}
			init();
		}
		
	};
	
	private String response;
	void postStatus(int position){
		postEntity = new HttppostEntity();
		JSONObject obj = new JSONObject();
		try {
			obj.put("Lstatus", 502);
			response = postEntity.doPost(obj, url_update_locationstatus + Uid + "&Lid=" + entitys.get(position).getLid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private OnClickListener back = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MyLocationsActivity.this, MainActivity.class);
			intent.putExtra("Uid", Uid);
			intent.putExtra("index", 3);
			startActivity(intent);
			finish();
		}
		
	};
	
	private int index;
	private void getBd(){
		Intent intent = getIntent();
		try{
			Bundle bd = intent.getExtras();
			Uid = bd.getString("Uid");
			index = bd.getInt("index");
		}catch(Exception e){
			e.printStackTrace();
			index = 1;
		}
	}
	
	private String location_list;
	private void getLocationList(){
		getEntity = new HttpgetEntity();
		try {
			location_list = getEntity.doGet(url_get_locationlist + Uid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setText(String task_list){
		if(task_list == null){
			Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT);
		}else{
			JSONObject json_response = StringToJSON.toJSONObject(task_list);
			if(json_response.optInt("status") == 200){
				try{
					String string_data = json_response.optString("data");
					JSONArray json_data = StringToJSON.toJSONArray(string_data);
					entitys.clear();
					for(int i = 0; i < json_data.length(); i++){
						JSONObject json_item = json_data.getJSONObject(i);
						LocationEntity entity = new LocationEntity();
						entity.setLid(json_item.optString("Lid"));
						entity.setLno(json_item.optString("Lno"));
						entity.setLstatus(deal_status(json_item.optInt("Lstatus")));
						entity.setLabo(json_item.optString("Labo"));
						entitys.add(entity);
					}
				}catch(Exception e){
					
				}
			}else{
				Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT);
			}
		}
	}
	
	private String deal_status(int status){
		String deal_status = "";
		if(status == 501){
			deal_status = "可用";
		}else if(status == 502){
			deal_status = "不可用";
		}else{
			deal_status = "未知状态";
		}
		return deal_status;
	}

}
