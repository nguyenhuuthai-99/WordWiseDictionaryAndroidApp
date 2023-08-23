package com.namsan.learnjsoup1.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.namsan.learnjsoup1.R;
import com.namsan.learnjsoup1.entity.Word;
import com.namsan.learnjsoup1.entity.WordExtend;
import com.namsan.learnjsoup1.entity.WordForm;

import java.util.ArrayList;

public class WordFormAdapter extends RecyclerView.Adapter<WordFormAdapter.WordFormRowHolder> {

    ArrayList<WordForm> wordFormArrayList;
    ArrayList<Word> wordArrayList;
    ArrayList<WordExtend> wordExtendArrayList;

    Context context;
    DictionaryAdapter dictionaryAdapter;
    DictionaryExtendAdapter dictionaryExtendAdapter;

    ClickListener clickListener;

    public WordFormAdapter(ArrayList<WordForm> wordFormArrayList, Context context, ClickListener clickListener) {
        this.wordFormArrayList = wordFormArrayList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public WordFormRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.word_form_holder, parent,false);

        return new WordFormRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordFormRowHolder holder, int position) {
        holder.wordForm.setText(wordFormArrayList.get(position).getWordForm());

        holder.usIpa.setText(wordFormArrayList.get(position).getUsIpa());
        holder.ukIpa.setText(wordFormArrayList.get(position).getUkIpa());

        wordArrayList = wordFormArrayList.get(position).getWordList();
        wordExtendArrayList = wordFormArrayList.get(position).getWordExtendArrayList();
        dictionaryAdapter = new DictionaryAdapter(wordArrayList, wordExtendArrayList, context, new DictionaryAdapter.ClickHandler() {
            @Override
            public void onWordClick(String word) {
                clickListener.onWordClick(word);
            }

            @Override
            public void saveWord(WordExtend wordExtend) {
                clickListener.saveWord(wordExtend);
            }
        });
        dictionaryExtendAdapter = new DictionaryExtendAdapter(wordExtendArrayList, new DictionaryExtendAdapter.ClickHandler() {
            @Override
            public void onWordClick(String word) {
                clickListener.onWordClick(word);
            }

            @Override
            public void saveWord(WordExtend wordExtend) {
                clickListener.saveWord(wordExtend);
            }
        }, context);

        holder.definitionHolder.setAdapter(dictionaryAdapter);
        holder.definitionHolder.setLayoutManager(new LinearLayoutManager(context));

        holder.definitionExtendHolder.setAdapter(dictionaryExtendAdapter);
        holder.definitionExtendHolder.setLayoutManager(new LinearLayoutManager(context));
    }


    @Override
    public int getItemCount() {
        return wordFormArrayList.size();
    }

    class WordFormRowHolder extends RecyclerView.ViewHolder {

        TextView wordForm, usIpa, ukIpa;

        ImageView usSound, ukSound;

        LinearLayout youglishSoundHolder;

        RecyclerView definitionHolder;
        RecyclerView definitionExtendHolder;

        public WordFormRowHolder(@NonNull View itemView) {
            super(itemView);

            wordForm = itemView.findViewById(R.id.word_form);
            usIpa = itemView.findViewById(R.id.ipa_us);
            ukIpa = itemView.findViewById(R.id.ipa_uk);

            usSound = itemView.findViewById(R.id.us_sound);
            usSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String button = "usButton";
                    clickListener.onItemClick(getAdapterPosition(), button);

                }
            });
            ukSound = itemView.findViewById(R.id.uk_sound);
            ukSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String button = "ukButton";
                    clickListener.onItemClick(getAdapterPosition(),button);
                }
            });

            definitionHolder = itemView.findViewById(R.id.definition_holder);
            definitionExtendHolder = itemView.findViewById(R.id.definition_extend_holder);

            youglishSoundHolder = itemView.findViewById(R.id.youglish_sound_holder);
            youglishSoundHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onYouglishHolderClick();
                }
            });

        }
    }

    public interface ClickListener{
        void onItemClick(int position, String button);
        void onYouglishHolderClick();
        void onWordClick(String word);
        void saveWord(WordExtend wordExtend);
    }
}
