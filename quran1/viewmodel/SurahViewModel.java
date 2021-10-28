package com.example.quran1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.quran1.repository.SurahRepo;
import com.example.quran1.response.SurahResponse;

public class SurahViewModel extends ViewModel {
private SurahRepo surahRepo;

    public SurahViewModel() {
        surahRepo=new SurahRepo();
    }

    public LiveData<SurahResponse> getSurah(){
        return surahRepo.getSurah();
    }
}
