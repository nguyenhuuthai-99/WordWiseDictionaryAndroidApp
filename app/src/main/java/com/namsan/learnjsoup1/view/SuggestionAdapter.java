package com.namsan.learnjsoup1.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.namsan.learnjsoup1.R;
import com.namsan.learnjsoup1.entity.DictionarySuggestion;

import java.util.ArrayList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionHolder> {

    ArrayList<DictionarySuggestion> dictionarySuggestions;

    Context context;

    MyClickHandler myClickHandler;

    public SuggestionAdapter(ArrayList<DictionarySuggestion> dictionarySuggestions, Context context, MyClickHandler myClickHandler) {
        this.dictionarySuggestions = dictionarySuggestions;
        this.myClickHandler = myClickHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public SuggestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dictionary_suggestion_holder, parent, false);

        return new SuggestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionHolder holder, int position) {
        holder.suggestion.setText(dictionarySuggestions.get(position).getWord());
    }

    @Override
    public int getItemCount() {
        return dictionarySuggestions.size();
    }

    class SuggestionHolder extends RecyclerView.ViewHolder {

        TextView suggestion;

        public SuggestionHolder(@NonNull View itemView) {
            super(itemView);
            suggestion = itemView.findViewById(R.id.suggestion_tv);
            suggestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myClickHandler.onItemClick(getAdapterPosition());
                }
            });

        }
    }
    interface MyClickHandler{
        void onItemClick(int position);
    }
}
