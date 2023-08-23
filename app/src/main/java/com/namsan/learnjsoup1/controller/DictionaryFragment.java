package com.namsan.learnjsoup1.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.namsan.learnjsoup1.R;
import com.namsan.learnjsoup1.dao.SavedWordHandler;
import com.namsan.learnjsoup1.view.SearchActivity;
import com.namsan.learnjsoup1.view.WordFormAdapter;
import com.namsan.learnjsoup1.entity.Word;
import com.namsan.learnjsoup1.entity.WordExtend;
import com.namsan.learnjsoup1.entity.WordForm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DictionaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DictionaryFragment extends Fragment {

    View view;

    ArrayList<Word> wordArrayList;
    ArrayList<WordForm> wordFormArrayList;
    ArrayList<WordExtend> wordExtendArrayList;

    ConstraintLayout constraintLayout;

    ImageView backButton;

    EditText wordInput;

    RecyclerView wordFormHolder, wordFormBottomHolder;

    WordFormAdapter wordFormAdapter;

    String wordName, bottomWordName;
    String url = "/us/dictionary/english/name";

    ArrayList<String> usSound, ukSound;

    TextView word;

    MediaPlayer mediaPlayer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DictionaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DictionaryFragment newInstance(String param1, String param2) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        url = getArguments().getString("inputSearch");
        if (url==null){
            url="/us/dictionary/english/hello";
        }
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        launch(url);
    }
    private void launch(String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDictionaryData(input);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        constraintLayout = view.findViewById(R.id.dictionary_frag_constrain);

                        wordFormHolder = view.findViewById(R.id.dictionary_frag_word_form_holder);

                        word = view.findViewById(R.id.dictionary_frag_word);
                        word.setText(wordName);

                        wordInput = view.findViewById(R.id.dictionary_frag_word_input);
                        wordInput.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                Intent intent = new Intent(getActivity(), SearchActivity.class);
                                startActivity(intent);
                                return false;
                            }
                        });

                        backButton = view.findViewById(R.id.dictionary_frag_back_button);
                        backButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getActivity().onBackPressed();
                            }
                        });


                        wordFormAdapter = new WordFormAdapter(wordFormArrayList, getActivity(), new WordFormAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, String button) {
                                playAudio(position, button);
                            }

                            @Override
                            public void onYouglishHolderClick() {
                                onYounglishClick(wordName);
                            }

                            @Override
                            public void onWordClick(String word) {
                                bottomDictionarylaunch(word);

                            }

                            @Override
                            public void saveWord(WordExtend wordExtend) {
                                boolean isSuccessful = new SavedWordHandler(getActivity()).create(wordExtend);

                                if (isSuccessful)
                                    Toast.makeText(getActivity(), "Word added", Toast.LENGTH_SHORT).show();
                            }
                        });

                        wordFormHolder.setLayoutManager(new LinearLayoutManager(getActivity()));
                        wordFormHolder.setAdapter(wordFormAdapter);


                    }
                });



            }

            public void getDictionaryData(String input){
                deleteFolderRaw();
                Document document = null;
                String url = input;
                try {
                    document = Jsoup.connect("https://dictionary.cambridge.org"+input).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                wordFormArrayList = new ArrayList<>();

                assert document != null;
                wordName = Objects.requireNonNull(document.getElementsByClass("di-title").first()).text();

                Elements wordFormEntryElements = Objects.requireNonNull(document.getElementsByClass("di-body").first()).getElementsByClass("entry-body");


                Elements wordFormElements = null;
                if (wordFormEntryElements.size()>1){
                    wordFormElements = wordFormEntryElements;

                }else{
                    for (Element l: wordFormEntryElements){
                        wordFormElements = l.getElementsByClass("pr entry-body__el");
                        if (wordFormElements.size()==0)
                        {
                            wordFormElements = l.getElementsByClass("pv-block");
                        }

                    }
                }
                if (wordFormElements== null){
                    wordFormElements = document.getElementsByClass("di-body").first().getElementsByClass("idiom-body didiom-body");
                }



                usSound = new ArrayList<>();
                ukSound = new ArrayList<>();

                for (Element a: wordFormElements){

                    try {
                        Element usSoundElement = a.getElementsByClass("us dpron-i ").first().select("source[type=audio/ogg]").first();
                        if (usSoundElement==null){
                            Elements usSoundElements = a.getElementsByClass("us dpron-i ");
                            for (Element b:usSoundElements){
                                usSoundElement = b.select("source[type=audio/ogg]").first();
                                if (usSoundElement!=null){
                                    break;
                                }
                            }
                        }
                        usSound.add(usSoundElement.attr("src"));

                        Element ukSoundElement = a.getElementsByClass("uk dpron-i ").first().select("source[type=audio/ogg]").first();
                        if (ukSoundElement==null){
                            Elements ukSoundElements = a.getElementsByClass("uk dpron-i ");
                            for (Element b:ukSoundElements){
                                ukSoundElement = b.select("source[type=audio/ogg]").first();
                                if (ukSoundElement!=null){
                                    break;
                                }
                            }
                        }
                        ukSound.add(ukSoundElement.attr("src"));
                    }
                    catch (Exception exception){
                        exception.printStackTrace();
                    }

                    String wordForm = null;
                    try {
                        wordForm = a.getElementsByClass("pos dpos").first().text();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    Elements definitionElements = a.getElementsByClass("def-block ddef_block ");
                    Elements definitionExtendHolders= a.getElementsByClass("pr phrase-block dphrase-block lmb-25");
                    wordExtendArrayList = new ArrayList<>();
                    String definitionExtend= null;
                    List<String> examplesExtend = null;
                    for (Element d: definitionExtendHolders){
                        Element definitionExtendElement = d.getElementsByClass("def-block ddef_block ").first();
                        definitionElements.remove(definitionExtendElement);
                        String title = d.getElementsByClass("phrase-title dphrase-title").first().text();
                        definitionExtend = d.getElementsByClass("def ddef_d db").first().text();
                        examplesExtend = d.getElementsByClass("eg deg").eachText();

                        wordExtendArrayList.add(new WordExtend(title, url,wordName, wordForm, definitionExtend, examplesExtend));

                    }
                    Elements definitionExtendHolders2= a.getElementsByClass("pr phrase-block dphrase-block ");
                    for (Element c: definitionExtendHolders2){
                        Element definitionExtendElement2 = c.getElementsByClass("def-block ddef_block ").first();
                        definitionElements.remove(definitionExtendElement2);
                        String title = c.getElementsByClass("phrase-title dphrase-title").first().text();
                        definitionExtend = c.getElementsByClass("def ddef_d db").first().text();
                        examplesExtend = c.getElementsByClass("eg deg").eachText();

                        wordExtendArrayList.add(new WordExtend(title, url,wordName, wordForm, definitionExtend, examplesExtend));

                    }
                    wordArrayList = new ArrayList<>();
                    String definition = null;
                    List<String> examples = null;
                    for (Element b: definitionElements){

                        definition = b.getElementsByClass("def ddef_d db").first().text();
                        examples = b.getElementsByClass("eg deg").eachText();

                        wordArrayList.add(new Word(url,wordName,wordForm,definition, examples));
                    }

                    String usIpa = "";
                    String ukIpa = "";
                    try {
                        usIpa = a.getElementsByClass("us dpron-i ").first().getElementsByClass("pron dpron").first().text();
                        ukIpa = a.getElementsByClass("uk dpron-i ").first().getElementsByClass("pron dpron").first().text();
                    }catch (Exception exception){

                    }

                    wordFormArrayList.add(new WordForm(wordForm,usIpa, ukIpa,wordArrayList,wordExtendArrayList));

                }

                getAudio(usSound,ukSound);
            }


        }).start();
    }

    public void getBottomDictionaryData(String input){

        deleteFolderSecondRaw();
        Document document = null;
        String url = "/us/dictionary/english/"+input;
        try {
            document = Jsoup.connect("https://dictionary.cambridge.org"+url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordFormArrayList = new ArrayList<>();

        bottomWordName = document.getElementsByClass("hw dhw").first().text();


        Elements wordFormElements = document.getElementsByClass("di-body").first().getElementsByClass("pr entry-body__el");

        usSound = new ArrayList<>();
        ukSound = new ArrayList<>();

        for (Element a: wordFormElements){

            try {
                Element usSoundElement = a.getElementsByClass("us dpron-i ").first().select("source[type=audio/ogg]").first();
                usSound.add(usSoundElement.attr("src"));

                Element ukSoundElement = a.getElementsByClass("uk dpron-i ").first().select("source[type=audio/ogg]").first();
                ukSound.add(ukSoundElement.attr("src"));
            }
            catch (Exception exception){
                exception.printStackTrace();
            }

            String wordForm = null;
            try {
                wordForm = a.getElementsByClass("pos dpos").first().text();

            }catch (Exception e){
                e.printStackTrace();
            }

            Elements definitionElements = a.getElementsByClass("def-block ddef_block ");
            Elements definitionExtendHolders= a.getElementsByClass("pr phrase-block dphrase-block lmb-25");
            wordExtendArrayList = new ArrayList<>();
            String definitionExtend= null;
            List<String> examplesExtend = null;
            for (Element d: definitionExtendHolders){
                Element definitionExtendElement = d.getElementsByClass("def-block ddef_block ").first();
                definitionElements.remove(definitionExtendElement);
                String title = d.getElementsByClass("phrase-title dphrase-title").first().text();
                definitionExtend = d.getElementsByClass("def ddef_d db").first().text();
                examplesExtend = d.getElementsByClass("eg deg").eachText();

                wordExtendArrayList.add(new WordExtend(title, url,bottomWordName, wordForm, definitionExtend, examplesExtend));

            }
            Elements definitionExtendHolders2= a.getElementsByClass("pr phrase-block dphrase-block ");
            for (Element c: definitionExtendHolders2){
                Element definitionExtendElement2 = c.getElementsByClass("def-block ddef_block ").first();
                definitionElements.remove(definitionExtendElement2);
                String title = c.getElementsByClass("phrase-title dphrase-title").first().text();
                definitionExtend = c.getElementsByClass("def ddef_d db").first().text();
                examplesExtend = c.getElementsByClass("eg deg").eachText();

                wordExtendArrayList.add(new WordExtend(title, url,bottomWordName, wordForm, definitionExtend, examplesExtend));

            }
            wordArrayList = new ArrayList<>();
            for (Element b: definitionElements){

                String definition = b.getElementsByClass("def ddef_d db").first().text();
                List<String> examples = b.getElementsByClass("eg deg").eachText();

                wordArrayList.add(new Word(url,bottomWordName,wordForm,definition, examples));
            }

            String usIpa = "";
            String ukIpa = "";
            try {
                usIpa = a.getElementsByClass("us dpron-i ").first().getElementsByClass("pron dpron").first().text();
                ukIpa = a.getElementsByClass("uk dpron-i ").first().getElementsByClass("pron dpron").first().text();
            }catch (Exception exception){

            }

            wordFormArrayList.add(new WordForm(wordForm,usIpa, ukIpa,wordArrayList,wordExtendArrayList));

        }

        getSecondAudio(usSound,ukSound);
    }

    public void onYounglishClick(String wordName){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                getActivity(), R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getActivity())
                .inflate(R.layout.youglish_holder, (ConstraintLayout)getActivity().findViewById(R.id.bottom_dictionary_layout));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        TextView youglishTitle = bottomSheetView.findViewById(R.id.youglish_title);
        youglishTitle.setText(wordName);

        ImageView exitYouglishButton = bottomSheetView.findViewById(R.id.exit_youglish);
        exitYouglishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        WebView youglishBrowser = bottomSheetView.findViewById(R.id.youglish_browser);
        WebSettings webSettings = youglishBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String data = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <body>\n" +
                "  \n" +
                "    <!-- 1. The widget will replace this <div> tag. -->\n" +
                "    <div id=\"widget-1\"></div>\n" +
                "\n" +
                "\n" +
                "    <script>\n" +
                "      // 2. This code loads the widget API code asynchronously.\n" +
                "      var tag = document.createElement('script');\n" +
                "\n" +
                "      tag.src = \"https://youglish.com/public/emb/widget.js\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      // 3. This function creates a widget after the API code downloads.\n" +
                "      var widget;\n" +
                "      function onYouglishAPIReady(){\n" +
                "        widget = new YG.Widget(\"widget-1\", {\n" +
                "          width: 640,\n" +
                "          components:95, \n" +
                "          events: {\n" +
                "            'onFetchDone': onFetchDone,\n" +
                "            'onVideoChange': onVideoChange,\n" +
                "            'onCaptionConsumed': onCaptionConsumed\n" +
                "          }          \n" +
                "        });\n" +
                "        // 4. process the query\n" +
                "        widget.fetch(\""+wordName+"\",\"english\");\n" +
                "      }\n" +
                "\n" +
                "     \n" +
                "      var views = 0, curTrack = 0, totalTracks = 0;\n" +
                "    \n" +
                "      // 5. The API will call this method when the search is done\n" +
                "      function onFetchDone(event){\n" +
                "        if (event.totalResult === 0)   alert(\"No result found\");\n" +
                "        else totalTracks = event.totalResult; \n" +
                "      }\n" +
                "         \n" +
                "      // 6. The API will call this method when switching to a new video. \n" +
                "      function onVideoChange(event){\n" +
                "        curTrack = event.trackNumber;\n" +
                "        views = 0;\n" +
                "      }\n" +
                "         \n" +
                "      // 7. The API will call this method when a caption is consumed. \n" +
                "      function onCaptionConsumed(event){\n" +
                "        if (++views < 2)\n" +
                "          widget.replay();\n" +
                "       \n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";

        youglishBrowser.loadData(data,"text/html","UTF-8");
    }

    public void deleteFolderRaw(){

        File file = new File(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.namsan.learnjsoup1/files/raw");
        if (file.isDirectory()){
            for (File child: file.listFiles()){
                child.delete();
            }
        }

    }

    public void deleteFolderSecondRaw(){
        File file = new File(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.namsan.learnjsoup1/files/raw2");
        if (file.isDirectory()){
            for (File child: file.listFiles()){
                child.delete();
            }
        }
    }


    public void getAudio(List<String> usSound, List<String> ukSound){

        int i = 0;
        for (String a: usSound){
            String DownLoadUrl = "https://dictionary.cambridge.org/"+ a;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownLoadUrl));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }

            request.setDestinationInExternalFilesDir(getActivity(), "/raw", "usSound"+i+".mp3");

            DownloadManager manager1 = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Objects.requireNonNull(manager1).enqueue(request);

            i++;
        }

        int j = 0;
        for (String a: ukSound){
            String DownLoadUrl = "https://dictionary.cambridge.org/"+ a;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownLoadUrl));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }

            request.setDestinationInExternalFilesDir(getActivity(), "/raw", "ukSound"+j+".mp3");

            DownloadManager manager1 = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Objects.requireNonNull(manager1).enqueue(request);

            j++;
        }

    }

    public void getSecondAudio(List<String> usSound, List<String> ukSound){

        int i = 0;
        for (String a: usSound){
            String DownLoadUrl = "https://dictionary.cambridge.org/"+ a;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownLoadUrl));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }

            request.setDestinationInExternalFilesDir(getActivity(), "/raw2", "usSound"+i+".mp3");

            DownloadManager manager1 = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Objects.requireNonNull(manager1).enqueue(request);

            i++;
        }

        int j = 0;
        for (String a: ukSound){
            String DownLoadUrl = "https://dictionary.cambridge.org/"+ a;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownLoadUrl));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }

            request.setDestinationInExternalFilesDir(getActivity(), "/raw2", "ukSound"+j+".mp3");

            DownloadManager manager1 = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Objects.requireNonNull(manager1).enqueue(request);

            j++;
        }

    }

    public void playAudio(int position, String button){

        File file = new File("");
        if (button == "usButton"){
            file = new File(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.namsan.learnjsoup1/files/raw", "usSound"+position+".mp3");
        }else{
            file = new File(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.namsan.learnjsoup1/files/raw", "ukSound"+position+".mp3");

        }

        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void playSecondAudio(int position, String button){
        File file = new File("");
        if (button == "usButton"){
            file = new File(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.namsan.learnjsoup1/files/raw2", "usSound"+position+".mp3");
        }else{
            file = new File(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.namsan.learnjsoup1/files/raw2", "ukSound"+position+".mp3");

        }

        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void bottomDictionarylaunch(String word){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                getActivity(), R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getActivity())
                .inflate(R.layout.bottom_dictionary, (ConstraintLayout)getActivity().findViewById(R.id.bottom_dictionary_layout));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        TextView bottomViewWord = bottomSheetView.findViewById(R.id.word_bottom_view);
        bottomViewWord.setText(word);
        new Thread(new Runnable() {
            @Override
            public void run() {

                getBottomDictionaryData(word);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        wordFormBottomHolder = bottomSheetView.findViewById(R.id.word_bottom_view_form_holder);

                        wordFormAdapter = new WordFormAdapter(wordFormArrayList, getActivity(), new WordFormAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, String button) {
                                playSecondAudio(position, button);
                            }

                            @Override
                            public void onYouglishHolderClick() {
                                onYounglishClick(bottomWordName);
                            }

                            @Override
                            public void onWordClick(String word) {
                                bottomDictionarylaunch(word);
                            }

                            @Override
                            public void saveWord(WordExtend wordExtend) {

                                System.out.println(wordExtend.getWordName()+wordExtend.getWordForm()+"\n"+wordExtend.getDefinition()+"\n"+wordExtend.getExamples());
                                boolean isSuccessful = new SavedWordHandler(getActivity()).create(wordExtend);

                                if (isSuccessful)
                                    Toast.makeText(getActivity(), "Word added", Toast.LENGTH_SHORT).show();
                            }
                        });

                        wordFormBottomHolder.setAdapter(wordFormAdapter);
                        wordFormBottomHolder.setLayoutManager(new LinearLayoutManager(getActivity()));

                        bottomSheetView.findViewById(R.id.exit_bottom_view_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.cancel();
                            }
                        });
                    }
                });
            }

        }).start();


    }
}