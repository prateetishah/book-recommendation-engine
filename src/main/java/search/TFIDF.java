package search;

import apple.laf.JRSUIUtils;
import model.DocumentData;

import java.util.*;

public class TFIDF {
    public static double calculateTF(List<String> words, String term){
        long count = 0;
        for(String word : words){
           if(term.equalsIgnoreCase(word)){
               count++;
           }
        }

        double termFreq = (double) count / words.size();
        return termFreq;
    }

    public static DocumentData createData(List<String> words, List<String> terms){
        DocumentData documentData = new DocumentData();

        for(String term: terms){
            double tf = calculateTF(words, term);
            documentData.putTermFreq(term, tf);
        }

        return documentData;
    }

    private static double getIDF(String term, Map<String, DocumentData> documents){
        double nt = 0;
        for(String document : documents.keySet()){
            DocumentData documentData = documents.get(document);
            double tf = documentData.getFreq(term);
            if(tf > 0.0){
                nt++;
            }
        }

        return nt == 0 ? 0 : Math.log10(documents.size() / nt);
    }

    private static Map<String, Double> getTermToIDFMap(List<String> terms, Map<String, DocumentData> documents){
        Map<String, Double> termToIDF = new HashMap<>();
        for(String term : terms){
            double idf = getIDF(term, documents);
            termToIDF.put(term, idf);
        }

        return termToIDF;
    }

    private static double calculateDocScore(List<String> terms, DocumentData documentData, Map<String, Double> termToIDF){
        double score = 0;
        for(String term : terms){
            double tf = documentData.getFreq(term);
            double inverseTF = termToIDF.get(term);
            score += tf * inverseTF;
        }
        return score;
    }

    public static Map<Double, List<String>> getSortedDocsByScore(List<String> terms, Map<String, DocumentData> documents){
        TreeMap<Double, List<String>> scoreToDocs = new TreeMap<>();
        Map<String, Double> termToIDF = getTermToIDFMap(terms, documents);

        for(String document : documents.keySet()){
            DocumentData documentData = documents.get(document);
            double score = calculateDocScore(terms, documentData, termToIDF);
            addDocScoreToTreeMap(scoreToDocs, score, document);
        }

        return scoreToDocs.descendingMap();
    }

    private static void addDocScoreToTreeMap(TreeMap<Double, List<String>> scoreToDoc, double score, String document){
        List<String> docsWithGivenScore = scoreToDoc.get(score);

        if(docsWithGivenScore == null){
            docsWithGivenScore = new ArrayList<>();
        }
        docsWithGivenScore.add(document);
        scoreToDoc.put(score, docsWithGivenScore);
    }

    public static List<String> getWordsFromLine(String line){
        return Arrays.asList(line.split("(\\.)+|(,)+|( )+|(-)+|(\\?)+|(!)+|(;)+|(:)+|(/d)+|(/n)"));
    }

    public static List<String> getWordsFromLines(List<String> lines){
        List<String> words = new ArrayList<>();
        for(String line : lines){
            words.addAll(getWordsFromLine(line));
        }

        return words;
    }
}
