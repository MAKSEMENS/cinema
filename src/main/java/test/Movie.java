package test;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate inceptionDate;
    @Column(name = "movie_finalDate")
    private LocalDate finalDate;
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

    public  LocalDate getIncDate (){
        return inceptionDate;
    }

    public  LocalDate getFinDate (){
        return finalDate;
    }

    public void setInceptionDate(LocalDate newInceptionDate){inceptionDate = newInceptionDate;}
    public void setFinalDate(LocalDate newFinalDate){finalDate = newFinalDate;}

    public SimpleStringProperty inceptionDateProperty (){
        return new SimpleStringProperty(inceptionDate.toString());
    }
    public SimpleStringProperty finalDateProperty (){
        return new SimpleStringProperty(finalDate.toString());
    }

    public  List<Session> getSessions() {
        return sessions;
    }


    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public String getFirstMonth (){return inceptionDate.getMonth().toString();}
    public String getLastMonth (){return finalDate.getMonth().toString();}

    public String getFirstYear (){return String.valueOf(inceptionDate.getYear());}
    public String getLastYear (){return String.valueOf(finalDate.getYear());}
}