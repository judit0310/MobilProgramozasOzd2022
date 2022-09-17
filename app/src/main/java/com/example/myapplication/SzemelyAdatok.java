package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityCimKiirBinding;
import com.example.myapplication.databinding.ActivitySzemelyAdatokBinding;
import com.example.myapplication.service.PersonDTO;

public class SzemelyAdatok extends AppCompatActivity implements View.OnClickListener {

    ActivitySzemelyAdatokBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_szemely_adatok);
        binding = ActivitySzemelyAdatokBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        PersonDTO szemely = (PersonDTO) intent.getSerializableExtra("szemely");
        if (szemely != null){
            binding.valaszottSzemelyId.setText(Integer.toString(szemely.getId()));
            binding.valasztottSzemelyKeresztnev.setText(szemely.getKeresztNev());
            binding.valasztottSzemelyVezeteknev.setText(szemely.getVezetekNev());
            binding.valasztottSzemelyFizetes.setText(Integer.toString(szemely.getFizetes()));
        }

        binding.startGomb.setOnClickListener(this);
        binding.stopGomb.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view == binding.startGomb){
            Intent intent = new Intent(this, ZeneLejatszas.class);
            startService(intent);
        }
        else if(view == binding.stopGomb){
            Intent intent = new Intent(this, ZeneLejatszas.class);
            stopService(intent);

        }
    }
}