package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;


/**
 * Displays the webview for the recipe source url link
 */

public class WebActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        // Creates intent to receive necessary data
        Intent intent = getIntent();
        String url = intent.getStringExtra("source");

        // Creates the WebView with the right url
        mWebView = findViewById(R.id.webView);
        mWebView.setVisibility(View.VISIBLE);
        mWebView.loadUrl(url);
    }
    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = getIntent();
        String input = intent.getStringExtra(EXTRA_ID);
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);
        detailIntent.putExtra(EXTRA_ID,input);
        startActivity(detailIntent);
    }
}
