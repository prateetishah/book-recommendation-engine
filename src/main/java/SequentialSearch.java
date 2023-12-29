import model.DocumentData;
import search.TFIDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SequentialSearch {
    public static final String BOOKS_DIRECTORY = "./resources/books";
    public static final String SEARCH_QUERY_1 = "Detective; Catches many Criminals";
    public static final String SEARCH_QUERY_2 = "Girl Falls into rabbit hole";
    public static final String SEARCH_QUERY_3 = "Cold Winter";

    public static void main(String [] args) throws FileNotFoundException {
        File docsDir = new File(BOOKS_DIRECTORY);

        List<String> docs = Arrays.asList(docsDir.list()).stream().map(docName -> BOOKS_DIRECTORY + "/" + docName).collect(Collectors.toList());

        List<String> terms = TFIDF.getWordsFromLine(SEARCH_QUERY_3);

        findRelevantDocs(docs, terms);
    }

    public static void findRelevantDocs(List<String> docs, List<String> terms) throws FileNotFoundException {
        Map<String, DocumentData> documentMap = new HashMap<>();

        for(String doc : docs){
            BufferedReader reader = new BufferedReader(new FileReader(doc));
            List<String> lines = reader.lines().collect(Collectors.toList());
            List<String> words = TFIDF.getWordsFromLines(lines);
            DocumentData data = TFIDF.createData(words, terms);
            documentMap.put(doc, data);
        }

        Map<Double, List<String>> docsByScore = TFIDF.getSortedDocsByScore(terms, documentMap);
        printResult(docsByScore);
    }

    private static void printResult(Map<Double, List<String>> docsByScore){
        for(Map.Entry<Double, List<String>> pair : docsByScore.entrySet()){
            double score = pair.getKey();
            for(String doc : pair.getValue()){
                System.out.println(String.format("Book: %s - score : %f", doc.split("/")[3], score));
            }
        }
    }
}
