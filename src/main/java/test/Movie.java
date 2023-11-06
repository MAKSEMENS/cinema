package test;

import javax.persistence.*;
import java.security.PublicKey;

@Entity
@Table(name = "movies")
public class Movie {
    private int movieId;
    private String movieName;
    private String genre;
    private Integer year;
    private String director;
    private static int[] inceptionDate = new int[3];
    private static int[] finalDate = new int[3];

    public Movie(){

    }

    @Id
    @Column(name = "movie_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getMovieId(){
        return movieId;
    }
    public void setMovieId (int newMovieId){
        movieId = newMovieId;
    }

    @Column(name="movie_title")
    public String getMovieName(){
        return movieName;
    }
   public void setMovieName(String newTitle){
        movieName = newTitle;
    }

    @Column(name="movie_year")
    public Integer getYear(){
        return year;
    }
    public void setYear(Integer newYear){
        year = newYear;
    }

    @Column(name="movie_genre")
    public String getGenre(){
        return genre;
    }
    public void setGenre(String newGenre){
        genre = newGenre;
    }
    @Column(name="movie_director")
    public String getDirector(){
        return director;
    }
    public void setDirector(String newDirector){
        director = newDirector;
    }
    @Column(name = "movie_inceptionDate")
    public static int[] getIncDate (){
        return inceptionDate;
    }
    @Column(name = "movie_finalDate")
    public static int[] getFinDate (){
        return finalDate;
    }

}