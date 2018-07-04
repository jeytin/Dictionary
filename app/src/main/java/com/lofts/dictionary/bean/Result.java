package com.lofts.dictionary.bean;

import java.util.List;

public class Result {

    private String word;
    private Pronunciation pronunciation;
    private List<Define> defs;
    private List<Sample> sams;

    public Result() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Pronunciation getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(Pronunciation pronunciation) {
        this.pronunciation = pronunciation;
    }

    public List<Define> getDefs() {
        return defs;
    }

    public void setDefs(List<Define> defs) {
        this.defs = defs;
    }

    public List<Sample> getSams() {
        return sams;
    }

    public void setSams(List<Sample> sams) {
        this.sams = sams;
    }
}
