package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.db.CimDAO;
import com.example.myapplication.db.CimDatabase;
import com.example.myapplication.service.PersonDTO;
import com.example.myapplication.service.PersonService;
import com.example.myapplication.ui.SzemelyAdapter;
import com.example.myapplication.ui.SzemelyKattintas;
import com.example.myapplication.ui.SzemelyViewModel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Adatbazisos extends AppCompatActivity {
    CimDAO dao;
    AutoCompleteTextView varosView;

    private SzemelyViewModel mViewModel;
    private SzemelyAdapter adapter;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adatbazisos);
        varosView = findViewById(R.id.varosKereso);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CimDatabase db = Room.databaseBuilder(getApplicationContext(), CimDatabase.class, "cimek").build();
        dao = db.getCimDAO();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://my-json-server.typicode.com/judit0310/dummyJsonServer/").
                addConverterFactory(GsonConverterFactory.create()).build();

        PersonService service = retrofit.create(PersonService.class);

      /*  Call<List<PersonDTO>> listCall=service.szemelyekListazasa();
        listCall.enqueue(new Callback<List<PersonDTO>>() {
            @Override
            public void onResponse(Call<List<PersonDTO>> call, Response<List<PersonDTO>> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<List<PersonDTO>> call, Throwable t) {

            }
        });

        Call<PersonDTO> szemelyCall= service.getSzemelyById(99);
        szemelyCall.enqueue(new Callback<PersonDTO>() {
            @Override
            public void onResponse(Call<PersonDTO> call, Response<PersonDTO> response) {
                if (response.body() != null){
                    Toast.makeText(Adatbazisos.this, response.body().toString(),
                            Toast.LENGTH_SHORT).show();
                    //System.out.println(response.body());
                }
                else{
                    Toast.makeText(Adatbazisos.this, getString(R.string.szemely_nem_talalhato),
                            Toast.LENGTH_SHORT).show();
                    //System.out.println(getString(R.string.szemely_nem_talalhato));
                }
            }

            @Override
            public void onFailure(Call<PersonDTO> call, Throwable t) {

            }
        });

        PersonDTO person = new PersonDTO(99,"János","Kiss",123456 );

        Call<PersonDTO> addSzemely = service.createSzemely(person);
        addSzemely.enqueue(new Callback<PersonDTO>() {
            @Override
            public void onResponse(Call<PersonDTO> call, Response<PersonDTO> response) {
                System.out.println("Siker "+response.body());
            }

            @Override
            public void onFailure(Call<PersonDTO> call, Throwable t) {
                System.out.println("Valami félrement");
            }
        });*/

        RecyclerView recyclerView = findViewById(R.id.szemelyeklista);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        mViewModel = new ViewModelProvider(this).get(SzemelyViewModel.class);
        adapter = new SzemelyAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        mViewModel.getSzemelyek().observe(this, szemelyek -> {
           adapter.setSzemelyek(szemelyek);
           adapter.setListener(new SzemelyKattintas() {
               @Override
               public void onSzemelyClick(int position, View v) {
                   PersonDTO szemely = szemelyek.get(position);
                   System.out.println("A következőre kattintottam: "+szemelyek.get(position));
                    Intent intent = new Intent(Adatbazisos.this, SzemelyAdatok.class);
                    intent.putExtra("szemely", szemely);
                    startActivity(intent);

               }
           });
           recyclerView.setAdapter(adapter);

        });



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