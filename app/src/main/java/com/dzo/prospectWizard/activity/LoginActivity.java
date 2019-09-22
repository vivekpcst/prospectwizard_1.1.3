package com.dzo.prospectWizard.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.R;
import com.dzo.prospectWizard.WebActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 10 ;
    String statement,user,pass,rep_id;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editCompanyname;
    TextView textwww,textcom;
    private Button buttonLogin;
    private String password,companynURL;
    private String username;
    private String CompanyName;
    private String system,golu,sand;

    LinearLayout loginlayout;
    int Color1;
    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2;
    String tkn;
    Typeface face;
    String Font;
    TextView forget;
    CheckBox remember;
    final int CONTACT_PICK_REQUEST = 1000;

    private GoogleApiClient client;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        checkAndRequestPermissions();
        editTextUsername = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editCompanyname =(EditText)findViewById(R.id.cmpnyEmail);
        textwww=(TextView)findViewById(R.id.textwww);
        textcom=(TextView)findViewById(R.id.textcom);


        buttonLogin = (Button) findViewById(R.id.btnLogin);
        forget = (TextView) findViewById(R.id.forget);
        Button btnSetting=(Button)findViewById(R.id.btnSetting);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,Setting.class);
                startActivity(intent);
            }
        });




        loginlayout=(LinearLayout)findViewById(R.id.loginlayout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        loginlayout.setBackgroundColor(Color1);

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null){
            face= Typeface.createFromAsset(getAssets(),Font);
            editTextUsername.setTypeface(face);
            editTextPassword.setTypeface(face);
            textwww.setTypeface(face);
            textcom.setTypeface(face);
            buttonLogin.setTypeface(face);
        }

        sharedPreferences2=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sand=sharedPreferences2.getString("golu",system);
        golu =null;
        if(sand !=null){
            editCompanyname.setText(sand);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        user = preferences.getString("userName", "");
        pass = preferences.getString("password", "");
        companynURL=preferences.getString("companynURL","");
        rep_id =  preferences.getString("UserName", "");
        editTextUsername.setText(user);
        editTextPassword.setText(pass);
        buttonLogin.setOnClickListener(this);


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();




    }
    private void userLogin(String newURL) {

        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        system=editCompanyname.getText().toString().trim();
        CompanyName = "www."+system+".com";
        golu= system;

        sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString("golu", system);
        editor.commit();



        String LOGIN_URL = newURL + "/prospect_app_dz/login_dz.php?";

        tkn = FirebaseInstanceId.getInstance().getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ss",""+response);
                        if (!response.contains("notok")) {

                            try {
                                JSONObject obj=new JSONObject(response);
                                statement=obj.getString("statement");
                                statement=obj.getString("statement");
                                JSONObject stst = obj.getJSONObject("statement");

                                String UserStr = stst.get("rep_id").toString();
                                if (!UserStr.equals("Login Failed")){
                                    PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("rep_id",UserStr).apply();
                                    if (!stst.isNull("company")){
                                        String companyStr = stst.get("company").toString();
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("companyURL",companyStr);
                                        editor.apply();
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                                        startActivity(intent);
                                    }else {

                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("UserName",UserStr);
                                        editor.putString("userName",username);
                                        editor.putString("password",password);
                                        editor.apply();
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                          // editTextPassword.setText("");

                        } else {
                            editTextPassword.setError("Check Password");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Please Check Your InterNet Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username",username);
                map.put("password",password);
                map.put("companyName",CompanyName);
                map.put("device_type", "Android");
                map.put("device_token", tkn);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void Notification() {
        tkn = FirebaseInstanceId.getInstance().getToken();
        String url = companynURL+"/prospect_app_dz/login_dz.php?rep_id="+ statement.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.print(response);

                        if (!response.contains("notok")) {


                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("device_type", "Android");
                map.put("device_token", tkn);
                Log.e("dhiydsf",""+map);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (editTextUsername.getText().length() == 0) {
            editTextUsername.setError("Field cannot be left blank.");
        }
        else if (editCompanyname.getText().length() == 0) {
            editCompanyname.setError("Field cannot be left blank.");
        }
        else if (editTextPassword.getText().length() == 0) {
            editTextPassword.setError("Field cannot be left blank.");
        } else
        {
            systemWebAddress();
        }


    }
public void systemWebAddress() {
    system=editCompanyname.getText().toString().trim();
    CompanyName = "www."+system+".com";
    golu= system;
   // https://www.essentialprospector.com/prospect_app_dz/company_url_dz.php?companyName=www.essentialprospector.com

   // String urlSys = "https://www.magicprospector.com/prospect_app_dz/company_url_dz.php?";
    String urlSys="https://www.essentialprospector.com/prospect_app_dz/company_url_dz.php?";

    sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences2.edit();
    editor.putString("golu", system);
    editor.commit();



    tkn = FirebaseInstanceId.getInstance().getToken();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSys,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("ss",""+response);
                    if (!response.contains("NA")) {

                        try {


                            JSONObject obj=new JSONObject(response);
                            statement=obj.getString("statement");
                            statement=obj.getString("statement");
                            JSONObject stst = obj.getJSONObject("statement");
                            String companyname = stst.getString("company");
                             companynURL = stst.getString("url");
                            if (companynURL == "NA"){
                            Toast.makeText(getApplicationContext(),"System web add does not exist",Toast.LENGTH_SHORT).show();
                            }else {
                                userLogin(companynURL);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("companynURL", companynURL);
                                editor.commit();

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // editTextPassword.setText("");

                    } else {
                        editTextPassword.setError("Check Password");
                        Toast.makeText(getApplicationContext(),"System web address does not exist",Toast.LENGTH_SHORT).show();

                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Please Check Your InterNet Connection", Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> map = new HashMap<String, String>();
            map.put("companyName",CompanyName);
            return map;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();


        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        loginlayout.setBackgroundColor(Color1);
        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null){
            face= Typeface.createFromAsset(getAssets(),Font);
            editTextUsername.setTypeface(face);
            editCompanyname.setTypeface(face);
            editTextPassword.setTypeface(face);
            textwww.setTypeface(face);
            textcom.setTypeface(face);
            buttonLogin.setTypeface(face);
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkAndRequestPermissions() {
//        int permissionSendMessage = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE);
        int contactpermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camerpermition = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA);
        int readcontact = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CONTACTS);
        int record = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECORD_AUDIO);
        int calRead = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CALENDAR);
        int calWrite=ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.WRITE_CALENDAR);

        List<String> listPermissionsNeeded = new ArrayList<>();
       /* if(call_phone !=PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }*/
        if (contactpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }if (contactpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALENDAR);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (calWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CALENDAR);
        }
        if (camerpermition != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readcontact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (record != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (calRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALENDAR);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {


            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {


                    if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED ) {

                        }
                    } else if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        }
                    } /*else if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        }

                    }*/
                    else if (permissions[i].equals(Manifest.permission.CAMERA)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        }
                    }
                    else if (permissions[i].equals(Manifest.permission.READ_CONTACTS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        }
                    }
                    else if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        }
                    }
                    /*else if (permissions[i].equals(Manifest.permission.SEND_SMS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        }
                    }*/else if (permissions[i].equals(Manifest.permission.READ_CALENDAR)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        }
                    }/*else if (permissions[i].equals(Manifest.permission.CALL_PHONE)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        }
                    }*/
                }

            }


        }
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