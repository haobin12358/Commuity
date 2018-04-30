package com.etech.wjs.community.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.adapter.BookAdapter;
import com.etech.wjs.community.adapter.ReviewAdapter;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.entity.BookEntity;
import com.etech.wjs.community.entity.ReviewEntity;
import com.etech.wjs.community.global.AppConst;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CardActivity extends Activity{
	
	private HttpgetEntity getEntity;
	private HttppostEntity postEntity;
	
	private ListView lst;
	private ReviewAdapter adapter;
	private List<ReviewEntity> entitys = new ArrayList<ReviewEntity>();
	
	private ViewGroup tv_top;
	private TextView tv1, tv2;
	
	private TextView tv3, tv4, tv5, tv6, tv7;
	
	private String url_get_reviewlist = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/reviews/reviews_list?Cid=";
	private String url_get_cardabo = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/cards/card_abo?Cid=";
	private String url_new_review = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/reviews/add_review?Uid=";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getBd();
		setContentView(R.layout.activity_card);
		new Thread(){
			public void run(){
				getReviewList();
			}
		}.start();
		while(true){
			if(review_list != null){
				break;
			}
		}
		new Thread(){
			public void run(){
				getCardabo();
			}
		}.start();
		while(true){
			if(card_abo != null){
				break;
			}
		}
		Log.e("card_abo", card_abo);
		init();
	}
	
	void init(){
		tv_top = (ViewGroup)findViewById(R.id.tv_top);
		tv1 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_title);
		tv2 = (TextView)findViewById(R.id.tv_top).findViewById(R.id.tv_edit);
		tv1.setText("帖子详情");
		tv2.setText("返回");
		tv2.setOnClickListener(back);
		lst = (ListView)findViewById(R.id.lst);
		adapter = new ReviewAdapter(this, entitys);
		lst.setAdapter(adapter);
		lst.setOnItemClickListener(new_review);
		tv7 = (TextView)findViewById(R.id.tv_1);
		tv7.setText("");
		tv3 = (TextView)findViewById(R.id.tv_2);
		tv4 = (TextView)findViewById(R.id.tv_4);
		tv5 = (TextView)findViewById(R.id.tv_6);
		tv6 = (TextView)findViewById(R.id.tv_8);
		setCardabo(card_abo);
		setReviewlist(review_list);
	}
	
	private String RUid;
	private OnItemClickListener new_review = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			// TODO Auto-generated method stub
			showDialog("评论");
		}
		
	};
	
	private String response;
	private void showDialog(String title){
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.layout_alert, null);
		builder.setTitle(title);
		final EditText et_name = (EditText)view.findViewById(R.id.alert_1);
		final EditText et_no = (EditText)view.findViewById(R.id.alert_2);
		et_no.setVisibility(View.GONE);
		et_name.setHint("请输入回复内容");
		builder.setView(view);
		builder.setPositiveButton("发布", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String name = et_name.getText().toString();
				final JSONObject obj = new JSONObject();
				try {
					obj.put("Rabo", name);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new Thread(){
					public void run(){
						postEntity = new HttppostEntity();
						try {
							response = postEntity.doPost(obj, url_new_review + Uid + "&Cid=" + Cid + "&RUid=" + RUid);
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
	

	private OnClickListener back = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(CardActivity.this, MainActivity.class);
			intent.putExtra("Uid", Uid);
			intent.putExtra("index", 2);
			startActivity(intent);
			finish();
		}
		
	};

	private String Uid, Cid;
	private int index;
	private void getBd(){
		Intent intent = getIntent();
		try{
			Bundle bd = intent.getExtras();
			Cid = bd.getString("Cid");
			Uid = bd.getString("Uid");
			index = bd.getInt("index");
		}catch(Exception e){
			e.printStackTrace();
			index = 1;
		}
	}
	
	private String review_list;
	private void getReviewList(){
		getEntity = new HttpgetEntity();
		try {
			review_list = getEntity.doGet(url_get_reviewlist + Cid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String card_abo;
	private void getCardabo(){
		getEntity = new HttpgetEntity();
		try {
			card_abo = getEntity.doGet(url_get_cardabo + Cid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setCardabo(String card_abo){
		if(card_abo != null){
			JSONObject obj = StringToJSON.toJSONObject(card_abo);
			String string_data = obj.optString("data");
			JSONObject json_data = StringToJSON.toJSONObject(string_data);
			tv3.setText(json_data.optString("Cname"));
			tv4.setText(json_data.optString("Uname"));
			tv5.setText(json_data.optString("Ctime"));
			tv6.setText(json_data.optString("Cabo"));
		}
	}
	
	private void setReviewlist(String review_list){
		if(review_list == null){
			Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT);
		}else{
			JSONObject json_response = StringToJSON.toJSONObject(review_list);
			if(json_response.optInt("status") == 200){
				try{
					String string_data = json_response.optString("data");
					JSONArray json_data = StringToJSON.toJSONArray(string_data);
					entitys.clear();
					for(int i = 0; i < json_data.length(); i++){
						JSONObject json_item = json_data.getJSONObject(i);
						ReviewEntity entity = new ReviewEntity();
						entity.setUid(json_item.optString("Uid"));
						entity.setUname(json_item.optString("Uname"));
						entity.setRUid(json_item.optString("RUid"));
						entity.setRUname(json_item.optString("RUname"));
						entity.setRabo(json_item.optString("Rabo"));
						entity.setRtime(json_item.optString("Rtime"));
						entitys.add(entity);
					}
				}catch(Exception e){
					
				}
			}else{
				Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT);
			}
		}
	}
}
