package com.dzo.prospectWizard.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dzo.prospectWizard.R;


public class ContactsPickerActivity extends BaseActivity {

    ListView contactsChooser;
    Button btnDone;
    EditText txtFilter;
    TextView txtLoadInfo;
    ContactsListAdapter contactsListAdapter;
    ContactsLoader contactsLoader;
    RelativeLayout contactpicker;
    SharedPreferences sharedPreferences,sharedPreferences1;
    int Color1;
    String Font;
    Typeface face;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_contacts_picker, frameLayout);
        contactpicker=(RelativeLayout)findViewById(R.id.contactpicker);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        contactpicker.setBackgroundColor(Color1);

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Font = sharedPreferences1.getString("item", null);
        face = null;
        if (Font != null) {
            face = Typeface.createFromAsset(getAssets(), Font);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        contactsChooser = (ListView) findViewById(R.id.lst_contacts_chooser);
        btnDone = (Button) findViewById(R.id.btn_done);
        txtFilter = (EditText) findViewById(R.id.txt_filter);
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtFilter, InputMethodManager.SHOW_IMPLICIT);
        txtLoadInfo = (TextView) findViewById(R.id.txt_load_progress);

        if (face != null) {
          txtFilter.setTypeface(face);
            btnDone.setTypeface(face);
        }
        contactsListAdapter = new ContactsListAdapter(this,new ContactsList(),Font,face);
        contactsChooser.setAdapter(contactsListAdapter);
        loadContacts("");
        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactsListAdapter.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contactsListAdapter.selectedContactsList.contactArrayList.isEmpty()){

                  setResult(RESULT_CANCELED);

                }
                else{
                    Intent resultIntent = new Intent();
                    resultIntent.putParcelableArrayListExtra("SelectedContacts", contactsListAdapter.selectedContactsList.contactArrayList);
                    setResult(RESULT_OK,resultIntent);

                }
                finish();

            }
        });
    }



    private void loadContacts(String filter){

        if(contactsLoader!=null && contactsLoader.getStatus()!= AsyncTask.Status.FINISHED){
            try{
                contactsLoader.cancel(true);
            }catch (Exception e){

            }
        }
        if(filter==null) filter="";

        try{
            contactsLoader = new ContactsLoader(this,contactsListAdapter);
            contactsLoader.txtProgress = txtLoadInfo;
            contactsLoader.execute(filter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
