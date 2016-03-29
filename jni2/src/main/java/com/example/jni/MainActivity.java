package com.example.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public  void  btn(View view){

        String  msg = getHello();
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

    }
    public  native  String getHello();
}
