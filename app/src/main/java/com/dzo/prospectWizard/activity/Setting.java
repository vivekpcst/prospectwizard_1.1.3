package com.dzo.prospectWizard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dzo.prospectWizard.R;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Setting extends Activity {

    String item;
    EditText edtText;
    int mDefaultColor;
    android.support.constraint.ConstraintLayout setting;
    Button btncolor;
    ImageView Back;
    int Color1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mDefaultColor = -7403255;
        item = "NoFont";
        edtText = (EditText) findViewById(R.id.enterText);
        setting=(android.support.constraint.ConstraintLayout)findViewById(R.id.setting);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        setting.setBackgroundColor(Color1);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Color1 = sharedPreferences.getInt("Color", -7403255);
        setting.setBackgroundColor(Color1);

        btncolor=(Button)findViewById(R.id.btnSelectColor);
        Back=(ImageView)findViewById(R.id.img1);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Setting.this,LoginActivity.class);
                startActivity(i);
            }
        });
        btncolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        Button ApplyColorBtn=(Button)findViewById(R.id.btnApplyColor);
        ApplyColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDefaultColor == -7403255){
                    Toast.makeText(getApplicationContext(),"Please Select Color",Toast.LENGTH_LONG).show();
                }else {
                    setting.setBackgroundColor(mDefaultColor);
                    btncolor.setBackgroundColor(-1645344);
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Color", mDefaultColor);
                    editor.commit();
                }
            }
        });


        final Spinner spinner = (Spinner) findViewById(R.id.spinnerFontName);
        spinner.setPrompt("Apply");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    item = spinner.getSelectedItem().toString();


                    Typeface face= Typeface.createFromAsset(getAssets(),item);
                    edtText.setTypeface(face);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {



            }

        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Select Font");
        Button ApplyFontBtn=(Button)findViewById(R.id.btnSetFontName);
        ApplyFontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item == "NoFont"){
                    Toast.makeText(getApplicationContext(),"Please Select Font",Toast.LENGTH_LONG).show();
                }else {
                    SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString("item", item);
                    Log.e("item", item);
                    editor.commit();
                    editor.apply();
                }
            }
        });


    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker=new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor=color;
                btncolor.setBackgroundColor(mDefaultColor);
            }
        });
        colorPicker.show();

    }

}
