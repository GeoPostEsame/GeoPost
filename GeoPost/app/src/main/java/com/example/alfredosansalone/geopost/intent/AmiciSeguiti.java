package com.example.alfredosansalone.geopost.intent;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.alfredosansalone.geopost.R;
import com.example.alfredosansalone.geopost.fragment.ListFragment;
import com.example.alfredosansalone.geopost.fragment.MapFragment;

public class AmiciSeguiti extends AppCompatActivity {

    //PULSANTE GESTIONE VISTA
    public void Vista(View v){
        ImageButton mappa = (ImageButton) findViewById(R.id.mappa);
        ImageButton lista = (ImageButton) findViewById(R.id.lista);
        if(lista.getVisibility() == View.VISIBLE){
            mappa.setVisibility(View.VISIBLE);
            lista.setVisibility(View.GONE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, new ListFragment());
            transaction.commit();
        } else {
            mappa.setVisibility(View.GONE);
            lista.setVisibility(View.VISIBLE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, new MapFragment());
            transaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amici_seguiti);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new MapFragment());
        transaction.commit();

    }


    // PULSANTE MENU A SCOMPARSA
    public void Menu(View v){
        ImageButton profilo = (ImageButton) findViewById(R.id.profilo);
        ImageButton aggamico = (ImageButton) findViewById(R.id.aggamici);
        if(profilo.getVisibility() == View.INVISIBLE) {
            Log.d("Button", "VISIBLE");
            profilo.setVisibility(View.VISIBLE);
            aggamico.setVisibility(View.VISIBLE);
        }else{
            Log.d("Button", "INVISIBLE");
            profilo.setVisibility(View.INVISIBLE);
            aggamico.setVisibility(View.INVISIBLE);
        }

    }

    // INTENT PER APRIRE NUOVE ACTIVITY
    public void Stato(View v) {
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, AggStato.class);
        startActivity(intent);
    }

    public void Profilo(View v) {
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, Profilo.class);
        startActivity(intent);
    }

    public void AggAmici(View v) {
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, AggAmici.class);
        startActivity(intent);
    }
}
