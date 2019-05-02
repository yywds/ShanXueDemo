package com.example.administrator.onlinelearning.Page.Test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.onlinelearning.R;

public class TestMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studycircle);
        //从activity跳转到fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.tv,new SlideFragment()).addToBackStack(null).commit();

    }
}
