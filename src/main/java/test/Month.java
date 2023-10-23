package test;


import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Month {
    private List<Movie> list_of_movies = new ArrayList<>();
    private int index;
    private ArrayList<Session> list_of_Session = new ArrayList<>();
    public List<Movie>GetList_of_Mov(){
        return list_of_movies;
    }
    public void SetList_of_Mov(List<Movie> newMovie){
    }
    public int GetIndex(){
        return index;
    }
    public void SetIndex(int newIndex){
        index = newIndex;
    }
    public List<Session> GetList_of_Ses(){
        return list_of_Session;
    }

}