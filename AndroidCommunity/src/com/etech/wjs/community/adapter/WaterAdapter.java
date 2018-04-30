package com.etech.wjs.community.adapter;

import java.util.List;

import com.etech.wjs.community.R;
import com.etech.wjs.community.entity.WaterEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WaterAdapter extends BaseAdapter{
	
	private Context context;
	private List<WaterEntity> entitys;
	
	public WaterAdapter(Context context, List<WaterEntity> entitys){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_water, null);
			holder.Wtime = (TextView)convertView.findViewById(R.id.tv_2);
			holder.Wpay = (TextView)convertView.findViewById(R.id.tv_4);
			holder.Wstatus = (TextView)convertView.findViewById(R.id.tv_6);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		WaterEntity entity = entitys.get(position);
		holder.Wtime.setText(entity.getWyear() + "Äê" + entity.getWmonth() + "ÔÂ");
		holder.Wpay.setText(entity.getWpay() + "Ôª");
		holder.Wstatus.setText(entity.getWstatus());
		convertView.setTag(holder);
		return convertView;
	}
	
	class ViewHolder{
		private TextView Wtime, Wpay, Wstatus;
	}

}
