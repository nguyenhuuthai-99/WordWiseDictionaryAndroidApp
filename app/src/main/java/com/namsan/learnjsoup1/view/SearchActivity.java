package com.namsan.learnjsoup1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.namsan.learnjsoup1.R;
import com.namsan.learnjsoup1.entity.DictionarySuggestion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText wordInput;

    ImageView backButton;

    HomeActivity homeActivity;

    ArrayList<DictionarySuggestion> dictionarySuggestions;

    SuggestionAdapter suggestionAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        dictionarySuggestions = new ArrayList<>();

        homeActivity = new HomeActivity();
        backButton = findViewById(R.id.search_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        wordInput = findViewById(R.id.home_word_input);
        wordInput.requestFocus();
        wordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    if (wordInput.getText().length()==0){
                        Toast.makeText(SearchActivity.this, "Invalid Word", Toast.LENGTH_SHORT).show();
                    }else {
                        String wordInputString = "/dictionary/english/" + wordInput.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("inputSearch", wordInputString);
                        startActivity(intent);
                        handled = true;
                    }
                }
                return handled;
            }
        });
        wordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = wordInput.getText().toString();

                if (charSequence.length()>0){
                    userSuggestion(input);
                }else {
                    dictionarySuggestions.clear();
                    suggestionAdapter = new SuggestionAdapter(dictionarySuggestions, getApplicationContext(), new SuggestionAdapter.MyClickHandler() {
                        @Override
                        public void onItemClick(int position) {
                        }
                    });
                    try {
                        recyclerView.setAdapter(suggestionAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void userSuggestion(String input){

        String jsonUrl = "https://dictionary.cambridge.org/us/autocomplete/amp?dataset=english&q="+input;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(jsonUrl);
                    URLConnection request = url.openConnection();
                    request.connect();

                    InputStreamReader inputStream = new InputStreamReader((InputStream) request.getContent());

                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(inputStream);
                    JsonArray jsonArray = jsonElement.getAsJsonArray();

                    dictionarySuggestions = new ArrayList<>();
                    for (JsonElement a: jsonArray){
                        JsonObject jsonObject = a.getAsJsonObject();
                        String word = jsonObject.get("word").getAsString();
                        String urlString = jsonObject.get("url").getAsString();
                        dictionarySuggestions.add(new DictionarySuggestion(word, urlString));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        recyclerView = findViewById(R.id.home_recycler_view);

                        suggestionAdapter = new SuggestionAdapter(dictionarySuggestions, SearchActivity.this, new SuggestionAdapter.MyClickHandler() {
                            @Override
                            public void onItemClick(int position) {
                                String inputURL = dictionarySuggestions.get(position).getUrl();
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("inputSearch",inputURL);

                                startActivity(intent);
                            }
                        });

                        recyclerView.setAdapter(suggestionAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                    }
                });
            }
        }).start();



    }
}