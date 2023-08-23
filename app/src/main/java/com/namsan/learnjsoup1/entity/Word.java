package com.namsan.learnjsoup1.entity;

import java.util.List;

public class Word {
    int id;
    private String url;
    private String wordName;
    private String wordForm;
    private String definition;
    private List<String> examples;

    public Word(String url, String wordName, String wordForm,String definition, List<String> examples) {
        this.url = url;
        this.wordName = wordName;
        this.wordForm = wordForm;
        this.definition = definition;
        this.examples = examples;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordForm() {
        return wordForm;
    }

    public void setWordForm(String wordForm) {
        this.wordForm = wordForm;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }
}
