package moviepack;

import java.io.*;
import java.util.*;

/** Create a Movie object that stores information about a movie.
 * @author Ryan Bisson, Jake Shaw, Austin Levesque
 * @version October 27, 2025
 */
public class Movie implements Comparable<Movie> {

    private final String title;
    private final double votes;
    private final String releaseDate;
    private final int runtime;
    private final int budget;
    private final String genre;
    private final String overview;

    public Movie(String title, double votes, String releaseDate,
                 int runTime, int budget, String genre, String overview) {
        this.title = title;
        this.votes = votes;
        this.releaseDate = releaseDate;
        this.runtime = runTime;
        this.budget = budget;
        this.genre = genre;
        this.overview = overview;
    }

    public String getTitle() { return title; }
    public String getOverview() { return overview; }

    @Override
    public int compareTo(Movie other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString(){
        return title + ": " + overview;
    }

    public static ArrayList<Movie> readMovies(String tsvPath) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(tsvPath));

        String line = br.readLine(); // skip header
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length == 7) {
                String title = parts[0].trim();
                double votes = Double.parseDouble(parts[1].trim());
                String releaseDate = parts[2].trim();
                int runTime = Integer.parseInt(parts[3].trim());
                int budget = Integer.parseInt(parts[4].trim());
                String genre = parts[5].trim();
                String overview = parts[6].trim();
                movies.add(new Movie(title, votes, releaseDate, runTime, budget, genre, overview));
            }
        }
        br.close();
        return movies;
    }
}
