package com.namsan.learnjsoup1.entity;

import java.util.List;

public class WordExtend extends Word{
    String title;

    public WordExtend(String title, String url, String wordName, String wordForm, String definition, List<String> examples) {
        super(url, wordName, wordForm, definition, examples);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
