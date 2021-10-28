package com.example.quran1.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quran1.network.Api;
import com.example.quran1.network.JsonPlaceHolderApi;
import com.example.quran1.response.SurahResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurahRepo {
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    public SurahRepo() {
        jsonPlaceHolderApi= Api.getRetrofit().create(JsonPlaceHolderApi.class);
    }

    public LiveData<SurahResponse> getSurah(){
        MutableLiveData<SurahResponse> data=new MutableLiveData<>();
        jsonPlaceHolderApi.getSurah().enqueue(new Callback<SurahResponse>() {
            @Override
            public void onResponse(@NonNull Call<SurahResponse> call, @NonNull Response<SurahResponse> response) {
                data.setValue(response.body());

            }

            @Override
            public void onFailure(@NonNull Call<SurahResponse> call, @NonNull Throwable t) {
                data.setValue(null);

            }
        });
        return data;
    }

}
