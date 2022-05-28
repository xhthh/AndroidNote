package com.xht.androidnote.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.xht.androidnote.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}