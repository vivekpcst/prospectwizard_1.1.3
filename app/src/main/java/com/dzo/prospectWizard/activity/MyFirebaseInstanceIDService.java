package com.dzo.prospectWizard.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyFirebaseInstanceIDService.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Token", refreshedToken);
        editor.commit();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

    }
}