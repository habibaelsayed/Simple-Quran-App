package com.example.quran1.activities;

import android.annotation.SuppressLint;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quran1.Adapter.SurahDetailAdapter;
import com.example.quran1.R;
import com.example.quran1.common.Common;
import com.example.quran1.model.SurahDetail;
import com.example.quran1.response.SurahDetailResponse;
import com.example.quran1.viewmodel.SurahDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SurahDetailActivity extends AppCompatActivity {

    private TextView surahName,surahType,surahTranslation;
    private int no;
    private RecyclerView recyclerView;
    private List<SurahDetail>list;
    private SurahDetailAdapter adapter;
    private SurahDetailViewModel surahDetailViewModel;
    private String english="english_saheeh";
    private String french="french_montada";
    private String german="german_bubenheim";
    //private SurahDetailResponse surahDetailResponse;
    //private EditText searchView;
    private ImageButton settingButton;
    private RadioGroup radioGroup,audioGroup;
    private RadioButton translationButton,qariSelectButton;
    private String lan;
    private String qariAB="abdul_basit_murattal";
    private String qariMin="minshawi_mujawwad";
    private String qariRashid="mishaari_raashid_al_3afaasee";
    private String qariHusare="mahmood_khaleel_al-husaree";
    private String qr;
    Handler handler=new Handler();
    SeekBar seekBar;
    TextView startTime,totalTime;
    ImageButton playButton;
    MediaPlayer mediaPlayer;
    private String str;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        surahName=findViewById(R.id.surah_name);
        surahType=findViewById(R.id.type);
        surahTranslation=findViewById(R.id.translation);
        recyclerView=findViewById(R.id.surah_detail_rv);
        settingButton=findViewById(R.id.settings_button);

        no=getIntent().getIntExtra(Common.SURAH_NO,0);
        surahName.setText(getIntent().getStringExtra(Common.SURAH_NAME));
        surahType.setText(getIntent().getStringExtra(Common.SURAH_TYPE)+" "+
                getIntent().getIntExtra(Common.SURAH_TOTAL_AYA,0)+" AYA ");
        surahTranslation.setText(getIntent().getStringExtra(Common.SURAH_TRANSLATION));

        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        surahTranslation(english,no);

        try {
            listenAudio(qariAB);
        } catch (IOException e) {
            e.printStackTrace();
        }

        settingButton.setOnClickListener(v -> {

            BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SurahDetailActivity.this,
                    R.style.BottomSheetDialogTheme);

            LayoutInflater inflater= LayoutInflater.from(getApplicationContext());

            View view=inflater.inflate(R.layout.bottom_sheet_layout,
                    findViewById(R.id.sheetContainer));

            view.findViewById(R.id.save_settings_button).setOnClickListener(v1 -> {
                radioGroup=view.findViewById(R.id.translation_group);
                audioGroup=view.findViewById(R.id.audio_group);

                int selectedId=radioGroup.getCheckedRadioButtonId();
                translationButton=view.findViewById(selectedId);
                if(selectedId==-1)
                {
                    Toast.makeText(SurahDetailActivity.this,"nothing selected",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SurahDetailActivity.this,"selected",Toast.LENGTH_SHORT).show();
                }

                if(translationButton.getText().toString().toLowerCase().trim().equals("english"))
                {
                    lan=english;
                }else if(translationButton.getText().toString().toLowerCase().trim().equals("french"))
                {
                    lan=french;
                }else{
                    lan=german;
                }
                surahTranslation(lan,no);

                int id=audioGroup.getCheckedRadioButtonId();
                qariSelectButton=view.findViewById(id);
                if(qariSelectButton.getText().toString().trim().toLowerCase().equals("abdul basit murattal"))
                {
                    qr=qariAB;
                }else if(qariSelectButton.getText().toString().trim().toLowerCase().equals("minshawi mujawwad")){
                    qr=qariMin;
                }else if(qariSelectButton.getText().toString().trim().toLowerCase().equals("mishaari raashid al aafaasee")){
                    qr=qariRashid;
                }else if(qariSelectButton.getText().toString().trim().toLowerCase().equals("mahmood khaleel al-husaree")){
                    qr=qariHusare;
                }
                mediaPlayer.reset();
                mediaPlayer.release();
                try {
                    listenAudio(qr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();

        });
    }

    private void surahTranslation(String lan, int id) {
        if(list.size()>0){
            list.clear();
        }

        surahDetailViewModel = new ViewModelProvider(this).get(SurahDetailViewModel.class);
        surahDetailViewModel.getSurahDetail(lan,id).observe(this, surahDetailResponse->{
            for(int i=0;i<surahDetailResponse.getList().size();i++){
                list.add(new SurahDetail(surahDetailResponse.getList().get(i).getId(),
                        surahDetailResponse.getList().get(i).getSura(),
                        surahDetailResponse.getList().get(i).getAya(),
                        surahDetailResponse.getList().get(i).getArabic_text(),
                        surahDetailResponse.getList().get(i).getTranslation(),
                        surahDetailResponse.getList().get(i).getFootnotes()));

            }

            if(list.size()!=0){
                adapter=new SurahDetailAdapter(this,list);
                recyclerView.setAdapter(adapter);


            }

        });}


    private void filter(String id) {
        ArrayList<SurahDetail> arrayList=new ArrayList<>();
        for(SurahDetail detail : list){
            if(String.valueOf(detail.getId()).contains(id)){
                arrayList.add(detail);
            }
        }
        adapter.filter(arrayList);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listenAudio(String qari) throws IOException {
        playButton=findViewById(R.id.play_button);
        startTime=findViewById(R.id.start_time);
        totalTime=findViewById(R.id.total_time);
        seekBar=findViewById(R.id.seekBar);

        mediaPlayer=new MediaPlayer();
        seekBar.setMax(100);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                }else{
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    updateSeekBar();

                }
            }
        });
        
        prepareMediaPlayer(qari);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SeekBar seekBar=(SeekBar) view;
                int playPosition=(mediaPlayer.getDuration()/100)*seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                startTime.setText(timeToMilliSecond(mediaPlayer.getCurrentPosition()));

                return false;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int i) {
                seekBar.setSecondaryProgress(i);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                startTime.setText("0:00");
                totalTime.setText("0:00");
                mediaPlayer.reset();
                try {
                    prepareMediaPlayer(qari);
                } catch (IOException e) {
                    e.printStackTrace();
                } {
                };
            }
        });

    }

    private void prepareMediaPlayer(String qari) throws IOException {
        if(no<10){
            str="00"+no;

        }else if(no<100){
            str="0"+no;
        }else if(no>=100){
            str=String.valueOf(no);
        }
        mediaPlayer.setDataSource("https://download.quranicaudio.com/quran/"+qari+"/"+str.trim()+".mp3");
        mediaPlayer.prepare();
        totalTime.setText(timeToMilliSecond(mediaPlayer.getDuration()));

    }

    private Runnable updater=new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration=mediaPlayer.getCurrentPosition();
            startTime.setText(timeToMilliSecond(currentDuration));

        }
    };

    private void updateSeekBar(){
        if(mediaPlayer.isPlaying())
        {
            seekBar.setProgress((int)(((float) mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }

    }

    private String timeToMilliSecond(long millisecond){
        String timerString="";
        String secondString;

        int hours=(int)(millisecond/(1000*60*60));
        int minutes=(int)(millisecond%(1000*60*60))/(1000*60);
        int second=(int)((millisecond%(1000*60*60))%(1000*60)/1000);

        if(hours>0){
            timerString=hours+":";
        }
        if(second<10)
        {
            secondString="0"+second;
        }else{
            secondString=""+second;
        }

        timerString=timerString+minutes+":"+secondString;
        return timerString;

    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer.isPlaying())
        {
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if(mediaPlayer.isPlaying())
        {
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(mediaPlayer.isPlaying())
        {
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        super.onPause();
    }
}