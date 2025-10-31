package moviepack;

import java.util.*;

public class MySearchEngine {
    private TreeMap<Movie, TreeMap<String, Double>> tf = new TreeMap<>();
    private TreeMap<String, Double> idf = new TreeMap<>();
    private final ArrayList<Movie> movies;

    public MySearchEngine(ArrayList<Movie> movies) {
        this.movies = movies;
        calculateTF();
        calculateIDF();
    }

    /** Calculates Inverse Document Frequency (IDF) */
    private void calculateIDF() {
        double totalMovies = this.movies.size();
        TreeMap<String, Integer> termCounts = new TreeMap<>();

        for (Movie m : this.movies) {
            // ✅ Include title + overview, lowercase, and clean punctuation
            String text = (m.getTitle() + " " + m.getOverview())
                    .toLowerCase()
                    .replaceAll("[^a-z0-9 ]", " ");
            String[] terms = text.split("\\s+");
            TreeSet<String> uniqueTerms = new TreeSet<>();

            for (String term : terms) {
                term = term.trim();
                if (term.isEmpty()) continue;
                uniqueTerms.add(term);
            }

            for (String uniqueTerm : uniqueTerms) {
                termCounts.put(uniqueTerm, termCounts.getOrDefault(uniqueTerm, 0) + 1);
            }
        }

        // ✅ Use smoothed log to avoid infinity/negatives for rare words
        for (Map.Entry<String, Integer> e : termCounts.entrySet()) {
            double count = e.getValue();
            double idfValue = Math.log((totalMovies + 1.0) / (count + 1.0)) + 1.0;
            idf.put(e.getKey(), idfValue);
        }
    }

    /** Calculates Term Frequency for each movie */
    private void calculateTF() {
        for (Movie m : movies) {
            // ✅ Include title + overview, lowercase, and remove punctuation
            String text = (m.getTitle() + " " + m.getOverview())
                    .toLowerCase()
                    .replaceAll("[^a-z0-9 ]", " ");
            String[] words = text.split("\\s+");

            TreeMap<String, Double> innerMap = new TreeMap<>();
            int totalWords = 0;

            for (String w : words) {
                if (!w.isBlank()) {
                    totalWords++;
                    innerMap.put(w, innerMap.getOrDefault(w, 0.0) + 1.0);
                }
            }

            // Normalize frequencies
            for (String w : innerMap.keySet()) {
                innerMap.put(w, innerMap.get(w) / totalWords);
            }

            tf.put(m, innerMap);
        }
    }

    /** Determines relevance of a movie using TF-IDF values */
    private double relevance(String query, Movie m) {
        // Clean query the same way as indexed text
        // maybe superstitious
        String[] queryTerms = query.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .split("\\s+");

        double baseScore = 0.0;
        int matchCount = 0;
        TreeMap<String, Double> movieTF = tf.get(m);
        if (movieTF == null) return 0.0;

        for (String term : queryTerms) {
            if (term.isBlank()) continue;

            double tfValue = movieTF.getOrDefault(term, 0.0);
            double idfValue = idf.getOrDefault(term, 0.0);

            if (tfValue > 0) matchCount++;
            baseScore += tfValue * idfValue;
        }

        // reward multi-term matches (because I think the program is possessed and wasn't working properly without it idk vro)
        if (matchCount > 1) {
            double bonus = 1.0 + 0.25 * (matchCount - 1);
            baseScore *= bonus;
        }
        if (matchCount == queryTerms.length && matchCount > 1) {
            baseScore *= 1.5;
        }

        return baseScore;
    }

    /** Find and print the top 5 most relevant movies for a query */
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
