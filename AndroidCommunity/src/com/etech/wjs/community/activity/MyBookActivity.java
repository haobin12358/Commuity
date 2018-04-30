package com.etech.wjs.community.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.adapter.BookAdapter;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.entity.BookEntity;
import com.etech.wjs.community.global.AppConst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyBookActivity extends Activity{
	
	private String Uid;
	private HttpgetEntity getEntity;
	private HttppostEntity postEntity;
	
	private ListView lst;
	private BookAdapter adapter;
	private List<BookEntity> entitys = new ArrayList<BookEntity>();
	
	private ViewGroup tv_top;
	private TextView tv1, tv2;
	
	private String url_get_booklist = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/books/my_book?Uid=";
	
	private String url_update_bookstatus = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/books/update_status?Uid=";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getBd();
		setContentView(R.layout.activity_list);
		new Thread(){
			public void run(){
				getBookList();
			}
		}.start();
		while(true){
			if(book_list != null){
				break;
			}
		}
		init();
	}
	
	private void init(){
		tv_top = (ViewGroup)findViewById(R.id.tv_top);
		lst = (ListView)findViewById(R.id.lst);
		adapter = new BookAdapter(this, entitys);
		lst.setAdapter(adapter);
		lst.setOnItemClickListener(update_status);
		tv1 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_title);
		tv2 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_edit);
		tv1.setText("我借阅的书");
		tv2.setText("返回");
		tv2.setOnClickListener(back);
		
		setText(book_list);
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
					getBookList();
				}
			}.start();
			while(true){
				if(book_list != null){
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
			obj.put("Bstatus", 302);
			response = postEntity.doPost(obj, url_update_bookstatus + Uid + "&Bid=" + entitys.get(position).getBid());
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
			Intent intent = new Intent(MyBookActivity.this, MainActivity.class);
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
	
	private String book_list;
	private void getBookList(){
		getEntity = new HttpgetEntity();
		try {
			book_list = getEntity.doGet(url_get_booklist + Uid);
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
						BookEntity entity = new BookEntity();
						entity.setBid(json_item.optString("Bid"));
						entity.setBname(json_item.optString("Bname"));
						entity.setBno(json_item.optString("Bno"));
						entity.setBstatus(deal_status(json_item.optInt("Bstatus")));
						entity.setBtype(deal_type(json_item.optInt("Btype")));
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
		if(status == 301){
			deal_status = "可借阅";
		}else if(status == 302){
			deal_status = "不可借阅";
		}else{
			deal_status = "未知状态";
		}
		return deal_status;
	}
	
	private String deal_type(int type){
		String deal_type = "";
		if(type == 401){
			deal_type = "历史类";
		}else if(type == 402){
			deal_type = "军事类";
		}else if(type == 403){
			deal_type = "经管类";
		}else if(type == 404){
			deal_type = "文学类";
		}else if(type == 405){
			deal_type = "其他";
		}else{
			deal_type = "未知";
		}
		return deal_type;
	}

}
