package com.dzo.prospectWizard.activity;



import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.R;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;

public class ActivityMain extends Activity {
    String Action = "";
    String phone = "";
    String Sms = "";
    String Name = "";
    String Message, rep_id, tkn;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =10000 ;
    String[] namearray;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        Message = b.getString("name");

        tkn = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityMain.this);
        rep_id = preferences.getString("UserName", "");
        namearray = Message.split(",");
        if (namearray.length > 3) {
            Action += namearray[0];
            phone += namearray[1];
            Sms += namearray[2];
            Name += namearray[3];


        }
        else if (namearray.length > 2) {
            Action += namearray[0];
            phone += namearray[1];
            Name += namearray[2];

        }
        userMessage();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void userMessage() {
        if (namearray != null && namearray.length > 3) {
            sendEmail();
            Email();
            finish();
        } else if (namearray != null && namearray.length > 2) {
            if (Action.contains("Please CALL")) {
                call();
                Update();
                finish();

            } else {



                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, Name, null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                    Update();
                    finish();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }
    }
    private void call() {
        Intent i = new Intent(Intent.ACTION_CALL);
        String p = "tel:"+phone;
        i.setData(Uri.parse(p));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;

        }
        startActivity(i);
    }


    protected void sendEmail() {
        String[] TO = {phone};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Name);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Sms);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActivityMain.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void Update() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityMain.this);
        String companynURL=preferences.getString("companynURL","");


        String UPDATE_URL = companynURL + "/prospect_app_dz/dialer_delete_dz.php";



        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("notok")) {
                            Toast.makeText(ActivityMain.this, "Upload Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityMain.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rep_id", rep_id);
                map.put("phone", phone);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityMain.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
    private void Email() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityMain.this);
        String companynURL=preferences.getString("companynURL","");
        String UPDATE_URL = companynURL + "/prospect_app_dz/dialer_delete_dz.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("notok")) {
                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityMain.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rep_id", rep_id);
                map.put("email", phone);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityMain.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
}