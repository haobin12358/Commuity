package com.etech.wjs.community.adapter;

import java.util.List;

import com.etech.wjs.community.R;
import com.etech.wjs.community.entity.BookEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookAdapter extends BaseAdapter{
	
	private Context context;
	private List<BookEntity> entitys;
	
	public BookAdapter(Context context, List<BookEntity> entitys){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_book, null);
			holder.Bno = (TextView)convertView.findViewById(R.id.tv_2);
			holder.Bname = (TextView)convertView.findViewById(R.id.tv_4);
			holder.Btype = (TextView)convertView.findViewById(R.id.tv_6);
			holder.Bstatus = (TextView)convertView.findViewById(R.id.tv_8);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		BookEntity entity = entitys.get(position);
		holder.Bno.setText(entity.getBno());
		holder.Bname.setText(entity.getBname());
		holder.Bstatus.setText(entity.getBstatus());
		holder.Btype.setText(entity.getBtype());
		convertView.setTag(holder);
		return convertView;
	}
	
	class ViewHolder{
		private TextView Bno, Bstatus, Btype, Bname;
	}

}
