package com.example.alfredosansalone.geopost.intent;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.alfredosansalone.geopost.R;

public class Profilo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void Logout(View v) {
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
