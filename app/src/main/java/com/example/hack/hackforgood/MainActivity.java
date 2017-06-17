package com.example.hack.hackforgood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button donateButton;
    private Button broadcastButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        donateButton = (Button) findViewById(R.id.donateButton);
        broadcastButton = (Button) findViewById(R.id.broadcastButton);
    }

}
