package moviepack;

import java.io.*;
import java.util.*;

/** Create a Movie object that stores information about a movie.
 * @author Abby Pitcairn
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

    /** Constructor for a Movie:
     * @param title - movie title
     * @param votes - movie ranking out of ten
     * @param releaseDate - movie release in format month-day-year
     * @param runTime - movie run time
     * @param budget - approximate movie budget
     * @param genre - most prevalent genre
     * @param overview - short summary of movie plot
     */
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

    /** Get the title for a Movie @return title - the title of the Movie */
    public String getTitle() { 
        return title; 
    }
    
    /** Get the overview for a Movie @return overview - the overview of the Movie */
    public String getOverview() { 
        return overview; 
    }

    /** Compare two Movie objects based on title
     * @param other - the Movie to compare the current Movie to
     * @return the value 0 if this == other; 
     *          a value less than 0 if this < other; 
     *          and a value greater than 0 if this > other
     */
    @Override
    public int compareTo(Movie other) {
        return this.title.compareTo(other.title);
    }

    /** Print the title and overview of a Movie object */
    @Override
    public String toString(){
        return title + ":" + overview;
    }
    
    /** Reads a tab-separated file with columns.
     * @param tsvPath - the path to the tsv file to be read
     * @return movies - an ArrayList of Movies from the file
    */
   public static ArrayList<Movie> readMovies(String tsvPath) throws IOException {
       // Initialize ArrayList of Movies
       ArrayList<Movie> movies = new ArrayList<>();     
       // Initialize the buffered reader to read the file
       BufferedReader br = new BufferedReader(new FileReader(tsvPath));
      
       // Process the file line by line:
       String line = br.readLine();
       while ((line = br.readLine()) != null) {
           String[] parts = line.split("\t");
           // If we have enough information 
           if (parts.length == 7) {
               String title = parts[0].trim();
               double votes = Double.parseDouble(parts[1].trim());
               String releaseDate = parts[2].trim();
               int runTime = Integer.parseInt(parts[3].trim());
               int budget = Integer.parseInt(parts[4].trim());
               String genre = parts[5].trim();
               String overview = parts[6].trim();
               // Create a new Movie and add it to the ArrayList
               movies.add(new Movie(title, votes, releaseDate, runTime, budget, genre, overview));
           }
           // If we don't have enough information to make a Movie, skip this line
           else { continue; }
       }
       
       br.close();
       return movies;
   }
}
