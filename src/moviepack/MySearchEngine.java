package moviepack;

import java.util.*;
import moviepack.Movie;

public class MySearchEngine {

    private TreeMap<Movie, TreeMap<String, Double>> tf;
    private TreeMap<String, Double> idf;
    private final ArrayList<Movie> movies;

    public MySearchEngine(ArrayList<Movie> movies) {
        this.movies = movies;
        this.tf = new TreeMap<>();
        this.idf = new TreeMap<>();

        // call both calculations when the engine is created
        calculateTF();
        calculateIDF();
    }

    /**
     * Calculate the term frequency (TF) for each word in each movie overview.
     * tf(w, m) = frequency of w in m / total number of words in m
     */
    private void calculateTF() {
        for (Movie m : movies) {
            String overview = m.getOverview().toLowerCase();
            String title = m.getTitle().toLowerCase();
            String allText = overview + " " + title;
            String[] words = allText.split("\\s+");
            TreeMap<String, Double> innerMap = new TreeMap<>();

            // count how many times each word appears
            for (String w : words) {
                w = w.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");
                if (w.isEmpty()) continue;
                innerMap.put(w, innerMap.getOrDefault(w, 0.0) + 1.0);
            }

            // divide counts by total number of words to get frequency
            int totalWords = Math.max(1, words.length);
            for (String key : innerMap.keySet()) {
                innerMap.put(key, innerMap.get(key) / totalWords);
            }

            // store this inner map for the movie
            tf.put(m, innerMap);
        }
    }

    /**
     * Calculate the inverse document frequency (IDF) for each unique term.
     * idf(w) = log(|M| / |{m ∈ M : w ∈ m}|)
     */
    private void calculateIDF() {
        double totalMovies = this.movies.size();
        TreeMap<String, Integer> term_counts = new TreeMap<>();

        for (Movie m : this.movies) {
            String overview = m.getOverview().toLowerCase();
            String title = m.getTitle().toLowerCase();
            String allWords = overview + " " + title;

            String[] tokens = allWords.split("\\s+");
            TreeSet<String> unique_terms = new TreeSet<>();

            for (String term : tokens) {
                term = term.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");
                if (term.isEmpty()) continue;
                unique_terms.add(term);
            }

            for (String unique_term : unique_terms) {
                term_counts.put(unique_term, term_counts.getOrDefault(unique_term, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> e : term_counts.entrySet()) {
            double term_count = e.getValue();
            double value = Math.log(totalMovies / term_count);
            idf.put(e.getKey(), value);
        }
    }

    /**
     * Compute the relevance between a query and a movie using TF × IDF
     */
    private double relevance(String query, Movie m) {
        String[] queryTerms = query.toLowerCase().split(" ");
        double score = 0.0;
        TreeMap<String, Double> movieTF = tf.get(m);

        for (String term : queryTerms) {
            term = term.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");
            if (term.isEmpty()) continue;

            double tfValue = 0.0;
            if (movieTF.containsKey(term)) tfValue = movieTF.get(term);

            double idfValue = 0.0;
            if (idf.containsKey(term)) idfValue = idf.get(term);

            score += tfValue * idfValue;

        }
        return score;
    }

    /**
     * Perform a search and print the top-5 most relevant movies
     */
    public void search(String query) {
        TreeMap<Movie, Double> scores = new TreeMap<>();

        for (Movie m : movies) {
            double score = relevance(query, m);
            scores.put(m, score);


        }


        List<Map.Entry<Movie, Double>> results = sortedByValue(scores, 5);
        printSearchResults(query, results);
    }

    /** Sort map entries by value (descending) and return top-k results */
    private List<Map.Entry<Movie, Double>> sortedByValue(TreeMap<Movie, Double> treeMap, int topK) {
        List<Map.Entry<Movie, Double>> list = new ArrayList<>(treeMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Movie, Double>>() {
            @Override
            public int compare(Map.Entry<Movie, Double> o1, Map.Entry<Movie, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int counter = 0;
        List<Map.Entry<Movie, Double>> results = new ArrayList<>();
        while (counter < topK && counter < list.size()) {
            results.add(Map.entry(list.get(counter).getKey(), list.get(counter).getValue()));
            counter++;
        }
        return results;
    }

    /** Print search results */
    private void printSearchResults(String query, List<Map.Entry<Movie, Double>> results) {
        System.out.println("Results for " + query);
        int rank = 1;
        for (Map.Entry<Movie, Double> entry : results) {
            System.out.println(rank + ": " + entry.getKey().getTitle() + "\t" + entry.getKey().getOverview());
            rank++;
        }
    }
}
