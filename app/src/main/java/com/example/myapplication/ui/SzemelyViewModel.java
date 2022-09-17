package com.example.myapplication.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.service.PersonDTO;
import com.example.myapplication.service.PersonService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SzemelyViewModel extends ViewModel {
    private MutableLiveData<List<PersonDTO>> szemelyek;

    public LiveData<List<PersonDTO>> getSzemelyek(){
        if (szemelyek == null){
            szemelyek = new MutableLiveData<>();
            loadSzemelyek();
        }
        return szemelyek;
    }

    private void loadSzemelyek() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://my-json-server.typicode.com/judit0310/dummyJsonServer/").
                addConverterFactory(GsonConverterFactory.create()).build();

        PersonService service = retrofit.create(PersonService.class);

        Call<List<PersonDTO>> listCall=service.szemelyekListazasa();
        listCall.enqueue(new Callback<List<PersonDTO>>() {
            @Override
            public void onResponse(Call<List<PersonDTO>> call, Response<List<PersonDTO>> response) {
                szemelyek.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<PersonDTO>> call, Throwable t) {

            }
        });
    }

}
