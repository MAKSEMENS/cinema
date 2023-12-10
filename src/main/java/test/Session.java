package test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @Column(name = "session_id",unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sessionId;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    @Column(name = "session_date")
    private LocalDate date;
    @Column(name = "session_time")
    private  LocalTime time;
    @Column(name = "session_count")
    private Integer countOfSold;

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getMovieId(){return movie.getMovieId();};
    public int getSessionId(){
        return sessionId;
    }
    public void setSessionId (int newSes){
        sessionId = newSes;
    }


    public LocalDate getSessionDate() {
        return date;
    }
    public void setSessionDate(LocalDate newDate) {
        date= newDate;

    }



    public LocalTime getSessionTime() {
        return time;
    }
    public void setSessionTime(LocalTime newTime) {
        time = newTime;
    }

    public Integer getCountOfSold() {
        return countOfSold;
    }
    public void setCountOfSold(int newCount_Sold) {
        countOfSold = newCount_Sold;
    }

    public StringProperty dateOfSessionProperty() {
        return new SimpleStringProperty(date.toString());
    }
    public StringProperty timeOfSessionProperty() {
        return new SimpleStringProperty(time.toString());
    }
}