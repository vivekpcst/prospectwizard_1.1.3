package com.dzo.prospectWizard.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.R;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactActivity extends BaseActivity {

    String firstNames = "";
    String lastNames = "";
    String displays = "";
    String displays3 = "";
    String rep_id;
    TextView contactsName, contactPhone;
    Button pickContacts;
    final int CONTACT_PICK_REQUEST = 1000;
    RelativeLayout contactlayout;
    String tkn;
    String token;
    SharedPreferences sharedPreferences,sharedPreferences1;
    int Color1;
    String Font;
    Typeface face;
    ArrayList<Contact> selectedContacts;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.contact_main, frameLayout);

        contactlayout=(RelativeLayout)findViewById(R.id.contactlayout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        contactlayout.setBackgroundColor(Color1);

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(getAssets(), Font);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        Intent intentContactPick = new Intent(ContactActivity.this, ContactsPickerActivity.class);
        ContactActivity.this.startActivityForResult(intentContactPick, CONTACT_PICK_REQUEST);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ContactActivity.this);
        rep_id = preferences.getString("UserName", "");
        tkn = FirebaseInstanceId.getInstance().getToken();
        token = preferences.getString("Token", "");
        contactsName = (TextView) findViewById(R.id.txt_name);
        contactPhone = (TextView) findViewById(R.id.txt_phone);

        if (face != null) {
            contactsName.setTypeface(face);
            contactPhone.setTypeface(face);
        }
        pickContacts = (Button) findViewById(R.id.btn_pick_contacts);
        pickContacts.setTypeface(face);
        pickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendContacts();
            }
        });
    }
    private void sendContacts() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ContactActivity.this);
        String companynURL=preferences.getString("companynURL","");

         String UPDATE_URL = companynURL +"/prospect_app_dz/addcontact_dz.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!response.contains("notok")) {
                            Toast.makeText(ContactActivity.this, selectedContacts.size()+" Records have been Sent to Server Successfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ContactActivity.this, ContactActivity.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(ContactActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ContactActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("unique_id", rep_id);
                map.put("rep_id", rep_id);
                map.put("phone", displays);
                map.put("firstname", firstNames);
                map.put("lastname", lastNames);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ContactActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK) {

            selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");
            String display = "";
            String display1 = "";
            for (int i = 0; i < selectedContacts.size(); i++) {
                if (i < (selectedContacts.size())) {
                    display += selectedContacts.get(i).getPhone() + ",\n";
                    display1 += selectedContacts.get(i).getName() + "\n";
                    displays += selectedContacts.get(i).getPhone() + ",";

                    String names = selectedContacts.get(i).getName();
                    String[] namearray = names.split("[\\s\\xA0]+");

                    if (namearray.length > 3) {
                        firstNames += namearray[0] + " " + namearray[1] + ",";
                        lastNames += namearray[2] + " " + namearray[3] + ",";

                    } else if (namearray.length > 2) {
                        firstNames += namearray[0] + " " + namearray[1] + ",";
                        lastNames += namearray[2] + ",";

                    } else if (namearray.length > 1) {
                        firstNames += namearray[0] + ",";
                        lastNames += namearray[1] + ",";

                    } else {
                        firstNames += namearray[0] + ",";
                        lastNames += ",";
                    }

                }
                contactsName.setText("" + display);
                contactPhone.setText("" + display1);
            }
        }
    }
}

