package com.namsan.learnjsoup1.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.namsan.learnjsoup1.entity.Word;
import com.namsan.learnjsoup1.entity.WordExtend;

import java.util.ArrayList;
import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryRowHolder> {

    ArrayList<Word> wordArrayList;
    ArrayList<WordExtend> wordExtendArrayList;

    Context context;

    ClickHandler clickHandler;


    public DictionaryAdapter(ArrayList<Word> wordArrayList, ArrayList<WordExtend> wordExtendArrayList, Context context, ClickHandler clickHandler) {
        this.wordExtendArrayList = wordExtendArrayList;
        this.wordArrayList = wordArrayList;
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public DictionaryRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dictionary_row_holder, parent, false);

        return new DictionaryRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryRowHolder holder, int position) {

        ArrayList<SpannableString> definitionSpannable = new ArrayList<>();
        String definitionString = wordArrayList.get(position).getDefinition();

        for (String a: definitionString.split(" ")){
            definitionSpannable.add(new SpannableString(a));
        }
        holder.definition.setText((position+1)+" ");

        for (SpannableString b: definitionSpannable){

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    StringBuilder wordBuilder = new StringBuilder();

                    char[] symbols = {'!','"','(',')','*','!','@','#','$','%','^','&','1','2','3','4','5','6','7','8','9','0','~','<','>','?','/','_','+','-','=',',','.',';','\'',':','[',']','{','}'};

                    String wordInput = b.toString();
                    wordBuilder.append(wordInput);
                    for (char a: symbols){
                        if (wordInput.indexOf(a)!=-1){
                            wordBuilder.deleteCharAt(wordInput.indexOf(a));
                        }
                    }

                    clickHandler.onWordClick(wordBuilder.toString());
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(ContextCompat.getColor(context, R.color.blue));
                    ds.setUnderlineText(false);
                }
            };



            b.setSpan(clickableSpan,0,b.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.definition.append(b);
            holder.definition.append(" ");
        }

        holder.definition.setMovementMethod(LinkMovementMethod.getInstance());

        for (int i = 0; i<wordArrayList.get(position).getExamples().size(); i++){

            TextView example = new TextView(context);
            example.setTypeface(null, Typeface.ITALIC);
            example.setTextColor(ContextCompat.getColor(context,R.color.blue));
            example.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            RecyclerView.LayoutParams layoutParams =  new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,10,0,0);
            example.setLayoutParams(layoutParams);

            String exampleString = wordArrayList.get(position).getExamples().get(i);

            ArrayList<SpannableString> spannableExampleStrings = new ArrayList<>();

            for (String c: exampleString.split(" ")){

                if (c.contains("/")){

                    int j=0;
                    for (String e: c.split("/")){
                        if (j==0){
                            spannableExampleStrings.add(new SpannableString(e));
                        }else
                            spannableExampleStrings.add(new SpannableString("/"+e));
                        if(j==c.split("/").length-1){
                            spannableExampleStrings.add(new SpannableString(" "));
                        }
                        j++;
                    }

                }else {
                    spannableExampleStrings.add(new SpannableString(c+" "));
                }


            }

            example.setText("-  ");

            for (SpannableString d: spannableExampleStrings){

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        StringBuilder wordBuilder = new StringBuilder();

                        char[] symbols = {'!','"','(',')','*','!','@','#','$','%','^','&','1','2','3','4','5','6','7','8','9','0','~','<','>','?','/','_','+','-','=',',','.',';','\'',':','[',']','{','}'};

                        String wordInput = d.toString();
                        wordBuilder.append(wordInput);
                        for (char a: symbols){
                            if (wordInput.indexOf(a)!=-1){
                                wordBuilder.deleteCharAt(wordInput.indexOf(a));
                            }
                        }

                        clickHandler.onWordClick(wordBuilder.toString());
                    }
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(ContextCompat.getColor(context, R.color.blue));
                        ds.setUnderlineText(false);
                    }
                };
                String alphabet ="abcdefghijklmnopqrstuvwxyz";
                for (String f: alphabet.split("")){
                    if (d.toString().contains(f)){
                        d.setSpan(clickableSpan,0,d.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                example.append(d);
            }
            example.setMovementMethod(LinkMovementMethod.getInstance());

            holder.exampleHolder.addView(example);
        }

    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }

    class DictionaryRowHolder extends RecyclerView.ViewHolder{

        ImageView saveButton;

        TextView definition;

        LinearLayout exampleHolder;

        public DictionaryRowHolder(@NonNull View itemView) {
            super(itemView);

            saveButton = itemView.findViewById(R.id.save_word);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = wordArrayList.get(getAdapterPosition()).getUrl();
                    String wordName = wordArrayList.get(getAdapterPosition()).getWordName();
                    String wordForm = wordArrayList.get(getAdapterPosition()).getWordForm();
                    String definition= wordArrayList.get(getAdapterPosition()).getDefinition();
                    List<String> examples = wordArrayList.get(getAdapterPosition()).getExamples();
                    WordExtend wordExtend = new WordExtend("",url,wordName,wordForm,definition,examples);
                    clickHandler.saveWord(wordExtend);
                }
            });

            definition = itemView.findViewById(R.id.definition);

            exampleHolder = itemView.findViewById(R.id.example_holder);

        }
    }


    interface ClickHandler{
        void onWordClick(String word);
        void saveWord(WordExtend wordExtend);
    }
}
