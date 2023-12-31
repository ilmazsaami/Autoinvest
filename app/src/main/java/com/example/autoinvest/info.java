package com.example.autoinvest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.autoinvest.infopager.infopage1;
import com.example.autoinvest.infopager.infopage2;
import com.example.autoinvest.infopager.infopage3;
import com.example.autoinvest.users.LoginActivity;
import com.example.autoinvest.users.RegisterActivity;
import com.google.android.material.tabs.TabLayout;



public class info extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tb;
    PagerAdapter pagerAdapter;
    public Button signin_bt,signup_bt;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        viewPager = findViewById(R.id.info_pager);
        tb = findViewById(R.id.nav_dots);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.add(new infopage1(),"page1");
        pagerAdapter.add(new infopage2(),"page2");
        pagerAdapter.add(new infopage3(),"page3");
        viewPager.setAdapter(pagerAdapter);

        tb.setupWithViewPager(viewPager,true);

        signin_bt=findViewById(R.id.signin_btn);
        signup_bt=findViewById(R.id.signup_btn);

        signin_bt.setBackgroundColor(Color.rgb(230,59,81));
        signup_bt.setBackgroundColor(Color.rgb(230,59,81));

        signup_bt.setOnClickListener(this);
        signin_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signup_btn)
        {
           Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }
        if(v.getId()==R.id.signin_btn)
        {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }
}