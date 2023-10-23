package test;

import javax.persistence.*;

@Entity
@Table(name = "sessions")
public class Session {
    private int sessionId;
    private Movie movieId;
    private static int[] date = new int[3];
    private static int[] time = new int[3];
    private int countOfSold;

    @Id
    @Column(name = "session_id",unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getSesId(){
        return sessionId;
    }
    public void setSesId (int newSes){
        sessionId = newSes;
    }
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    public Movie getMovie() {
        return movieId;
    }
    public void setMovie(Movie newMovie) {
        movieId = newMovie;
    }
    @Column(name = "session_date")
    public static int[] getSessionDate() {
        return date;
    }
    public void setSessionDate(int[] newDate) {
        date[0] = newDate[0];
        date[1] = newDate[1];
        date[2] = newDate[2];
    }
    @Column(name = "session_time")
    public static int[] getSessionTime() {
        return time;
    }
    public void setSessionTime(int[] newTime) {
        time = newTime;
    }
    @Column(name = "session_count")
    public int getCount_Sold() {
        return countOfSold;
    }
    public void setCount_Sold(int newCount_Sold) {
        countOfSold = newCount_Sold;
    }

}