package com.dzo.prospectWizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dzo.prospectWizard.activity.HomeActivity;

public class CallAndSms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_and_sms);

        String call_sms= PreferenceManager.getDefaultSharedPreferences(this).getString("call_sms","");
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        switch (call_sms){
            case "sms":{
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body","Type here...");
                editor.putString("call_sms","");
                editor.apply();
                startActivity(sendIntent);
                break;
            }
            case "call":{
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+call_sms));
                editor.putString("call_sms","");
                editor.apply();
                startActivity(intent);
                break;
            }
            /*default:{
                startActivity(new Intent(this, HomeActivity.class));
            }*/
        }
    }
}
