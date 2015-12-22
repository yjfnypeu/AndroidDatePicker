package org.lzh.datepickerlib.dtpicker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lzh.datepickerlib.R;
import org.lzh.datepickerlib.wheel.TosAdapterView;
import org.lzh.datepickerlib.wheel.TosGallery;
import org.lzh.datepickerlib.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期选择器
 * @author lzh
 */
public class DatePicker extends RelativeLayout {
	
	private LayoutInflater mInflater = null;
	/**
	 * 使用的日期控件布局id
	 */
	private int datePickerLayout = R.layout.date_picker_layout;
	
	private WheelView year;
	private WheelView month;
	private WheelView day;
	
	private STYLE mStyle = STYLE.YEAR_AND_MONTH;
	
	/**
	 * 用于保存当前的日期记录(年、月、日选中的项)
	 */
	private Calendar calendar;

	private InnerDataAdapter mYearAdapter;
	private InnerDataAdapter mMonthAdapter;
	private InnerDataAdapter mDayAdapter;
	
	private OnDateSelectListener mDateSelectListener;

	public DatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = LayoutInflater.from(context);
		calendar = Calendar.getInstance();
		
		initLayout();
		updateWithStyle();
	}
	
	public void setStyle(STYLE style) {
		this.mStyle = style;
		updateWithStyle();
	}
	
	STYLE getStyle() {
		return mStyle;
	}
	
	private void updateWithStyle() {
		updateSingleItem(mStyle.showYear,year);
		updateSingleItem(mStyle.showMonth,month);
		updateSingleItem(mStyle.showDay,day);
	}

	private void updateSingleItem(boolean show,View item) {
		if (show) {
			item.setVisibility(View.VISIBLE);
		} else {
			item.setVisibility(View.GONE);
		}
	}
	
	

	public void setDate(Date date) {
		calendar.setTime(date);
		initOrResetAdapter();
	}
	
	public void setCalender(Calendar calendar) {
		this.calendar = calendar;
		initOrResetAdapter();
	}
	
	/**
	 * 设置当前选中的年份。同时更新月与日
	 * @param year
	 */
	public void setYear(int year) {
		if (calendar.get(Calendar.YEAR) == year) {
			return;
		}
		calendar.set(Calendar.YEAR, year);
		notifyDateChange();
	}
	
	/**
	 * 设置当前选中的月。同时更新日
	 * @param month
	 */
	public void setMonth(int month) {
		if (calendar.get(Calendar.MONTH) == month - 1) {
			return;
		}
		calendar.set(Calendar.MONTH, month - 1);
		mDayAdapter.setList(
				createDayList(month, calendar.get(Calendar.YEAR)));
		notifyDateChange();
	}
	
	/**
	 * 设置当前选中的日
	 * @param day
	 */
	public void setDayOfMonth(int day) {
		if (day == calendar.get(Calendar.DAY_OF_MONTH)) {
			return;
		}
		calendar.set(Calendar.DAY_OF_MONTH, day);
		notifyDateChange();
//		mDayAdapter.setSelection(day + "");
//		initOrResetAdapter();
	}
	
	private void notifyDateChange() {
		if (mDateSelectListener != null) {
			mDateSelectListener.onDateSelected(getYear(), getMonth(), getDay());
		}
	}
	
	public int getYear() {
		return Integer.valueOf(year.getSelectedItem().toString());
	}
	
	public int getMonth() {
		return Integer.valueOf(month.getSelectedItem().toString());
	}
	
	public int getDay() {
		return Integer.valueOf(day.getSelectedItem().toString());
	}

	private void initOrResetAdapter() {
		int curYear = calendar.get(Calendar.YEAR);
		int curMonth = calendar.get(Calendar.MONTH) + 1;//月份以0开始
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		List<String> yearList = createYearList();
		List<String> monthList = createMonthList();
		List<String> dayList = createDayList(curMonth, curYear);
		
		if (mYearAdapter == null) {
			mYearAdapter = new InnerDataAdapter(yearList,getContext());
		} else {
			mYearAdapter.setList(yearList);
		}
		mYearAdapter.setSelection(curYear + "");
		
		if (mMonthAdapter == null) {
			mMonthAdapter = new InnerDataAdapter(monthList,getContext());
		} else {
			mMonthAdapter.setList(monthList);
		}
		mMonthAdapter.setSelection(curMonth + "");
		
		if (mDayAdapter == null) {
			mDayAdapter = new InnerDataAdapter(dayList,getContext());
		} else {
			mDayAdapter.setList(dayList);
		}
		mDayAdapter.setSelection(curDay + "");
	}
	
	/**
	 * 获取年份列表。从1700到当前年份+10
	 * @return
	 */
	private List<String> createYearList() {
		Calendar temp = Calendar.getInstance();
		int year = temp.get(Calendar.YEAR);
		List<String> list = new ArrayList<String>();
		for (int i = 1700; i <= year + 10; i++) {
			list.add(i + "");
		}
		return list;
	}

	/**
	 * 获取月份列表
	 * @return
	 */
	private List<String> createMonthList() {
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			list.add(i + "");
		}
		return list;
	}

	/**
	 * 获取不同月份中的日期列表
	 * @param month
	 * @param year
	 * @return
	 */
	private List<String> createDayList(int month, int year) {
		boolean isLeap = isLeapYear(year);
		List<String> list = new ArrayList<String>();
		//先填充基本数据
		for (int i = 1; i <= 28; i++) {
			list.add(i + "");
		}
		switch (month) {
		case 2:
			if (isLeap) {
				list.add("29");
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			list.add("29");
			list.add("30");
			break;
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			list.add("29");
			list.add("30");
			list.add("31");
			break;
		}
		return list;
	}

	/**
	 * 检查当前的年份是否为闰年。
	 * @param year
	 * @return
	 */
	private boolean isLeapYear(int year) {
		boolean isLeapYear = false;
		if (year % 100 != 0) {
			isLeapYear = year % 400 == 0;
		} else {
			isLeapYear = year % 4 == 0;
		}
		return isLeapYear;
	}

	private void initLayout() {
		removeAllViews();
		View child = mInflater.inflate(datePickerLayout, this, false);
		addView(child);
		
		LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
		if (layoutParams != null) {
			// 设置排版规则。子View居中
			layoutParams.addRule(CENTER_IN_PARENT, TRUE);
		}
		year = (WheelView) findViewById(R.id.dp_year);
		month = (WheelView) findViewById(R.id.dp_month);
		day = (WheelView) findViewById(R.id.dp_day);
		
		initOrResetAdapter();
		day.setScrollCycle(true);
		year.setAdapter(mYearAdapter);
		year.setSelection(mYearAdapter.getSelectIndex());
		month.setAdapter(mMonthAdapter);
		month.setSelection(mMonthAdapter.getSelectIndex());
		day.setAdapter(mDayAdapter);
		day.setSelection(mDayAdapter.getSelectIndex());
		
		year.setOnEndFlingListener(new DateFlingListener(DateFlingListener.TYPE_YEAR));
		month.setOnEndFlingListener(new DateFlingListener(DateFlingListener.TYPE_MONTH));
		day.setOnEndFlingListener(new DateFlingListener(DateFlingListener.TYPE_DAY));
		
		
	}
	
	
	
	/**
	 * 滚动完成监听
	 * @author lzh
	 *
	 */
	class DateFlingListener implements TosGallery.OnEndFlingListener,TosAdapterView.OnItemSelectedListener {
		
		static final int TYPE_YEAR = 0;
		static final int TYPE_MONTH = 1;
		static final int TYPE_DAY = 2;
		
		private int type = 0;
		
		public DateFlingListener(int type) {
			this.type = type;
		}

		@Override
		public void onEndFling(TosGallery v) {
			String date = v.getSelectedItem().toString();
			int select = Integer.parseInt(date);
			System.out.println("select = " + select + ",date = " + date);
			switch (type) {
			case TYPE_YEAR:
				setYear(select);
				break;
			case TYPE_MONTH:
				setMonth(select);
				break;
			case TYPE_DAY:
				setDayOfMonth(select);
				break;
			}
		}

		@Override
		public void onItemSelected(TosAdapterView<?> parent, View view,
				int position, long id) {
			System.out.println("onItemSelected:" + position);
		}

		@Override
		public void onNothingSelected(TosAdapterView<?> parent) {
			System.out.println("onNothingSelected:");
		}
		
	}

	/**
	 * 日期数据适配器
	 * @author lzh
	 */
	class DateAdapter extends BaseAdapter {

		private List<String> list = null;
		private String select;

		public DateAdapter(List<String> list) {
			super();
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
			return list.get(position);
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
	
	public void setOnDateSelectListener (OnDateSelectListener listener) {
		this.mDateSelectListener = listener;
	}

	/**
	 * 日期改变监听
	 */
	public interface OnDateSelectListener {

		/**
		 * 日期改变完成
		 * @param year
		 * @param month
		 * @param day
		 */
		void onDateSelected(int year, int month, int day);
	}
	
	/** 
	 * 显示的样式
	 */
	public enum STYLE {
		/**
		 * 只显示年和月
		 */
		YEAR_AND_MONTH(true,true,false),
		/**
		 * 显示全部
		 */
		ALL(true,true,true);
		
		private boolean showYear;
		private boolean showMonth;
		private boolean showDay;
		
		private STYLE(boolean showYear,boolean showMonth,boolean showDay) {
			this.showYear = showYear;
			this.showMonth = showMonth;
			this.showDay = showDay;
		}

		public boolean isShowYear() {
			return showYear;
		}

		public boolean isShowMonth() {
			return showMonth;
		}

		public boolean isShowDay() {
			return showDay;
		}
		
		
	}

}
