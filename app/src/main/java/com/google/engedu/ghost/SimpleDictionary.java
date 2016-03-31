package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }


    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        int beginning = 0;
        int ending = words.size()-1;
        Log.d("Get any word", prefix);
        if((prefix.isEmpty()) || prefix == " " || prefix == ""){
            Random rand = new Random();
            int randNum = rand.nextInt(words.size());
            Log.d("Get any word", words.get(randNum));
            return words.get(randNum);
        }else{
            while(beginning <= ending){
                int mid = ((ending-beginning)/2);
                if(words.get(mid).compareTo(prefix) < 0){
                    ending = mid - 1;
                }else if(words.get(mid).compareTo(prefix) > 0){
                    beginning = mid + 1;
                }else if(words.get(mid).compareTo(prefix) == 0){
                    Log.d("Get any word", words.get(mid));
                    return words.get(mid);
                }
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }

}