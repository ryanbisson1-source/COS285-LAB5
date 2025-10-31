package moviepack;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

/** Program to run Lab 5
 * @author Abby Pitcairn
 * @version October 27, 2025
 */
public class Lab5 {
    public static void main(String[] args) throws IOException {
        String tsv = args[0];
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
            "john mcclane",
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
