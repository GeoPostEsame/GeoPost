package com.example.alfredosansalone.geopost.intent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.alfredosansalone.geopost.R;

public class AggAmici extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agg_amici);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //speriamo funzioni
    public void Aggiungi(View v) {
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, AmiciSeguiti.class);
        startActivity(intent);
    }
}
