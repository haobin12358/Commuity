package com.etech.wjs.community.adapter;

import java.util.List;

import com.etech.wjs.community.R;
import com.etech.wjs.community.entity.LocationEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter{
	
	private Context context;
	private List<LocationEntity> entitys;
	
	public LocationAdapter(Context context, List<LocationEntity> entitys){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_location, null);
			holder.Lno = (TextView)convertView.findViewById(R.id.tv_2);
			holder.Lstatus = (TextView)convertView.findViewById(R.id.tv_4);
			holder.Labo = (TextView)convertView.findViewById(R.id.tv_6);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		LocationEntity entity = entitys.get(position);
		holder.Lno.setText(entity.getLno());
		holder.Lstatus.setText(entity.getLstatus());
		holder.Labo.setText(entity.getLabo());
		convertView.setTag(holder);
		return convertView;
	}
	
	class ViewHolder{
		private TextView Lno, Lstatus, Labo;
	}

}
