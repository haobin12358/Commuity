package com.etech.wjs.community.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.activity.MainActivity;
import com.etech.wjs.community.adapter.LocationAdapter;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.entity.LocationEntity;
import com.etech.wjs.community.global.AppConst;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LocationFragment extends Fragment{
	
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
			+ "/wjs/locations/location_list";
	private String url_update_locationstatus = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/locations/update_lstatus?Uid=";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getBd();
		View view = inflater.inflate(R.layout.activity_list, container, false);
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
		init(view);
		return view;
	}
	
	private void init(View view){
		tv_top = (ViewGroup)view.findViewById(R.id.tv_top);
		lst = (ListView)view.findViewById(R.id.lst);
		adapter = new LocationAdapter(getActivity(), entitys);
		lst.setAdapter(adapter);
		lst.setOnItemClickListener(update_status);
		tv1 = (TextView)view.findViewById(R.id.tv_top).findViewById(R.id.tv_title);
		tv2 = (TextView)view.findViewById(R.id.tv_top).findViewById(R.id.tv_edit);
		tv1.setText("车位列表");
		tv2.setText("");
		
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
			Log.e("response", response);
		}
		
	};
	
	private String response;
	void postStatus(int position){
		postEntity = new HttppostEntity();
		JSONObject obj = new JSONObject();
		try {
			obj.put("Lstatus", 501);
			response = postEntity.doPost(obj, url_update_locationstatus + Uid + "&Lid=" + entitys.get(position).getLid());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getBd(){
		Uid = ((MainActivity)getActivity()).getUid();
	}
	
	private String location_list;
	private void getLocationList(){
		getEntity = new HttpgetEntity();
		try {
			location_list = getEntity.doGet(url_get_locationlist);
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
				Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT);
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
