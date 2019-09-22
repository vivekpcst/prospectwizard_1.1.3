package com.dzo.prospectWizard.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdditionalActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout additonal;
    SharedPreferences sharedPreferences,sharedPreferences1;
    int Color1;
    String Font;
    Typeface face;
    String userName;
    Button btnDatePicker2, btnTimePicker2;
    Button send;
    EditText txtDate, txtTime,add_edittext1;
    String desc,sdate, aTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar beginTime = Calendar.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Additional Task");
        getLayoutInflater().inflate(R.layout.activity_additional, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        additonal =(LinearLayout) findViewById(R.id.additonal);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AdditionalActivity.this);
        userName = preferences.getString("UserName", "");

        btnDatePicker2=(Button)findViewById(R.id.btn_date1);
        btnTimePicker2=(Button)findViewById(R.id.btn_time1);

        txtDate=(EditText)findViewById(R.id.in_date1);
        txtTime=(EditText)findViewById(R.id.in_time1);
        add_edittext1= (EditText) findViewById(R.id.add_edittext1);
        add_edittext1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    send.performClick();
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
                return true;
            }
        });
        send=(Button)findViewById(R.id.stEvent1);
        btnDatePicker2.setOnClickListener(this);
        btnTimePicker2.setOnClickListener(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    desc  =   add_edittext1.getText().toString();
                    sdate=txtDate.getText().toString();
                    if(add_edittext1.getText().length()==0){
                        Toast.makeText(getApplicationContext(),"Fill above ",Toast.LENGTH_SHORT).show();
                    }
                    else if(txtDate.getText().length()==0){
                        Toast.makeText(getApplicationContext(),"Fill above ",Toast.LENGTH_SHORT).show();

                    }else {
                        Events();

                    }







            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        additonal.setBackgroundColor(Color1);
        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(getAssets(), Font);
        }
        if (face != null) {
            send.setTypeface(face);
            add_edittext1.setTypeface(face);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color1));


    }


    @Override
    public void onClick(View v) {

        if (v == btnDatePicker2) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            beginTime.set(mYear, mMonth, mDay);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }


        if (v == btnTimePicker2) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            beginTime.set(mYear, mMonth, mDay, mHour, mMinute);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                            updateTime(hourOfDay, minute);
                            txtTime.setText(aTime);
//                            sdate = txtDate.getText().toString() + " " + aTime;
                            sdate=txtDate.getText().toString();

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

    }
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();


    }


    private void Events() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AdditionalActivity.this);
        String companynURL=preferences.getString("companynURL","");
        String UPDATE_URL = companynURL + "/prospect_app_dz/additional_task_dz.php?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            Toast.makeText(AdditionalActivity.this, "Task Added Successfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(AdditionalActivity.this,AdditionalActivity.class);
                            startActivity(intent);
                            add_edittext1.setText("");
                            txtDate.setText("");
                           // txtTime.setText("");
                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(AdditionalActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rep_id", userName);
                map.put("desc", desc);
                map.put("date", sdate);
                return map;


            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AdditionalActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent view) {
        if(getCurrentFocus()!=null){
            InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return super.dispatchTouchEvent(view);
    }

}



