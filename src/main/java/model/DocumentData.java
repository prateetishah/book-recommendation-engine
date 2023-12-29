package model;

import java.util.HashMap;
import java.util.Map;

public class DocumentData {
    private Map<String, Double> termToFreq = new HashMap<>();

    public void putTermFreq(String term, double freq){
        termToFreq.put(term, freq);
    }

    public double getFreq(String term){
        return termToFreq.get(term);
    }
}
