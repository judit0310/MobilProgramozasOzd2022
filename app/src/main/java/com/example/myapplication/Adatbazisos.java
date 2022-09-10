package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.db.CimDAO;
import com.example.myapplication.db.CimDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Adatbazisos extends AppCompatActivity {
    CimDAO dao;
    AutoCompleteTextView varosView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adatbazisos);
        varosView = findViewById(R.id.varosKereso);

        CimDatabase db = Room.databaseBuilder(getApplicationContext(), CimDatabase.class, "cimek").build();
        dao = db.getCimDAO();
    }

    @Override
    protected void onStart() {
        super.onStart();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Cim> cimek = dao.getAll();
                List<String> varosok = new ArrayList<>();
                for (int i = 0; i < cimek.size(); i++){
                    Cim c = cimek.get(i);
                    if (!varosok.contains(c.getVaros())){
                        varosok.add(c.getVaros());
                    }
                }

                System.out.println(varosok);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Adatbazisos.this, android.R.layout.simple_list_item_1, varosok);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(Adatbazisos.this, cimek.toString(), Toast.LENGTH_LONG).show();
                        varosView.setAdapter(adapter);
                    }
                });
            }
        });

        ImageButton hozzaadGomb = findViewById(R.id.hozzaadGomb);
        hozzaadGomb.setOnClickListener(view -> {
            Cim cim = new Cim();
            SharedPreferences prefs = getSharedPreferences("MainActivity", MODE_PRIVATE);
            cim.iranyitoszam = prefs.getString("iranyitoszam", "");
            cim.varos = prefs.getString("varos", "");
            cim.utca = prefs.getString("utca", "");
            cim.hazszam = prefs.getString("hazszam", "");
            if (dao.findSame(cim).size() == 0) {
                dao.insert(cim);
            }
        });


        ImageButton keresesGomb = findViewById(R.id.keresesGomb);
        keresesGomb.setOnClickListener(view ->
        {
            if (!varosView.getText().toString().trim().isEmpty()){
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Cim> varosiCimek =  dao.findInCity(varosView.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Adatbazisos.this, varosiCimek.toString(), Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                });
            }
        });





    }
}