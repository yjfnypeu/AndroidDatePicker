package org.lzh.androiddatepicker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.lzh.datepickerlib.dtpicker.DTPicker;
import org.lzh.datepickerlib.dtpicker.DTPickerDialog;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.go_dt_picker_dialog).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goSelectTime();
            }
        });
    }

    private void goSelectTime() {
        DTPickerDialog dialog = new DTPickerDialog();
        dialog.setDTSelectListener(new DTPicker.OnDTSelectListener() {

            @Override
            public void onDTSelected(Calendar calendar) {
                Log.d("DTPicker", calendar.getTime().toString());
                Toast.makeText(MainActivity.this, calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "DTPickerDialog");
    }
}
