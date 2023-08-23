package com.namsan.learnjsoup1.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.namsan.learnjsoup1.R;
import com.namsan.learnjsoup1.entity.WordExtend;

import java.util.ArrayList;

public class SavedWordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<WordExtend> wordExtendArrayList;

    Context context;

    ClickHandler clickHandler;

    public SavedWordAdapter(ArrayList<WordExtend> wordExtendArrayList, Context context, ClickHandler clickHandler) {
        this.wordExtendArrayList = wordExtendArrayList;
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType==1){
            view = LayoutInflater.from(context).inflate(R.layout.saved_word_holder, parent,false);
            return new SavedWordHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.saved_word_extend_holder, parent, false);
            return  new SaveWordExtendHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position)==1){
            ((SavedWordHolder)holder).setWord(wordExtendArrayList.get(position));
        }else {
            ((SaveWordExtendHolder)holder).setExtendWord(wordExtendArrayList.get(position));
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (wordExtendArrayList.get(position).getTitle().equals("")){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return wordExtendArrayList.size();
    }

    class SavedWordHolder extends RecyclerView.ViewHolder {

        TextView wordName, definition;

        LinearLayout exampleButton, exampleHolder, definitionHolder;

        ImageView deleteButton;

        public SavedWordHolder(@NonNull View itemView) {
            super(itemView);

            wordName = itemView.findViewById(R.id.saved_holder_word_name);
            definition = itemView.findViewById(R.id.saved_holder_definition);

            exampleButton = itemView.findViewById(R.id.dropdown_button);
            exampleHolder = itemView.findViewById(R.id.saved_holder_example);

            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.deleteButtonClick(getAdapterPosition());
                }
            });

            definitionHolder = itemView.findViewById(R.id.saved_holder_definition_holder);
            definitionHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onWordCLick(getAdapterPosition());
                }
            });

        }
        private void setWord(WordExtend wordExtend){
            wordName.setText(wordExtend.getWordName());
            definition.setText("("+wordExtend.getWordForm()+") "+
                    wordExtend.getDefinition());

            if (wordExtend.getExamples().size()!=0){
                exampleButton.setVisibility(View.VISIBLE);
            }
            exampleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < wordExtendArrayList.get(getAdapterPosition()).getExamples().size(); i++) {

                        TextView example = new TextView(context);
                        example.setTypeface(null, Typeface.ITALIC);
                        example.setTextColor(ContextCompat.getColor(context, R.color.blue));
                        example.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0,10,0,0);
                        example.setLayoutParams(layoutParams);

                        String exampleString = wordExtendArrayList.get(getAdapterPosition()).getExamples().get(i);

                        example.setText("-  ");
                        example.append(exampleString);
                        exampleHolder.addView(example);
                        exampleButton.setVisibility(View.GONE);

                    }
                }
            });
        }
    }

    class SaveWordExtendHolder extends RecyclerView.ViewHolder{
        TextView wordName,title, definition;

        LinearLayout exampleButton, exampleHolder, definitionHolder;

        ImageView deleteButton;
        public SaveWordExtendHolder(@NonNull View itemView) {
            super(itemView);

            wordName = itemView.findViewById(R.id.saved_word_extend_word_name);
            title = itemView.findViewById(R.id.saved_word_extend_title);
            definition = itemView.findViewById(R.id.saved_word_extend_definition);

            exampleButton = itemView.findViewById(R.id.saved_word_extend_dropdown_button);
            exampleHolder = itemView.findViewById(R.id.saved_word_extend_example);

            deleteButton = itemView.findViewById(R.id.saved_word_extend_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.deleteButtonClick(getAdapterPosition());
                }
            });

            definitionHolder = itemView.findViewById(R.id.saved_word_extend_definition_holder);
            definitionHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onWordCLick(getAdapterPosition());
                }
            });
        }
        private void setExtendWord(WordExtend wordExtend){
            wordName.setText(wordExtend.getWordName());
            title.setText(wordExtend.getTitle());
            definition.setText("("+wordExtend.getWordForm()+") "+
                    wordExtend.getDefinition());

            if (wordExtend.getExamples().size()!=0){
                exampleButton.setVisibility(View.VISIBLE);
            }
            exampleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < wordExtendArrayList.get(getAdapterPosition()).getExamples().size(); i++) {

                        TextView example = new TextView(context);
                        example.setTypeface(null, Typeface.ITALIC);
                        example.setTextColor(ContextCompat.getColor(context, R.color.white));
                        example.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0,10,0,0);
                        example.setLayoutParams(layoutParams);

                        String exampleString = wordExtendArrayList.get(getAdapterPosition()).getExamples().get(i);

                        example.setText("-  ");
                        example.append(exampleString);
                        exampleHolder.addView(example);
                        exampleButton.setVisibility(View.GONE);

                    }
                }
            });
        }
    }

    public interface ClickHandler{
        void deleteButtonClick(int position);
        void onWordCLick(int position);
    }
}
