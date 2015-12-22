package org.lzh.datepickerlib.dtpicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.lzh.datepickerlib.R;

import java.util.Calendar;

@SuppressLint("NewApi")
public class DTPickerDialog extends DialogFragment {
	
	private DTPicker dtPicker = null;
	private DTPicker.OnDTSelectListener mListener = null;
	private DatePicker.STYLE mDateStyle = DatePicker.STYLE.ALL;
	private DTPicker.STYLE mDTStyle = DTPicker.STYLE.ALL;
	
	private DTPicker.OnDTSelectListener mInnerListener = new DTPicker.OnDTSelectListener() {
		
		@Override
		public void onDTSelected(Calendar calendar) {
			if (mListener != null) {
				mListener.onDTSelected(calendar);
			}
			dismiss();
		}
	};
	
	public void setDTSelectListener (DTPicker.OnDTSelectListener listener) {
		this.mListener = listener;
	}
	
	public DTPickerDialog setDateStyle(DatePicker.STYLE style) {
		this.mDateStyle = style;
		return this;
	}
	
	public DTPickerDialog setDTStyle(DTPicker.STYLE style) {
		this.mDTStyle = style;
		return this;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(null);
		return inflater.inflate(R.layout.dialog_dt_picker, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		dtPicker = (DTPicker) view.findViewById(R.id.dt_picker);
		dtPicker.setOnTimeSelectListener(mInnerListener);
		dtPicker.setStyle(mDTStyle);
		dtPicker.setDateStyle(mDateStyle);
	}
	
}
