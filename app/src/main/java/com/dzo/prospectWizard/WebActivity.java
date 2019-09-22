package com.dzo.prospectWizard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

    WebView webView;
SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Login");
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        setContentView(R.layout.activity_web);

        String CompanyURL;

         preferences = PreferenceManager.getDefaultSharedPreferences(WebActivity.this);

        CompanyURL = preferences.getString("companyURL","No");

        String url= CompanyURL;
        webView=(WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }

    }
}
