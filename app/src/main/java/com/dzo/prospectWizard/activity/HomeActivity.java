package com.dzo.prospectWizard.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dzo.prospectWizard.R;
import com.dzo.prospectWizard.adapters.ListViewAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VinayDzo on 10/1/2016.
 */
public class HomeActivity extends BaseActivity {
    public static final String JSON_URL = "https://www.magicprospector.com/prospect_app_dz/dialer_records_dz.php?rep_id=";
    private static final String UPDATE_URL = "https://www.magicprospector.com/prospect_app_dz/update_records_dz.php?";
    String tkn;

    LinearLayout homelayout;
    int Color1;
    String Font;
    Typeface face;
    SharedPreferences sharedPreferences, sharedPreferences1;
    ContactList contactList;
    List<ContactList> contactListForAddingData;

    ListViewAdapter CustomHomeAdapterData;
    String arrayListForProf_mod;
    String arrayListForPhone;


    @RequiresApi(api = Build.VERSION_CODES.M)

    private ListView list;
    String userName;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.home_activity, frameLayout);
        tkn = FirebaseInstanceId.getInstance().getToken();
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        getSupportActionBar().setTitle("Home");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        userName = preferences.getString("UserName", "");
        System.out.println("UserName : " + userName);

        list = (ListView) findViewById(R.id.list);
        homelayout = (LinearLayout) findViewById(R.id.homelayout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        homelayout.setBackgroundColor(Color1);

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(getAssets(), Font);
        }
        contactListForAddingData = new ArrayList<>();

        CustomHomeAdapterData = new ListViewAdapter(this, R.layout.home_list_adapter, contactListForAddingData, face);

        sendData();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_delete);
        item.setVisible(false);
        return true;
    }

    public void sendData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        String companynURL=preferences.getString("companynURL","");

        String url =companynURL +"/prospect_app_dz/dialer_records_dz.php?rep_id=" + userName;
        Log.e("url", "" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        Log.e("Ar-res", "" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (response.contains("null")) {
                                Toast.makeText(HomeActivity.this, "No Record Found. Click on Home button to Navigate the other items.", Toast.LENGTH_SHORT).show();

                            } else {
                                contactListForAddingData = new ArrayList<>();

                                JSONArray jsonList = jsonObject.getJSONArray("statement");

                                JSONObject contactData = null;
                                contactListForAddingData.clear();
                                for (int i = 0; i < jsonList.length(); i++) {

                                    contactData = jsonList.getJSONObject(i);

                                    String contact_id = contactData.getString("contact_id");
                                    String firstname = contactData.getString("firstname");
                                    String lastname = contactData.getString("lastname");
                                    String notes = contactData.getString("notes");
                                    String action = contactData.getString("action");
                                    Log.e("acton",action);
                                    String pref_mode = contactData.getString("pref_mode");

                                    String phone_num;
                                    if (pref_mode.equals("TEXT")){
                                        phone_num = contactData.getString("text");
                                    }else {
                                        phone_num = contactData.getString("phone");
                                    }

                                    contactList = new ContactList();
                                    contactList.setContactId(contact_id);
                                    contactList.setFirstName(firstname);
                                    contactList.setLastName(lastname);
                                    contactList.setNotes(notes);
                                    contactList.setAction(action);
                                    contactList.setpref_mode(pref_mode);
                                    contactList.setPhone(phone_num);


                                    contactListForAddingData.add(contactList);

                                }
                                System.out.print(contactListForAddingData.size());
                                int aaaa = contactListForAddingData.size();

                                CustomHomeAdapterData.addAll(contactListForAddingData);
                                list.setAdapter(CustomHomeAdapterData);

                                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                                list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                                    @Override
                                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                                        final int checkedCount = list.getCheckedItemCount();
                                        System.out.println("Selected : " + checkedCount);
                                        System.out.println("position : " + position);
                                        mode.setTitle(checkedCount + " Selected");
                                        CustomHomeAdapterData.toggleSelection(position);
                                    }

                                    @Override
                                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                        mode.getMenuInflater().inflate(R.menu.main, menu);
                                        MenuItem item = menu.findItem(R.id.action_delete);
                                        item.setVisible(true);

                                        return true;
                                    }

                                    @Override
                                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.action_delete:

                                                arrayListForPhone = "";
                                                arrayListForProf_mod = "";

                                                SparseBooleanArray selected = CustomHomeAdapterData
                                                        .getSelectedIds();
                                                for (int i = (selected.size() - 1); i >= 0; i--) {

                                                    if (selected.valueAt(i)) {

                                                        ContactList selecteditem = CustomHomeAdapterData
                                                                .getItem(selected.keyAt(i));

                                                        if (arrayListForPhone.equals("")){
                                                            arrayListForPhone = selecteditem.getPhone();
                                                            arrayListForProf_mod = selecteditem.getpref_mode();
                                                        }else {
                                                            arrayListForPhone = arrayListForPhone + "," + selecteditem.getPhone();
                                                            arrayListForProf_mod = arrayListForProf_mod  + "," +  selecteditem.getpref_mode();
                                                        }

                                                        CustomHomeAdapterData.remove(selecteditem);
                                                    }
                                                }
                                                mode.finish();
                                                DeleteRecordMult();
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }

                                    @Override
                                    public void onDestroyActionMode(ActionMode mode) {
                                        CustomHomeAdapterData.removeSelection();

                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Unable to Process this request, Please try again later.", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("rep_id", userName);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

        Log.e("User Profile", "" + stringRequest);

    }



    private void DeleteRecordMult() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        String companynURL=preferences.getString("companynURL","");
        String url = companynURL+"/prospect_app_dz/multidel_dz.php?rep_id=" + userName + "&phone=" + arrayListForPhone + "&pref_mode=" + arrayListForProf_mod ;

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("null") ) {

                            Toast.makeText(HomeActivity.this, "No Record Found. Click on Home button to Navigate the other items.", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.print(response);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Please Check Your InterNet Connection", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

}

