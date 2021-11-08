package com.wuwl.mynote.ui;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使得状态栏字体变黑
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
