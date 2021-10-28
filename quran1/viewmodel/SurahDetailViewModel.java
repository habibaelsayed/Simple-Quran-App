package com.example.quran1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.quran1.repository.SurahDetailRepo;
import com.example.quran1.response.SurahDetailResponse;

public class SurahDetailViewModel extends ViewModel {
    public SurahDetailRepo surahDetailRepo;

    public SurahDetailViewModel() {
        surahDetailRepo=new SurahDetailRepo();
    }

    public LiveData<SurahDetailResponse> getSurahDetail(String lan , int id){

        return surahDetailRepo.getSurahDetail(lan,id);
    }


}
