package moviepack;
import moviepack.Movie;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class MySearchEngine {
    private TreeMap<Movie, TreeMap<Double, String>> tf;
    private TreeMap<String, Double> idf;
    private final ArrayList<Movie> movies;



    public MySearchEngine(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    private void calculateIDF() {
        int total_movies = this.movies.size();
        TreeMap<String, Integer> term_counts = new TreeMap<>();
        for (Movie m : this.movies) {
            String overview = m.getOverview();
            String title = m.getTitle();
            String allWords = overview + " " + title;
            String[] terms = allWords.split("\\s+");
            TreeSet<String> unique_terms = new TreeSet<>();
            for (String term : terms) {
                term = term.trim();
                term = term.toLowerCase();
                if (term.isEmpty()) continue;
                unique_terms.add(term);
            }
            for (String unique_term : unique_terms) {
                term_counts.put(unique_term, term_counts.getOrDefault(unique_term, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> e: term_counts.entrySet()){
            double term_count = e.getValue();
            double value = total_movies / term_count;
            idf.put(e.getKey(), value);
        }
    }

    private void calculateTF(){
        for (Movie m : movies) {
            String overview = m.getOverview();
            TreeMap<String, Double> innerMap = new TreeMap<String,Double>();

        }
    }

    private double relevance(String query, Movie m) {
        String[] queryTerms = query.toLowerCase().split(" ");
        double score = 0.0;

        TreeMap<String, Double> movieTF = tf.get(m);

        for (String term : queryTerms) {
            if (term.isBlank()) continue;

            // get TF and IDF values safely
            double tfValue = 0.0;
            if (movieTF.containsKey(term)) {
                tfValue = movieTF.get(term);
            }

            double idfValue = 0.0;
            if (idf.containsKey(term)) {
                idfValue = idf.get(term);
            }

            // add to total score (TF * IDF)
            score += tfValue * idfValue;
        }

        return score;
    }

    public void search(String query){

    }

}
