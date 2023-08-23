package com.namsan.learnjsoup1.entity;

import java.util.ArrayList;

public class WordForm {
    private String wordForm;
    private String usIpa;
    private String ukIpa;
    private ArrayList<Word> wordList;
    private ArrayList<WordExtend> wordExtendArrayList;

    public WordForm(String wordForm, String usIpa, String ukIpa, ArrayList<Word> wordList, ArrayList<WordExtend> wordExtendArrayList) {
        this.wordExtendArrayList = wordExtendArrayList;
        this.wordForm = wordForm;
        this.usIpa = usIpa;
        this.ukIpa = ukIpa;
        this.wordList = wordList;
    }

    public ArrayList<WordExtend> getWordExtendArrayList() {
        return wordExtendArrayList;
    }

    public void setWordExtendArrayList(ArrayList<WordExtend> wordExtendArrayList) {
        this.wordExtendArrayList = wordExtendArrayList;
    }

    public String getWordForm() {
        return wordForm;
    }

    public void setWordForm(String wordForm) {
        this.wordForm = wordForm;
    }

    public ArrayList<Word> getWordList() {
        return wordList;
    }

    public void setWordList(ArrayList<Word> wordList) {
        this.wordList = wordList;
    }

    public String getUsIpa() {
        return usIpa;
    }

    public void setUsIpa(String usIpa) {
        this.usIpa = usIpa;
    }

    public String getUkIpa() {
        return ukIpa;
    }

    public void setUkIpa(String ukIpa) {
        this.ukIpa = ukIpa;
    }
}
