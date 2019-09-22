package com.dzo.prospectWizard.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.provider.CalendarContract.Events;
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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CalenderActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout calenderlayout;
    SharedPreferences sharedPreferences, sharedPreferences1;
    int Color1;
    String Font;
    Typeface face;
    Button btnDatePicker, btnTimePicker;
    Button btnDatePicker1,btnTimePicker1,send;
    EditText txtDate, txtTime,txtDate1,txtTime1,setEvent,Title;
    String desc,sdate,edate, aTime,calTitle,sdate1,edate1,bTime,txdate3,txtdate4;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar beginTime = Calendar.getInstance();
    Calendar endTime = Calendar.getInstance();

    Context context;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Calendar Event");
        getLayoutInflater().inflate(R.layout.activity_calender, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        calenderlayout =(LinearLayout) findViewById(R.id.calenderlayout);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalenderActivity.this);
        userName = preferences.getString("UserName", "");

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnDatePicker1=(Button)findViewById(R.id.bt_date);
        btnTimePicker1=(Button)findViewById(R.id.bt_time);

        txtDate1=(EditText)findViewById(R.id.n_date);
        txtTime1=(EditText)findViewById(R.id.n_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        setEvent=(EditText)findViewById(R.id.add_edittext);
        Title=(EditText)findViewById(R.id.titlecal);
        send=(Button)findViewById(R.id.stEvent);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnDatePicker1.setOnClickListener(this);
        btnTimePicker1.setOnClickListener(this);
        setEvent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    send.performClick();
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    desc  =   setEvent.getText().toString();
                    calTitle = Title.getText().toString();
                    final ContentValues event = new ContentValues();
                    Calendar beginTime = Calendar.getInstance();
                    Log.e("sdate", sdate);
                    int year, month;
                    int day;
                    int hour, min, min1;
                    String[] parts = sdate.split("-");
                    String yearTime = parts[2];
                    String[] yeare = yearTime.split(" ");
                    year=Integer.parseInt(yeare[0]);
                    month=Integer.parseInt(parts[1]);
                    String xx = "PM";
                    day=Integer.parseInt(parts[0]);
                    String[] time = yeare[1].split(":");
                    hour = Integer.parseInt(time[0]);
                    min = Integer.parseInt(time[1]);
                    String AMPM = yeare[2];
                    if(AMPM.equals(xx)){
                        hour = hour+12;
                    }
                    beginTime.set(day, month-1, year, hour, min);
                    int year1, month1, hour1;
                    int day1;
                    String[] parts1 = edate.split("-");
                    String yearTime1 = parts1[2];
                    String[] year11 = yearTime1.split(" ");
                    year1=Integer.parseInt(year11[0]);
                    month1=Integer.parseInt(parts1[1]);
                    day1=Integer.parseInt(parts1[0]);
                    String[] time1 = year11[1].split(":");
                    hour1 = Integer.parseInt(time1[0]);
                    min1 = Integer.parseInt(time1[1]);
                    String AMPM1 = year11[2];
                    if(AMPM1.equals(xx)) {
                        hour1 = hour1+12;
                    }
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(day1, month1-1, year1, hour1, min1);
                    event.put(Events.CALENDAR_ID, 1);
                    event.put(Events.TITLE, calTitle);
                    event.put(Events.DESCRIPTION, desc);
                    event.put(Events.DTSTART, beginTime.getTimeInMillis());
                    event.put(Events.DTEND, endTime.getTimeInMillis());
                    event.put(Events.ALL_DAY, 0);
                    event.put(Events.HAS_ALARM, 1);
                    String timeZone = TimeZone.getDefault().getID();
                    event.put(Events.EVENT_TIMEZONE, timeZone);
                    Uri baseUri;
                    baseUri = Uri.parse("content://com.android.calendar/events");
                    getContentResolver().insert(baseUri, event);
                    if (Title.getText().length() == 0) {
                        Title.setError("Field cannot be left blank.");
                    } else if(setEvent.getText().length()==0){
                        setEvent.setError("Field cannot be left blank.");
                    }else {
                        Events();
                    }
                }catch (Exception e){
                  Toast.makeText(getApplicationContext(),"Event not added. ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        calenderlayout.setBackgroundColor(Color1);
        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(getAssets(), Font);
        }
        if (face != null) {
            send.setTypeface(face);
            setEvent.setTypeface(face);
            Title.setTypeface(face);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color1));


    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            beginTime.set(mYear,mMonth,mDay);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String s;

                            if((monthOfYear<=9)&&(dayOfMonth>9&&dayOfMonth!=9)) {
                                s = new StringBuilder().append(year).append("-").append("0").append(monthOfYear+1).append("-").append(dayOfMonth).toString();
                            }
                            else if((dayOfMonth<=9)&&(monthOfYear<=9)){
                                s=new StringBuilder().append(year).append("-").append("0").append(monthOfYear+1).append("-").append("0").append(dayOfMonth).toString();

                            }
                            else if((dayOfMonth<=9)&&(monthOfYear>9&&monthOfYear!=9)){
                                s=new StringBuilder().append(year).append("-").append(monthOfYear+1).append("-").append("0").append(dayOfMonth).toString();

                            }
                            else{
                                s=new StringBuilder().append(year).append("-").append(monthOfYear+1).append("-").append(dayOfMonth).toString();

                            }






                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            txtDate1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            txdate3=s;
                            txtdate4=s;


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if(v == btnDatePicker1) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            endTime.set(mYear,mMonth,mDay);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String m;
                            if((monthOfYear<=9)&&(dayOfMonth>9&&dayOfMonth!=9)) {
                                m = new StringBuilder().append(year).append("-").append("0").append(monthOfYear+1).append("-").append(dayOfMonth).toString();
                            }
                            else if((dayOfMonth<=9)&&(monthOfYear<=9)){
                                m=new StringBuilder().append(year).append("-").append("0").append(monthOfYear+1).append("-").append("0").append(dayOfMonth).toString();

                            }
                            else if((dayOfMonth<=9)&&(monthOfYear>9&&monthOfYear!=9)){
                                m=new StringBuilder().append(year).append("-").append(monthOfYear+1).append("-").append("0").append(dayOfMonth).toString();

                            }
                            else{
                                m=new StringBuilder().append(year).append("-").append(monthOfYear+1).append("-").append(dayOfMonth).toString();

                            }


                            txtDate1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            txtdate4=m;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnTimePicker) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            beginTime.set(mYear, mMonth,mDay,mHour,mMinute);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                            updateTime(hourOfDay,minute);
                            txtTime.setText(aTime);
                            sdate =txtDate.getText().toString()+" "+aTime;
                            sdate1 = txdate3+" "+bTime;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        else if(v == btnTimePicker1){
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            endTime.set(mYear, mMonth,mDay,mHour,mMinute);

            System.out.print(c);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime1.setText(hourOfDay + ":" + minute);

                             updateTime(hourOfDay,minute);
                            txtTime1.setText(aTime);
                            edate = txtDate1.getText().toString() + " " + aTime;
                            edate1 = txtdate4+" "+bTime;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        }

    }
    private void updateTime(int hours, int mins) {
        int hh,mm;
        hh=hours;
        mm=mins;
     String sec="00";
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
                .append(minutes) .append(" ").append(timeSet).toString();
        if((hh<=9)&&(mm>9&&mm!=9)){
            bTime=new StringBuilder().append("0").append(hh).append(':').append(mm).append(':').append(sec).toString();

        }
        else if((mm<=9)&&(hh<=9)){
            bTime=new StringBuilder().append("0").append(hh).append(':').append("0").append(mm).append(':').append(sec).toString();

        }
        else if((mm<=9)&&(hh>9&&hh!=9)){
            bTime=new StringBuilder().append(hh).append(':').append("0").append(mm).append(':').append(sec).toString();

        }
        else{
            bTime=new StringBuilder().append(hh).append(':').append(mm).append(':').append(sec).toString();

        }

    }

    private void Events() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalenderActivity.this);
        String companynURL=preferences.getString("companynURL","");

        String UPDATE_URL = companynURL+"/prospect_app_dz/events_dz.php?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            Toast.makeText(CalenderActivity.this, "Event Added Successfully", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(CalenderActivity.this,CalenderActivity.class);
                            startActivity(intent);
                            setEvent.setText("");
                            Title.setText("");
                            txtDate1.setText("");
                            txtTime1.setText("");
                            txtDate.setText("");
                            txtTime.setText("");
                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(CalenderActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rep_id", userName);
                map.put("desc", desc);
                map.put("sdate", sdate1);
                map.put("edate", edate1);
                map.put("title", calTitle);
                return map;


            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CalenderActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    public static void saveCalendar(Context ctx, String title,
                                    String description, String location, Calendar cal_start,
                                    Calendar cal_end) {

        Cursor cursor = ctx.getContentResolver()
                .query(Uri.parse("content://com.android.calendar/calendars"),
                        new String[] { "_id", "displayname" }, "selected=1",
                        null, null);
        cursor.moveToFirst();
        String[] CalNames = new String[cursor.getCount()];
        int[] CalIds = new int[cursor.getCount()];
        for (int i = 0; i < CalNames.length; i++) {
            CalIds[i] = cursor.getInt(0);
            CalNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }

        cursor.close();

        ContentValues event = new ContentValues();
        event.put("calendar_id", CalIds[0]);
        event.put("title", title);
        event.put("description", description);
        event.put("eventLocation", location);
        event.put("dtstart", cal_start.getTimeInMillis());
        event.put("dtend", cal_end.getTimeInMillis());
        event.put("hasAlarm", 1);

        Uri eventsUri = Uri.parse("content://com.android.calendar/events");
        Uri newEvent = ctx.getContentResolver().insert(eventsUri, event);

        long eventID = Long.parseLong(newEvent.getLastPathSegment());

        ContentValues cv_alarm = new ContentValues();
        cv_alarm.put("event_id", eventID);
        cv_alarm.put("method", 1);
        cv_alarm.put("minutes", 120);
        ctx.getContentResolver()
                .insert(Uri.parse("content://com.android.calendar/reminders"),
                        cv_alarm);

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



