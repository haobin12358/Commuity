package com.etech.wjs.community.adapter;

import java.util.List;

import com.etech.wjs.community.R;
import com.etech.wjs.community.entity.CardEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardAdapter extends BaseAdapter{
	
	private Context context;
	private List<CardEntity> entitys;
	
	public CardAdapter(Context context, List<CardEntity> entitys){
		this.context = context;
		this.entitys = entitys;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return entitys.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return entitys.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_card, null);
			holder.Cname = (TextView)convertView.findViewById(R.id.tv_2);
			holder.Uname = (TextView)convertView.findViewById(R.id.tv_4);
			holder.Ctime = (TextView)convertView.findViewById(R.id.tv_6);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		CardEntity entity = entitys.get(position);
		holder.Cname.setText(entity.getCname());
		holder.Uname.setText(entity.getUname());
		holder.Ctime.setText(entity.getCtime());
		convertView.setTag(holder);
		return convertView;
	}
	
	class ViewHolder{
		private TextView Cname, Uname, Ctime;
	}

}
