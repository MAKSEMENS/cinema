package test;

import javax.persistence.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @Column(name = "movie_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;
    @Column(name="movie_title")
    private String movieName;
    @Column(name="movie_genre")
    private String genre;
    @Column(name="movie_year")
    private Integer year;
    @Column(name="movie_director")
    private String director;
    @Column(name = "movie_inceptionDate")
    private static int[] inceptionDate = new int[3];
    @Column(name = "movie_finalDate")
    private static int[] finalDate = new int[3];
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    public Movie(){

    }


    public int getMovieId(){
        return movieId;
    }
    public void setMovieId (int newMovieId){
        movieId = newMovieId;
    }


    public String getMovieName(){
        return movieName;
    }
   public void setMovieName(String newTitle){
        movieName = newTitle;
    }


    public Integer getYear(){
        return year;
    }
    public void setYear(Integer newYear){
        year = newYear;
    }


    public String getGenre(){
        return genre;
    }
    public void setGenre(String newGenre){
        genre = newGenre;
    }

    public String getDirector(){
        return director;
    }
    public void setDirector(String newDirector){
        director = newDirector;
    }

    public static int[] getIncDate (){
        return inceptionDate;
    }

    public static int[] getFinDate (){
        return finalDate;
    }


    public  List<Session> getSessions() {
        return sessions;
    }


    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}