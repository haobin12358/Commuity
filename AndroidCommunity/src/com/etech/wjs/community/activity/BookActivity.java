package com.etech.wjs.community.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.global.AppConst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BookActivity extends Activity{
	
	private HttpgetEntity getEntity;
	private HttppostEntity postEntity;
	
	private String url_book_abo = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/books/book_abo?Bid=";
	
	private String url_update_bookstatus = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/books/update_status?Uid=";
	
	private TextView tv1, tv2, tv3, tv4, tv6, tv8, tv10;
	private ViewGroup tv_top;
	private Button btn1;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getBd();
		setContentView(R.layout.activity_book);
		new Thread(){
			public void run(){
				getBookabo();
			}
		}.start();
		while(true){
			if(book_abo != null){
				break;
			}
		}
		init();
	}
	
	private String Uid;
	private String Bid;
	private int index;
	private void getBd(){
		Intent intent = getIntent();
		try{
			Bundle bd = intent.getExtras();
			Uid = bd.getString("Uid");
			Bid = bd.getString("Bid");
			index = bd.getInt("index");
		}catch(Exception e){
			e.printStackTrace();
			index = 1;
		}
	}
	
	private String book_abo;
	private void getBookabo(){
		getEntity = new HttpgetEntity();
		try {
			book_abo = getEntity.doGet(url_book_abo + Bid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void init(){
		tv_top = (ViewGroup)findViewById(R.id.tv_top);
		tv1 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_title);
		tv2 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_edit);
		tv3 = (TextView)findViewById(R.id.tv_2);
		tv4 = (TextView)findViewById(R.id.tv_4);
		tv6 = (TextView)findViewById(R.id.tv_6);
		tv8 = (TextView)findViewById(R.id.tv_8);
		tv10 = (TextView)findViewById(R.id.tv_10);
		btn1 = (Button)findViewById(R.id.btn_1);
		btn1.setOnClickListener(update_status);
		tv1.setText("图书详情");
		tv2.setText("返回");
		tv2.setOnClickListener(back);
		
		setText(book_abo);
	}
	
	private OnClickListener update_status = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			new Thread(){
				public void run(){
					postStatus();
				}
			}.start();
			while(true){
				if(response != null){
					break;
				}
			}
			Log.e("response", response);
			new Thread(){
				public void run(){
					getBookabo();
				}
			}.start();
			while(true){
				if(book_abo != null){
					break;
				}
			}
			init();
		}
		
	};
	
	private String response;
	void postStatus(){
		postEntity = new HttppostEntity();
		JSONObject obj = new JSONObject();
		try {
			obj.put("Bstatus", 301);
			response = postEntity.doPost(obj, url_update_bookstatus + Uid + "&Bid=" + Bid);
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
			Intent intent = new Intent(BookActivity.this, MainActivity.class);
			intent.putExtra("Uid", Uid);
			intent.putExtra("index", 0);
			startActivity(intent);
			finish();
		}
		
	};
	
	private void setText(String book_abo){
		if(book_abo != null){
			JSONObject obj = StringToJSON.toJSONObject(book_abo);
			String string_data = obj.optString("data");
			JSONObject json_data = StringToJSON.toJSONObject(string_data);
			tv3.setText(json_data.optString("Bno"));
			tv4.setText(json_data.optString("Bname"));
			tv6.setText(deal_type(json_data.optInt("Btype")));
			tv8.setText(deal_status(json_data.optInt("Bstatus")));
			tv10.setText(json_data.optString("Babo"));
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
