package com.namsan.learnjsoup1.entity;

public class DictionarySuggestion {
    private String word;
    private String url;

    public DictionarySuggestion(String word, String url) {
        this.word = word;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
