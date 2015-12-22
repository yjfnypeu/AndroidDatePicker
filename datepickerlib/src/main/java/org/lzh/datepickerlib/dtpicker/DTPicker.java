package org.lzh.datepickerlib.dtpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lzh.datepickerlib.R;

import java.util.Calendar;

public class DTPicker extends RelativeLayout {
	
	protected Context mContext;
	
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	private STYLE mStyle = STYLE.ALL;
	
	private View select;
	
	private TextView mTvTabDate;
	private TextView mTvTabTime;
	
	private int dtPickerLayout = R.layout.date_time_picker_layout;
	
	private OnDTSelectListener mTimeSelectListener;
	
	public DTPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context; 
		
		initLayout();
		setCurrentTab(0);
		updateWithStyle();
	}
	
	private void updateWithStyle() {
		updateSingleItem(mStyle.showDate, mTvTabDate, mDatePicker);
		updateSingleItem(mStyle.showTime, mTvTabTime, mTimePicker);
		if (mStyle.showDate) {
			setCurrentTab(0);
		} else if (mStyle.showTime) {
			setCurrentTab(1);
		}
	}
	
	private void updateSingleItem(boolean show,View tab,View item) {
		if (show) {
			tab.setVisibility(View.VISIBLE);
			item.setVisibility(View.VISIBLE);
		} else {
			tab.setVisibility(View.GONE);
			tab.setVisibility(View.GONE);
		}
	}
	

	public void setStyle(STYLE style) {
		this.mStyle = style;
		updateWithStyle();
	}
	
	public void setDateStyle(DatePicker.STYLE style) {
		mDatePicker.setStyle(style);
	}
	
	private void initLayout() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View child = inflater.inflate(dtPickerLayout, this, false);
		addView(child);
		
		mDatePicker = (DatePicker) child.findViewById(R.id.dt_picker_date);
		mTimePicker = (TimePicker) child.findViewById(R.id.dt_picker_time);
		
		mTvTabDate = (TextView) child.findViewById(R.id.dt_picker_tab_date);
		mTvTabTime = (TextView) child.findViewById(R.id.dt_picker_tab_time);
		
		select = child.findViewById(R.id.dt_picker_tab_select);
		
		TabClickListener tabClickListener = new TabClickListener();
		mTvTabDate.setOnClickListener(tabClickListener);
		mTvTabTime.setOnClickListener(tabClickListener);
		
		mDatePicker.setOnDateSelectListener(new OnDateChange());
		mTimePicker.setOnTimeSelectListener(new OnTimeChange());
		
		select.setOnClickListener(new SelectListener());
		
	}
	
	public Calendar getTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, mDatePicker.getYear());
		calendar.set(Calendar.MONTH, mDatePicker.getMonth() - 1);
		calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDay());
		
		calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour24());
		calendar.set(Calendar.MINUTE, mTimePicker.getMin());
		return calendar;
	}
	
	class OnDateChange implements DatePicker.OnDateSelectListener {

		@Override
		public void onDateSelected(int year, int month, int day) {
			StringBuffer dateBuffer = new StringBuffer();
			DatePicker.STYLE style = mDatePicker.getStyle();
			if (style.isShowYear()) {
				dateBuffer.append(year + "年");
			}
			if (style.isShowMonth()) {
				dateBuffer.append(month + "月");
			}
			if (style.isShowDay()) {
				dateBuffer.append(day + "日");
			}
			mTvTabDate.setText(dateBuffer.toString());
		}
	}
	
	class OnTimeChange implements TimePicker.OnTimeSelectListener {

		@Override
		public void onTimeSelected(int hour, int min) {
			StringBuffer timeBuffer = new StringBuffer();
			timeBuffer.append(hour > 12 ? "下午 " + (hour % 12) : "上午 " + hour);
			timeBuffer.append(":" + min);
			mTvTabTime.setText(timeBuffer.toString());
		}
		
	}
	
	class SelectListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mTimeSelectListener != null) {
				mTimeSelectListener.onDTSelected(getTime());
			}
		}
		
	}
	
	class TabClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {	
			if (v.getId() == R.id.dt_picker_tab_date) {
				setCurrentTab(0);
			} else if (v.getId() == R.id.dt_picker_tab_time) {
				setCurrentTab(1);
			}
		}
	}
	
	/**
	 * @param index 0 : date;   1: time
	 */
	void setCurrentTab(int index) {
		mDatePicker.setVisibility(View.GONE);
		mTimePicker.setVisibility(View.GONE);
		mTvTabDate.setSelected(false);
		mTvTabTime.setSelected(false);
		if (mStyle == STYLE.ALL) {
			
		}
		switch (index) {
		case 0:
			mDatePicker.setVisibility(View.VISIBLE);
			if (mStyle == STYLE.ALL) {
				mTvTabDate.setSelected(true);
			}
			break;
		default:
			mTimePicker.setVisibility(View.VISIBLE);
			if (mStyle == STYLE.ALL) {
				mTvTabTime.setSelected(true);
			}
			break;
		}
	}
	
	public void setOnTimeSelectListener (OnDTSelectListener listener) {
		this.mTimeSelectListener = listener;
	}
	
	public interface OnDTSelectListener {
		void onDTSelected(Calendar calendar);
	}
	
	public enum STYLE {
		TIME(false,true),
		DATE(true,false),
		ALL(true,true);
		boolean showDate;
		boolean showTime;
		private STYLE(boolean showDate,boolean showTime) {
			this.showDate = showDate;
			this.showTime = showTime;
		}
		public boolean isShowDate() {
			return showDate;
		}
		public boolean isShowTime() {
			return showTime;
		}
	}
	
}
