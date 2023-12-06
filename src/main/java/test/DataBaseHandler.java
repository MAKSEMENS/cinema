package test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class DataBaseHandler {
    private static final Logger logger = LogManager.getLogger("mainLogger");

    public static void getDataFromDB(String persistenceUnitName, ObservableList<Movie> groupsData) {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            logger.info("Trying to get data from DB");
            entityManager.getTransaction().begin();
            List<Movie> movies = entityManager.createQuery("from Movie", Movie.class).getResultList();
            groupsData.clear();
            groupsData.addAll(movies);

            entityManager.getTransaction().commit();
            logger.info("Fetching data from DB successful");

    }

    public static void saveMovieToDB (Movie movie){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();

        logger.info("Saving new band to DataBase");

        em.getTransaction().begin();


        em.persist(movie);
        em.getTransaction().commit();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("Movie successfully added, " + "id is " + movie.getMovieId());
        alert.showAndWait();
    }
    public static List<Movie> getSearchedMovies (String movieName){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Movie> searchedMovies = em.createQuery("select m from Movie m where lower(m.movieName) = lower(:name)", Movie.class).setParameter("name", movieName).getResultList();
        em.getTransaction().commit();
        return searchedMovies;
    }

    public static void saveSessionToDB(Movie movie,Session session) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
            logger.info("Saving new tour to DataBase");

            em.getTransaction().begin();
            movie.getSessions().add(session);
            session.setMovie(movie);
            em.persist(session);
            em.getTransaction().commit();

            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null,  session.getSessionId() + "session" + " successfully added");
            logger.info(session.getSessionDate() + "session" + " successfully saved to DB");


    }

    public static void editDataSession(int selectedSessionId, Map<String,String> paramValue, String persistenceUnitName) {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE Session SET countOfSold = ?1, date = ?2, time = ?3 WHERE id = ?4")
                    .setParameter(1, Integer.parseInt(paramValue.get("countOfSold")))
                    .setParameter(2, LocalDate.parse(paramValue.get("date")))
                    .setParameter(3, LocalTime.parse(paramValue.get("time")))
                    .setParameter(4,selectedSessionId)
                    .executeUpdate();
            entityManager.getTransaction().commit();

        AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null,  "session" + " successfully edited");

    }

    public static void editDataMovie(int selectedMovieId, Map<String,String> paramValue, String persistenceUnitName) {
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("UPDATE Movie SET movieName = ?1, year = ?2, genre = ?3, director = ?4 WHERE id = ?5")
                .setParameter(1, paramValue.get("movieName"))
                .setParameter(2, Integer.parseInt(paramValue.get("year")))
                .setParameter(3, paramValue.get("genre"))
                .setParameter(4, paramValue.get("director"))
                .setParameter(5,selectedMovieId)
                .executeUpdate();
        entityManager.getTransaction().commit();

        AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null,   "Movie" + " successfully edited");

    }

    public static void removeMovie( Movie movie){
        boolean k = AlertHandler.makeConfAlertWindow(Alert.AlertType.INFORMATION, "ПОДТВЕРЖДЕНИЕ!", null,   "Вы уверены, что хотите удалить данный фильм?");
        if (k==true) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test_persistence");

            // Открытие EntityManager
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            // Начало транзакции
            entityManager.getTransaction().begin();

            // Загрузка фильма
            int movieId = movie.getMovieId(); // Укажите актуальный идентификатор фильма
            Movie newMovie = entityManager.find(Movie.class, movieId);

            if (movie != null) {
                // Удаление фильма и всех его сеансов
                entityManager.remove(newMovie);
            }

            // Коммит транзакции
            entityManager.getTransaction().commit();

            // Закрытие EntityManager и фабрики EntityManager
            entityManager.close();
            entityManagerFactory.close();
            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null,   "Movie" + " successfully removed");
        }


    }

    public static void removeSession(Session session) {
        boolean k = AlertHandler.makeConfAlertWindow(Alert.AlertType.INFORMATION, "ПОДТВЕРЖДЕНИЕ!", null,   "Вы уверены, что хотите удалить данный сеанс?");
        if (k==true) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test_persistence");

            // Открытие EntityManager
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            // Начало транзакции
            entityManager.getTransaction().begin();

            // Загрузка фильма
            int movieId = session.getSessionId(); // Укажите актуальный идентификатор фильма
            Movie newMovie = entityManager.find(Movie.class, movieId);

            if (session != null) {
                // Удаление фильма и всех его сеансов
                entityManager.remove(newMovie);
            }

            // Коммит транзакции
            entityManager.getTransaction().commit();

            // Закрытие EntityManager и фабрики EntityManager
            entityManager.close();
            entityManagerFactory.close();
            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null,   "Session" + " successfully removed");
        }
    }
}
