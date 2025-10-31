package moviepack;

import java.util.*;

public class MySearchEngine {
    private TreeMap<Movie, TreeMap<String, Double>> tf = new TreeMap<>();
    private TreeMap<String, Double> idf = new TreeMap<>();
    private final ArrayList<Movie> movies;

    public MySearchEngine(ArrayList<Movie> movies) {
        this.movies = movies;

        // Run calculations when object is created
        calculateTF();
        calculateIDF();
    }

    /** Calculates Inverse Document Frequency (IDF) */
    private void calculateIDF() {
        double totalMovies = this.movies.size();
        TreeMap<String, Integer> termCounts = new TreeMap<>();

        for (Movie m : this.movies) {
            String overview = m.getOverview().toLowerCase();
            String[] terms = overview.split("\\s+");
            TreeSet<String> uniqueTerms = new TreeSet<>();

            for (String term : terms) {
                term = term.trim();
                if (term.isEmpty()) continue;
                uniqueTerms.add(term);
            }

            // Count in how many movies each word appears
            for (String uniqueTerm : uniqueTerms) {
                termCounts.put(uniqueTerm, termCounts.getOrDefault(uniqueTerm, 0) + 1);
            }
        }

        // Compute IDF values using log(totalMovies / count)
        for (Map.Entry<String, Integer> e : termCounts.entrySet()) {
            double count = e.getValue();
            double idfValue = Math.log(totalMovies / count);
            idf.put(e.getKey(), idfValue);
        }
    }

    /** Calculates Term Frequency for each movie */
    private void calculateTF() {
        for (Movie m : movies) {
            String overview = m.getOverview().toLowerCase();
            String[] words = overview.split("\\s+");

            TreeMap<String, Double> innerMap = new TreeMap<>();
            int totalWords = 0;

            for (String w : words) {
                if (!w.isBlank()) {
                    totalWords++;
                    innerMap.put(w, innerMap.getOrDefault(w, 0.0) + 1.0);
                }
            }

            // normalize frequencies
            for (String w : innerMap.keySet()) {
                innerMap.put(w, innerMap.get(w) / totalWords);
            }

            tf.put(m, innerMap);
        }
    }

    /** Calculates the relevance score for one movie and one query */
    private double relevance(String query, Movie m) {
        String[] queryTerms = query.toLowerCase().split(" ");
        double score = 0.0;

        TreeMap<String, Double> movieTF = tf.get(m);
        if (movieTF == null) return 0.0;

        for (String term : queryTerms) {
            if (term.isBlank()) continue;

            double tfValue = movieTF.getOrDefault(term, 0.0);
            double idfValue = idf.getOrDefault(term, 0.0);
            score += tfValue * idfValue;
        }

        return score;
    }

    /** Finds and prints the top 5 most relevant movies for a query */
    public void search(String query) {
        HashMap<Movie, Double> scores = new HashMap<>();

        for (Movie m : movies) {
            double s = relevance(query, m);
            scores.put(m, s);
        }

        // Sort by score descending
        List<Map.Entry<Movie, Double>> sorted = new ArrayList<>(scores.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.println("\nTop 5 results for query: \"" + query + "\"");

        int rank = 1;
        for (Map.Entry<Movie, Double> entry : sorted) {
            if (rank > 5) break;
            Movie m = entry.getKey();
            double score = entry.getValue();
            System.out.printf("%d. %s (%.6f)\n   %s\n\n",
                    rank, m.getTitle(), score, m.getOverview());
            rank++;
        }
    }
}
