package com.example.quran1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quran1.R;
import com.example.quran1.listener.SurahListener;
import com.example.quran1.model.Surah;

import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

    private Context context;
    private List<Surah>list;
    private SurahListener surahListener;

    public SurahAdapter(Context context, List<Surah> list,SurahListener surahListener) {
        this.context = context;
        this.list = list;
        this.surahListener=surahListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.surah_layout,parent,false);

        return new ViewHolder(view,surahListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SurahAdapter.ViewHolder holder, int position) {
        holder.surahNumber.setText(String.valueOf(list.get(position).getNumber()));
        holder.englishName.setText(list.get(position).getEnglishName());
        holder.arabicName.setText(list.get(position).getName());
        holder.totalAya.setText("Aya : "+list.get(position).getNumberOfAyahs());


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView surahNumber,arabicName,englishName,totalAya;
        private SurahListener surahListener;
        public ViewHolder(@NonNull View itemView,SurahListener surahListener) {
            super(itemView);
            surahNumber=itemView.findViewById(R.id.surah_number);
            arabicName=itemView.findViewById(R.id.arabic_name);
            englishName=itemView.findViewById(R.id.english_name);
            totalAya=itemView.findViewById(R.id.total_aya);

            itemView.setOnClickListener(view -> surahListener.onSurahListener(getAdapterPosition()));


        }
    }
}
