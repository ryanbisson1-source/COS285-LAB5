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

    private double relevance(String query, Movie m){ return 0.0;};

    public void search(String query){

    }

}
