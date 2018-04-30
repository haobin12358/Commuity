package com.etech.wjs.community.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.activity.BookActivity;
import com.etech.wjs.community.activity.MainActivity;
import com.etech.wjs.community.adapter.BookAdapter;
import com.etech.wjs.community.common.HttpgetEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.entity.BookEntity;
import com.etech.wjs.community.global.AppConst;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BookFragment extends Fragment{
	
	private String Uid;
	private HttpgetEntity getEntity;
	
	private ListView lst;
	private BookAdapter adapter;
	private List<BookEntity> entitys = new ArrayList<BookEntity>();
	
	private ViewGroup tv_top;
	private TextView tv1, tv2;
	
	private String url_get_booklist = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/books/book_list";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getBd();
		View view = inflater.inflate(R.layout.activity_list, container, false);
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
		init(view);
		return view;
	}
	
	private void init(View view){
		tv_top = (ViewGroup)view.findViewById(R.id.tv_top);
		lst = (ListView)view.findViewById(R.id.lst);
		adapter = new BookAdapter(getActivity(), entitys);
		lst.setAdapter(adapter);
		lst.setOnItemClickListener(get_abo);
		tv1 = (TextView)view.findViewById(R.id.tv_top).findViewById(R.id.tv_title);
		tv2 = (TextView)view.findViewById(R.id.tv_top).findViewById(R.id.tv_edit);
		tv1.setText("图书列表");
		tv2.setText("");
		
		setText(book_list);
	}
	
	private OnItemClickListener get_abo = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), BookActivity.class);
			intent.putExtra("Uid", Uid);
			intent.putExtra("Bid", entitys.get(position).getBid());
			intent.putExtra("index", 0);
			startActivity(intent);
			getActivity().finish();
		}
		
	};
	
	private void getBd(){
		Uid = ((MainActivity)getActivity()).getUid();
	}
	
	private String book_list;
	private void getBookList(){
		getEntity = new HttpgetEntity();
		try {
			book_list = getEntity.doGet(url_get_booklist);
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
				Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT);
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
