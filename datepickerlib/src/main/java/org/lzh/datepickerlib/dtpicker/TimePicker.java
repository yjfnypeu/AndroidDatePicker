package org.lzh.datepickerlib.dtpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import org.lzh.datepickerlib.R;
import org.lzh.datepickerlib.wheel.TosGallery;
import org.lzh.datepickerlib.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 时间选择器
 */
public class TimePicker extends RelativeLayout {
	
	protected Context mContext;
	
	private static final String AM = "am";
	private static final String PM = "pm";
	
	private boolean isAm = true;
	
	private static final int APM = 0;
	private static final int HOUR = 1;
	private static final int MIN = 2;
	
	/**
	 * 12小时制
	 */
	public static final int AM_PM = 0;
	 /**
	 * 24小时制
	 */
	public static final int NOT_AM_PM = 1;
	
	private int mType = AM_PM;
	
	/**
	 * 使用的时间控件布局id，暂时与日期使用一样的。
	 */
	private int timePickerLayout = R.layout.time_picker_layout;
	
	private LayoutInflater mInflater;
	
	private WheelView mApmView;//上下午
	private WheelView mHourView;// 小时
	private WheelView mMinView;// 分
	
	private OnTimeSelectListener mTimeListener;
	
	/**
	 * 用于保存当前的日期记录(年、月、日选中的项)
	 */
	private Calendar mCalendar;
	
	private InnerDataAdapter mApmAdapter;//上下午适配器
	private InnerDataAdapter mHourAdapter;// 小时适配器
	private InnerDataAdapter mMinAdapter;// 分钟适配器
	
	public TimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		// 初始化时间
		mCalendar = Calendar.getInstance();
		initLayout();
	}
	 
	/**
	 * 初始化布局
	 */
	private void initLayout() {
		removeAllViews();
		View child = mInflater.inflate(timePickerLayout, this, false);
		addView(child);
		
		LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
		if (layoutParams != null) {
			// 设置排版规则。子View居中
			layoutParams.addRule(CENTER_IN_PARENT, TRUE);
		}
		
		mApmView = (WheelView) findViewById(R.id.tp_apm);
		mHourView = (WheelView) findViewById(R.id.tp_hour);
		mMinView = (WheelView) findViewById(R.id.tp_min);
		
		if (mType != AM_PM) {
			mApmView.setVisibility(View.GONE);
		}
		
		mApmAdapter = new InnerDataAdapter(getContext());
		mHourAdapter = new InnerDataAdapter(getContext());
		mMinAdapter = new InnerDataAdapter(getContext());
		
		mApmView.setAdapter(mApmAdapter);
		mHourView.setAdapter(mHourAdapter);
		mMinView.setAdapter(mMinAdapter);
		
		resetAdapter();
		
		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int min = mCalendar.get(Calendar.MINUTE);
		
		if (mType == AM_PM) {
			isAm = hour > 12 ? false : true;
			mApmAdapter.setSelection(isAm ? AM:PM);
			mApmView.setSelection(mApmAdapter.setSelection(isAm ? AM:PM));
			mHourAdapter.setSelection((hour > 12 ? hour - 12 : hour) + "");
			mHourView.setSelection(mHourAdapter.setSelection((hour > 12 ? hour - 12 : hour) + ""));
		} else {
			mHourView.setSelection(mHourAdapter.setSelection(hour + ""));
		}
		mMinView.setSelection(mMinAdapter.setSelection(min + ""));
		
		mApmView.setOnEndFlingListener(new OnTimeEndFlingListener(APM));
		mHourView.setOnEndFlingListener(new OnTimeEndFlingListener(HOUR));
		mMinView.setOnEndFlingListener(new OnTimeEndFlingListener(MIN));
	}
	
	public class OnTimeEndFlingListener implements TosGallery.OnEndFlingListener {
		
		private int type = 0;
		
		public OnTimeEndFlingListener (int type ) {
			this.type = type;
		}
		
		@Override
		public void onEndFling(TosGallery v) {
			String select = v.getSelectedItem().toString();
			switch (type) {
			case APM:
				setAPM(select);
				break;
			case HOUR:
				setHour(Integer.parseInt(select));
				break;
			case MIN:
				setMin(Integer.parseInt(select));
				break;

			default:
				break;
			}
		}
		
	}
	
	private void resetAdapter() {
		mApmAdapter.setList(createAPMList());
		mHourAdapter.setList(createHourList());
		mMinAdapter.setList(createMinList());
	}

	private List<String> createAPMList() {
		List<String> list = new ArrayList<String>();
		list.add(AM);
		list.add(PM);
		return list;
	}
	
	private List<String> createHourList() {
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= (mType == AM_PM ? 12 : 24); i++) {
			list.add("" + i);
		}
		return list;
	}
	
	private List<String> createMinList() {
		List<String> list = new ArrayList<String>();
		list.add(00 + "");
		list.add(10 + "");
		list.add(20 + "");
		list.add(30 + "");
		list.add(40 + "");
		list.add(50 + "");
//		for (int i = 0; i < 60; i++) {
//			list.add("" + i);
//		}
		return list;
	}
	
	public void setAPM(String APM) {
		if ((APM.equals(AM) && isAm) || (APM.equals(PM) && !isAm)) {
			return;
		}
		
		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		if (APM.equals(AM)) {
			isAm = true;
			mCalendar.set(Calendar.HOUR_OF_DAY, hour % 12);
		} else {
			isAm = false;
			mCalendar.set(Calendar.HOUR_OF_DAY, hour % 12 + 12);
		}
		
		notifyTimeDataChange();
	}
	
	private void notifyTimeDataChange() {
		if (mTimeListener != null) {
			mTimeListener.onTimeSelected(getHour24(), getMin());
		}
	}

	public void setHour(int hour) {
		if (mType == AM_PM) {
			hour = isAm ? hour : hour + 12;
		}
		
		if (hour == mCalendar.get(Calendar.HOUR_OF_DAY)) {
			return;
		}
		
		mCalendar.set(Calendar.HOUR_OF_DAY, hour);
		notifyTimeDataChange();
	}
	
	public void setMin(int min) {
		int sMin = mCalendar.get(Calendar.MINUTE);
		if (min == sMin) {
			return;
		}
		mCalendar.set(Calendar.MINUTE, min);
		notifyTimeDataChange();
	}
	
	public int getHour24() {
		return mCalendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMin() {
		return mCalendar.get(Calendar.MINUTE);
	}
	
	public void setOnTimeSelectListener (OnTimeSelectListener listener) {
		this.mTimeListener = listener;
	}
	
	/**
	 * 时间选择回调器
	 */
	public interface OnTimeSelectListener {
		/**
		 * 
		 * @param hour 选中的小时数
		 * @param min 选中的分钟数
		 */
		void onTimeSelected(int hour, int min);
	}
	
}
