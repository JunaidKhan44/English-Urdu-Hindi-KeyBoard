package com.example.create_keyboard1.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-020 on 8/5/2016.
 */
public class TrieCharacters implements Serializable{

    protected final Map<String, TrieCharacters> children;
    protected String value;

    protected boolean terminal = false;

    public TrieCharacters() {
        this(null);
    }

    private TrieCharacters(String value) {
        this.value = value;
        children = new HashMap<String, TrieCharacters>();
    }

    protected void add(String s) {
        String val;
        if (this.value == null) {
            val = s;
        } else {
            val = this.value + s;
        }
        children.put(s, new TrieCharacters(val));
    }

    public void insert(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Cannot add null to a Trie");
        }
        TrieCharacters node = this;
        String[] n = word.split(" ");
        word = n[0];
        String freq = n[1];
        char[] c = word.toCharArray();

        for (int i=0; i<c.length; i++) {

            String letter = "" + c[i];
            String nodeVal = "";
            if(i==c.length-1)
            {
                nodeVal = c[i] + " " +freq;
            }
            else
            {
                nodeVal = c[i]+"";
            }



            if (!node.children.containsKey(nodeVal)) {
                node.add(nodeVal);
            }
            node = node.children.get(nodeVal);
        }
        node.terminal = true;
    }

    public String find(String word) {
        TrieCharacters node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return "";
            }
            node = node.children.get(c);
        }
        return node.value;
    }

    public Collection<String> autoComplete(String prefix) {
        TrieCharacters node = this;

        for (char c : prefix.toCharArray()) {

            String s =""+c;
            if (!node.children.containsKey(s)) {
                return Collections.emptyList();
            }
            node = node.children.get(s);

        }
        return node.allPrefixes();
    }

    protected Collection<String> allPrefixes() {
        List<String> results = new ArrayList<String>();
        if (this.terminal) {
            results.add(this.value);
        }
        for (Map.Entry<String, TrieCharacters> entry : children.entrySet()) {
            TrieCharacters child = entry.getValue();
            Collection<String> childPrefixes = child.allPrefixes();
            results.addAll(childPrefixes);
        }
        return results;
    }
}
