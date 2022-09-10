package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {

    EditText iranyitoszam;
    EditText varos;
    EditText utca;
    EditText hazszam;
    Button betoltesGomb;
    Button mentesGomb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button kuldesGomb = findViewById(R.id.kuldesGomb);
        iranyitoszam = findViewById(R.id.iranyitoszam);
        varos = findViewById(R.id.varos);
        utca = findViewById(R.id.utca);
        hazszam = findViewById(R.id.hazszam);
        betoltesGomb = findViewById(R.id.betoltGomb);
        mentesGomb = findViewById(R.id.mentesGomb);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (!prefs.contains("iranyitoszam")) {
            betoltesGomb.setClickable(false);
        } else {
            betoltesGomb.setClickable(true);
        }


       int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }


        kuldesGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout layout = findViewById(R.id.mainlayout);
                if (checkRequiredFieldsFilled(layout)) {
                    Intent intent = new Intent(MainActivity.this, CimKiir.class);
                    Cim cim = new Cim(iranyitoszam.getText().toString(), varos.getText().toString(), utca.getText().toString(), hazszam.getText().toString());
                    intent.putExtra("cim", cim);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_uzenet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mentesGomb.setOnClickListener(view -> {
            ConstraintLayout layout = findViewById(R.id.mainlayout);
            if (checkRequiredFieldsFilled(layout)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("iranyitoszam", iranyitoszam.getText().toString());
                editor.putString("varos", varos.getText().toString());
                editor.putString("utca", utca.getText().toString());
                editor.putString("hazszam", hazszam.getText().toString());
                editor.apply();

                SharedPreferences sharedPreferences = getSharedPreferences("aktualisvaros", MODE_PRIVATE);
                sharedPreferences.edit().putString("varos", varos.getText().toString()).apply();


                Toast.makeText(MainActivity.this, getString(R.string.sikeres_mentes_uzenet), Toast.LENGTH_LONG).show();

//region Belső tároló írás
                File file = new File(getFilesDir() + "/log.txt");

                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter writer = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(writer);
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    bw.append(String.format("%s, %s, %s, %s, %s\n", now.toString(), iranyitoszam.getText().toString(),
                            varos.getText().toString(), utca.getText().toString(), hazszam.getText().toString()));
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//endregion

            //region Külső tároló írás

                File publicfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "publiclog.txt");
                System.out.println(publicfile.getAbsolutePath());
                try {
                    if (!publicfile.exists()) {
                        publicfile.createNewFile();
                    }
                    FileWriter writer = new FileWriter(publicfile, true);
                    BufferedWriter bw = new BufferedWriter(writer);
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    bw.append(String.format("%s %s\n", now.toString(), getString(R.string.log_uzenet)));
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            //endregion

            }
        });

        betoltesGomb.setOnClickListener(view ->

        {
            iranyitoszam.setText(prefs.getString("iranyitoszam", ""));
            varos.setText(prefs.getString("varos", ""));
            utca.setText(prefs.getString("utca", ""));
            hazszam.setText(prefs.getString("hazszam", ""));

            Toast.makeText(MainActivity.this, getString(R.string.sikeres_betoltes_uzenet), Toast.LENGTH_LONG).show();

//region Belső tároló olvasás
            try {
                File file = new File(getFilesDir() + "/log.txt");
                FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader);
                int db = 0;
                while(br.readLine() != null){
                    db++;
                }
                br.close();
                System.out.println("Eddig "+db+" cím lett elmentve");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//endregion

            //region Külső tároló olvasás
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/publiclog.txt");
                FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader);
                int db = 0;
                while(br.readLine() != null){
                    db++;
                }
                br.close();
                System.out.println("Eddig "+db+" cím lett elmentve");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//endregion

        });

        Button adatbazisGomb = findViewById(R.id.adatbazisGomb);
        adatbazisGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Adatbazisos.class);
                startActivity(intent);
            }
        });

        Button szenzorGomb = findViewById(R.id.szenzorosGomb);
        szenzorGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Szenzoros.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkRequiredFieldsFilled(ViewGroup viewGroup) {
        boolean result = true;
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                checkRequiredFieldsFilled((ViewGroup) view);
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                if (editText.getText().toString().trim().isEmpty()) {
                    result = false;
                    editText.setError(getString(R.string.kotelezo));
                }
            }
        }
        return result;
    }
}