package com.etech.wjs.community.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.etech.wjs.community.R;
import com.etech.wjs.community.common.HttppostEntity;
import com.etech.wjs.community.common.StringToJSON;
import com.etech.wjs.community.global.AppConst;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity{

	private EditText et1, et2;
	private TextView tv1, tv2;
	private Button btn1, btn2;
	
	private String register_url = "http://" 
			+ AppConst.sServerURL 
			+ "/wjs/users/register";

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		init();
	}
	
	void init(){
		et1 = (EditText)findViewById(R.id.et_1);
		et2 = (EditText)findViewById(R.id.et_2);
		tv1 = (TextView)findViewById(R.id.tv_1);
		tv2 = (TextView)findViewById(R.id.tv_2);
		btn1 = (Button)findViewById(R.id.btn_1);
		btn2 = (Button)findViewById(R.id.btn_2);
		
		tv1.setText("用户名：");
		tv2.setText("密码：");
		et1.setHint("请输入用户名");
		et2.setHint("请输入密码");
		btn1.setText("注册");
		btn2.setText("取消");
		
		btn1.setOnClickListener(register_listener);
		btn2.setOnClickListener(sove_listener);
	};
	
	private OnClickListener register_listener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(getText()){
				getText();
				new Thread(){
					public void run(){
						postText();
					}
				}.start();
				while(true){
					if(result_register != null){
						break;
					}
				}
				
				JSONObject json_obj = StringToJSON.toJSONObject(result_register);
				if(json_obj.optInt("status") == 200){
					Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT);
					Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}else{
					Toast.makeText(RegisterActivity.this, "系统异常", Toast.LENGTH_SHORT);
				}
				Log.e("register","ok");
				
				
			}
		}
		
	};
	
	private OnClickListener sove_listener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		
	};
	
	private String Uname, Upwd;
	private boolean getText(){
		Uname = et1.getText().toString();
		Log.e("Uname", Uname);
		if(Uname.length() == 0){
			new AlertDialog.Builder(this).setTitle(R.string.ti_xing).setMessage(R.string.qing_shu_ru_yong_hu_ming).show();
			return false;
		}
		Upwd = et2.getText().toString();
		Log.e("Upwd", Upwd);
		if(Upwd.length() == 0){
			new AlertDialog.Builder(this).setTitle(R.string.ti_xing).setMessage(R.string.qing_shu_ru_mi_ma).show();
			return false;
		}
		return true;
	}
	
	private String result_register;
	//灏佽鏁版嵁浼犺緭
	private void postText(){
		JSONObject obj = new JSONObject();
		try {
			obj.put("Utel",Uname);
			obj.put("Upwd", Upwd);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("url",register_url);
		HttppostEntity httppost = new HttppostEntity();
		try {
			result_register = httppost.doPost(obj, register_url);
			Log.e("result",result_register);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("post", "error");
			e.printStackTrace();
		}
	}
}
