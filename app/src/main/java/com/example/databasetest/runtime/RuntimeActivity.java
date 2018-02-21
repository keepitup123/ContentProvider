package com.example.databasetest.runtime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.databasetest.R;



public class RuntimeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mbtnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime);
        mbtnCall = findViewById(R.id.btn_call);
        mbtnCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(ContextCompat.checkSelfPermission(RuntimeActivity.this,Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RuntimeActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            call();
        }
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10000"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "权限请求失败", Toast.LENGTH_SHORT).show();
                }
                break;
           // default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
