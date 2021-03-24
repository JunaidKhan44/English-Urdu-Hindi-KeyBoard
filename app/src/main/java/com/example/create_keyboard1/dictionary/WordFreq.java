package com.example.create_keyboard1.dictionary;

/**
 * Created by PC-020 on 8/5/2016.
 */
public class WordFreq implements Comparable<WordFreq>{
    String letter;
    int frequency;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public WordFreq(String letter, int frequency) {
        this.letter = letter;
        this.frequency = frequency;
    }

    public WordFreq() {
    }

    public int compareTo(WordFreq compare) {

        int compareFrequency = ((WordFreq) compare).getFrequency();

        //ascending order
        //return this.frequency - compareFrequency;

        //descending order
        return compareFrequency - this.frequency;

    }

}

