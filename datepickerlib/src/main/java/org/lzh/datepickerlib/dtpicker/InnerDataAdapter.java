package org.lzh.datepickerlib.dtpicker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lzh.datepickerlib.R;

import java.util.List;

/**
 * 内部数据适配器。日期与时间
 */
public class InnerDataAdapter extends BaseAdapter{

	private List<String> list = null;
	private String select;
	private LayoutInflater mInflater;
	
	public InnerDataAdapter(Context context) {
		super();
		mInflater = LayoutInflater.from(context);
	}

	public InnerDataAdapter(List<String> list,Context context) {
		super();
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	public void setList(List<String> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	
	public int setSelection (String select) {
		if (!select.equals(this.select)) {
			notifyDataSetChanged();
		}
		this.select = select;
		int index = list.indexOf(select);
		return index;
	}
	
	public int getSelectIndex() {
		return list.indexOf(select);
	}
	
	public String getSelection() {
		return select;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		try {
			return list.get(position);
		} catch (IndexOutOfBoundsException e) {
			return list.get(list.size() - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_date_picker,
					parent, false);
			mHolder = new ViewHolder(convertView);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.tv.setText(list.get(position));
		if (list.get(position).equals(select)) {
			mHolder.tv.setTextColor(Color.RED);
		} else {
			mHolder.tv.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv;

		public ViewHolder(View root) {
			tv = (TextView) root.findViewById(R.id.item_date_picker_tv);
		}
	}

}
