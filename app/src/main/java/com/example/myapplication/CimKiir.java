package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityCimKiirBinding;


public class CimKiir extends AppCompatActivity {

    private ActivityCimKiirBinding binding;
    private int CAMERA_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCimKiirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Cim cim = (Cim) intent.getSerializableExtra("cim");
        binding.iranyitoszamMezo.setText(cim.iranyitoszam);
        binding.varosMezo.setText(cim.varos);
        binding.utcaMezo.setText(cim.utca);
        binding.hazszamMezo.setText(cim.hazszam);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("aktualisvaros", MODE_PRIVATE);
        Toast.makeText(this, prefs.getString("varos",getString(R.string.ismeretlen_varos)),Toast.LENGTH_SHORT ).show();

        ActivityResultLauncher<Intent> kamerakep = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bitmap img = (Bitmap) result.getData().getExtras().get("data");
                binding.table.setBackground(new BitmapDrawable(getResources(), img));
            }
        });

        binding.kameraGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                kamerakep.launch(intent);
               // startActivityForResult(intent, CAMERA_IMAGE_REQUEST);

            }
        });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
               Bitmap img = (Bitmap) data.getExtras().get("data");
               binding.table.setBackground(new BitmapDrawable(getResources(), img));
            }
        }
    }
*/
}