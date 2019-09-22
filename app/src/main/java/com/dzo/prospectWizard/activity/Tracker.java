package com.dzo.prospectWizard.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tracker extends BaseActivity implements View.OnClickListener, TrackerListAdapter.OnActionTrakingListener, EditTrackerFragment.OnFragmentInteractionListener {
    ConstraintLayout cPrevRec;
    RecyclerView rvTrackList;
    TrackerListAdapter adapter;
    String rep_id,Font;
    Typeface face;
    ArrayList<String> eid, repid, names, description, start_mileage, end_mileage, expense_amount;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tracker);
        getLayoutInflater().inflate(R.layout.activity_tracker, frameLayout);

        ConstraintLayout managelayout=findViewById(R.id.trackerC);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        if(managelayout != null) {
            managelayout.setBackgroundColor(Color1);
        }
        Font = sharedPreferences.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(getAssets(), Font);
        }

        if(face != null){
            ((TextView)findViewById(R.id.tvTrName)).setTypeface(face);
            ((TextView)findViewById(R.id.tvTrStMile)).setTypeface(face);
            ((TextView)findViewById(R.id.tvTrEndMile)).setTypeface(face);
            ((TextView)findViewById(R.id.tvTrDesc)).setTypeface(face);
            ((Button)findViewById(R.id.btnSubmitTrack)).setTypeface(face);

            ((EditText)findViewById(R.id.etStartingMile)).setTypeface(face);
            ((EditText)findViewById(R.id.etTrackName)).setTypeface(face);
            ((EditText)findViewById(R.id.etEndingMile)).setTypeface(face);
            ((EditText)findViewById(R.id.etDescTrack)).setTypeface(face);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        cPrevRec=findViewById(R.id.cPrevRec);
        findViewById(R.id.ibBackTrack).setOnClickListener(this);
        findViewById(R.id.tvBackTrack).setOnClickListener(this);
        findViewById(R.id.btnSubmitTrack).setOnClickListener(this);
        eid= new ArrayList<>();
        repid=new ArrayList<>();
        names=new ArrayList<>();
        description=new ArrayList<>();
        start_mileage=new ArrayList<>();
        end_mileage=new ArrayList<>();
        expense_amount=new ArrayList<>();
        rvTrackList=findViewById(R.id.rvTrackList);
        rvTrackList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new TrackerListAdapter(this,eid,names,description,start_mileage,end_mileage,expense_amount);
        rvTrackList.setAdapter(adapter);
        rep_id=PreferenceManager.getDefaultSharedPreferences(this).getString("rep_id",null);
        trackIt(rep_id, "", "", "", "", "retrieve", "");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ibBackTrack:
            case R.id.tvBackTrack:
                Intent intent=new Intent(this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnSubmitTrack:
                cPrevRec.setVisibility(View.VISIBLE);
                String starting_mile=((EditText)findViewById(R.id.etStartingMile)).getText().toString();
                String ending_mile=((EditText)findViewById(R.id.etEndingMile)).getText().toString();
                String description=((EditText)findViewById(R.id.etDescTrack)).getText().toString();
                String name=((EditText)findViewById(R.id.etTrackName)).getText().toString();
                String amount=((EditText)findViewById(R.id.etTrackAmount)).getText().toString();
                rep_id=PreferenceManager.getDefaultSharedPreferences(this).getString("rep_id",null);
                if(rep_id != null) {
                    if (name.isEmpty() || starting_mile.isEmpty() || ending_mile.isEmpty()) {
                        Toast.makeText(this, "Please fill both starting and ending miles. !", Toast.LENGTH_LONG).show();
                    } else {
                        trackIt(rep_id, name, starting_mile, ending_mile, description,"create", "");
                    }
                }else{
                    Toast.makeText(this, "You are not authorized !", Toast.LENGTH_LONG).show();
                }
                break;

        }

    }



    private void enableEditText(EditText etTxt) {
        etTxt.setEnabled(true);
        etTxt.setKeyListener(new AppCompatEditText(getApplicationContext()).getKeyListener());
    }

    private void disableEditText(EditText etTxt) {
        etTxt.setEnabled(false);
        etTxt.setKeyListener(null);
    }

    private void trackIt(final String rep_id, final String name, final String starting_mile, final String ending_mile, final String description, final String action,final String eid) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Tracker.this);
        String companynURL=preferences.getString("companynURL","");
//        "https://www.essentialprospector.com
        String url=companynURL+"/prospect_app_dz/mobile-expenses.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("expense deleted")){
                    trackIt(rep_id, name, starting_mile, ending_mile, description, "retrieve", eid);
                    Toast.makeText(Tracker.this, "Record deleted.", Toast.LENGTH_SHORT).show();
                }
                try {
                    JSONObject object=new JSONObject(response);
                    JSONObject meta=object.getJSONObject("meta");
                    JSONArray data=object.getJSONArray("data");
                    String status=meta.getString("status");
                    String sqlMsg=object.getString("sql_message");
                    if(sqlMsg.contains("expense item updated")){
                        Toast.makeText(Tracker.this, "Record updated.", Toast.LENGTH_SHORT).show();
                    }
                    if(status.equalsIgnoreCase("ok")){
                        String action=meta.getString("action");
                        if(action.equalsIgnoreCase("create") || action.equalsIgnoreCase("update") || action.equalsIgnoreCase("delete")) {
                            trackIt(rep_id, name, starting_mile, ending_mile, description, "retrieve", eid);
                            ((EditText)findViewById(R.id.etStartingMile)).setText("");
                            ((EditText)findViewById(R.id.etEndingMile)).setText("");
                            ((EditText)findViewById(R.id.etDescTrack)).setText("");
                            ((EditText)findViewById(R.id.etTrackName)).setText("");
                            ((EditText)findViewById(R.id.etTrackAmount)).setText("");
                        }
                    }
                    if(data != null){
                        showLastRecord(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Tracker.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("action",action);
                map.put("repid",rep_id);
                map.put("name",name);
                map.put("description",description);
                map.put("start_mileage",starting_mile);
                map.put("end_mileage",ending_mile);
                map.put("expense_amount","");
                map.put("expense_description","");
                map.put("eid",eid);
                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showLastRecord(JSONArray data) {
        try {
            names.clear();
            description.clear();
            start_mileage.clear();
            end_mileage.clear();
            eid.clear();
            expense_amount.clear();
        for(int i=0; i< data.length(); i++){
                JSONObject dataObj=data.getJSONObject(i);
            eid.add(dataObj.getString("eid"));
            names.add(dataObj.getString("name"));
            description.add(dataObj.getString("description"));
            start_mileage.add(dataObj.getString("start_mileage"));
            end_mileage.add(dataObj.getString("end_mileage"));
            expense_amount.add(dataObj.getString("expense_amount"));
        }
            adapter=new TrackerListAdapter(this,eid,names,description,start_mileage,end_mileage,expense_amount);
            rvTrackList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTrackerItemDelete(String eid) {
        trackIt(rep_id, "", "", "", "", "delete",eid);

    }

    @Override
    public void onTrackerEditItem(String eid) {
//        trackIt(rep_id, "", "", "", "", "update",eid);
        EditTrackerFragment dialog = new EditTrackerFragment();
//        EditTrackerFragment.newInstance(names.get(0),start_mileage.get(0),end_mileage.get(0),description.get(0));
        Bundle bundle = new Bundle();
        bundle.putString("tName", names.get(0));
        bundle.putString("tSMile", start_mileage.get(0));
        bundle.putString("tEMile", end_mileage.get(0));
        bundle.putString("tDesc", description.get(0));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "MyCustomDialog");
    }

    @Override
    public void onFragmentInteraction(String name, String stMile, String eMile, String desc) {
        trackIt(rep_id, name, stMile, eMile, desc, "update", eid.get(0));
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