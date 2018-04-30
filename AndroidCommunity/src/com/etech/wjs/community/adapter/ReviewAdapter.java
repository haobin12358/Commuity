package com.etech.wjs.community.adapter;

import java.util.List;

import com.etech.wjs.community.R;
import com.etech.wjs.community.entity.ReviewEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReviewAdapter extends BaseAdapter{
	
	private Context context;
	private List<ReviewEntity> entitys;
	
	public ReviewAdapter(Context context, List<ReviewEntity> entitys){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_review, null);
			holder.URname = (TextView)convertView.findViewById(R.id.tv_2);
			holder.Rabo = (TextView)convertView.findViewById(R.id.tv_6);
			holder.Rtime = (TextView)convertView.findViewById(R.id.tv_4);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		ReviewEntity entity = entitys.get(position);
		holder.URname.setText(entity.getUname()+"»Ø¸´"+entity.getUname());
		holder.Rabo.setText(entity.getRabo());
		holder.Rtime.setText(entity.getRtime());
		convertView.setTag(holder);
		return convertView;
	}
	
	class ViewHolder{
		private TextView URname, Rabo, Rtime;
	}

}
