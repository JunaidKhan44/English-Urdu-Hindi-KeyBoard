package com.example.create_keyboard1.dictionary;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-020 on 8/5/2016.
 */
public class TrieWords {

    protected final Map<String, TrieWords> children;
    protected String value;

    protected boolean terminal = false;

    public TrieWords() {
        this(null);
    }

    private TrieWords(String value) {
        this.value = value;
        children = new HashMap<String, TrieWords>();
    }

    protected void add(String s) {
        String val;
        if (this.value == null) {
            val = s;
        } else {
            val = this.value + s;
        }
        children.put(s, new TrieWords(val));
    }

    public void insert(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Cannot add null to a Trie");
        }

        TrieWords node = this;
        String[] n = word.split(" ");
        String word2;
        word = n[0];

        String[] n2 = n[1].split("\t");

        word2 = "_"+n2[0];
        String freq = n2[1];

        //System.out.println(freq);

        List<String> list = new ArrayList<>();

        list.add(word);
        list.add(word2);
        list.add(freq);



        for (int i=0; i<2; i++) {

            String letter = "" + list.get(i);
            String nodeVal = "";
            if(i==1)
            {
                nodeVal = list.get(i) + " " +freq;
            }
            else
            {
                nodeVal = list.get(i)+"";
            }



            if (!node.children.containsKey(nodeVal)) {
                node.add(nodeVal);
            }
            node = node.children.get(nodeVal);
        }
        node.terminal = true;
    }

    public String find(String word) {
        TrieWords node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return "";
            }
            node = node.children.get(c);
        }
        return node.value;
    }

    public Collection<String> autoComplete(String prefix) {
        TrieWords node = this;
        Log.e("Word", prefix);
        if (!node.children.containsKey(prefix.trim())) {
            return Collections.emptyList();
        }
        node = node.children.get(prefix);

        return node.allPrefixes();
    }

    protected Collection<String> allPrefixes() {
        List<String> results = new ArrayList<String>();
        if (this.terminal) {
            results.add(this.value);
        }
        for (Map.Entry<String, TrieWords> entry : children.entrySet()) {
            TrieWords child = entry.getValue();
            Collection<String> childPrefixes = child.allPrefixes();
            results.addAll(childPrefixes);
        }
        return results;
    }
}