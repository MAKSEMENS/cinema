package test;

import javax.persistence.*;



public class AppClass {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        System.out.println("ааааааааааааааааа");

        Movie gr = new Movie();
        gr.setMovieName("Lord of the rings: return of the King");


        em.persist(gr);
        em.getTransaction().commit();

        System.out.println("New Movie Id is " + gr.getMovieId());

    }
}