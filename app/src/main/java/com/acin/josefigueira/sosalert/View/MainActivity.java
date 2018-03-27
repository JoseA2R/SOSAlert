package com.acin.josefigueira.sosalert.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.acin.josefigueira.sosalert.R;

public class MainActivity extends AppCompatActivity {

   Button _btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        _btn = findViewById(R.id.app_start_btn);

       _btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FormActivity.class));
            }
        });

    }

    public void onClick(View view) {
    }
}
