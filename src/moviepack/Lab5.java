package moviepack;

import java.io.IOException;
import java.util.ArrayList;

/** Program to run Lab 5
 * @author Abby Pitcairn
 * @version October 27, 2025
 */
public class Lab5 {
    public static void main(String[] args) throws IOException {
        //String tsv = args[0];
        String tsv = "C:\\Users\\Crew Win\\Documents\\GitHub\\COS285-LAB5\\src\\moviepack\\imdb_movies.tsv";
        ArrayList<Movie> movies = Movie.readMovies(tsv);

        // Time creation of the search engine
        long t0 = System.currentTimeMillis();
        MySearchEngine engine = new MySearchEngine(movies);
        long t1 = System.currentTimeMillis();
        System.out.println((t1 - t0) + " milliseconds to create search engine");

        String[] queries = {
            "harry potter",
            "small town in maine",
            "world war",
            "john mcclanes",
            "bruce wayne"
        };

        // Run each search
        for (String q : queries) {
            t0 = System.currentTimeMillis();
            engine.search(q);
            t1 = System.currentTimeMillis();
            System.out.println("Time for search: " + (t1 - t0) + " ms\n");
        }
    }
}
