package com.example.databasetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.databasetest.runtime.RuntimeActivity;
import com.example.databasetest.visitprovider.VisitActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mbtnRun,mbtnVisit,mbtnProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbtnRun = findViewById(R.id.btn_runtimepromission);
        mbtnVisit = findViewById(R.id.btn_visit);
        mbtnProvider = findViewById(R.id.btn_myprovider);
        mbtnProvider.setOnClickListener(this);
        mbtnVisit.setOnClickListener(this);
        mbtnRun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_runtimepromission:
                intent = new Intent(MainActivity.this,RuntimeActivity.class);
                break;
            case R.id.btn_visit:
                intent = new Intent(MainActivity.this, VisitActivity.class);
                break;
            case R.id.btn_myprovider:
                intent = new Intent(MainActivity.this,DatabaseTestActivity.class);
                break;
        }
        startActivity(intent);
    }
}
