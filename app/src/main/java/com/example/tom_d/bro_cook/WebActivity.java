package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        String url = intent.getStringExtra("source");

        mWebView = findViewById(R.id.webView);
        mWebView.setVisibility(View.VISIBLE);
        mWebView.loadUrl(url);
    }
    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(WebActivity.this, RecipeImageActivity.class));
    }
}
