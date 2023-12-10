package test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.util.HashMap;
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
    public static List<Movie> getSearchedByNameMovies (String movieName){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Movie> searchedMovies = em.createQuery("select m from Movie m where lower(m.movieName) = lower(:name)", Movie.class).setParameter("name", movieName).getResultList();
        em.getTransaction().commit();
        return searchedMovies;
    }
    public static List<Movie> getSearchedByMonthMovies(String month) {
        Map<String, Integer> monthMap = new HashMap<>();
        // Заполнение HashMap значениями
        monthMap.put("Январь", 1);
        monthMap.put("Февраль", 2);
        monthMap.put("Март", 3);
        monthMap.put("Апрель", 4);
        monthMap.put("Май", 5);
        monthMap.put("Июнь", 6);
        monthMap.put("Июль", 7);
        monthMap.put("Август", 8);
        monthMap.put("Сентябрь", 9);
        monthMap.put("Октябрь", 10);
        monthMap.put("Ноябрь", 11);
        monthMap.put("Декабрь", 12);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Movie> searchedMovies = em.createQuery("SELECT m FROM Movie m " +
                        "WHERE MONTH(m.inceptionDate) <= :date AND MONTH(m.finalDate) >= :date ", Movie.class).setParameter("date",monthMap.get(month)).getResultList();
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
        entityManager.createQuery("UPDATE Movie SET movieName = ?1, year = ?2, genre = ?3, director = ?4, inceptionDate =?5, finalDate = ?6   WHERE id = ?7")
                .setParameter(1, paramValue.get("movieName"))
                .setParameter(2, Integer.parseInt(paramValue.get("year")))
                .setParameter(3, paramValue.get("genre"))
                .setParameter(4, paramValue.get("director"))
                .setParameter(5, LocalDate.parse(paramValue.get("inceptionDate"), DateTimeFormatter.ISO_DATE))
                .setParameter(6, LocalDate.parse(paramValue.get("finalDate"), DateTimeFormatter.ISO_DATE))
                .setParameter(7,selectedMovieId)
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
        boolean k = AlertHandler.makeConfAlertWindow(Alert.AlertType.INFORMATION, "ПОДТВЕРЖДЕНИЕ!", null, "Вы уверены, что хотите удалить данный сеанс?");
        if (k) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test_persistence");

            // Открытие EntityManager
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            // Начало транзакции
            entityManager.getTransaction().begin();

            int sessionId = session.getSessionId();
            Session newSession = entityManager.find(Session.class, sessionId);
            Movie newMovie = entityManager.find(Movie.class, session.getMovieId());
            if (newSession != null) {
                System.out.println(newSession.getSessionId());
                newMovie.getSessions().remove(newSession);
            }

            // Коммит транзакции
            entityManager.getTransaction().commit();

            // Закрытие EntityManager и фабрики EntityManager
            entityManager.close();
            entityManagerFactory.close();
            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Session" + " successfully removed");
        }
    }

        public static void removeSession(Movie movie, TableView <Session> sessionTableView, ObservableList<Session> sessionData, String persistenceUnitName) {
            boolean k = AlertHandler.makeConfAlertWindow(Alert.AlertType.INFORMATION, "ПОДТВЕРЖДЕНИЕ!", null, "Вы уверены, что хотите удалить данный сеанс?");
            if (k) {
                Session selectedSession= sessionTableView.getSelectionModel().getSelectedItem();
                if (selectedSession != null) {
                    int selectedSessionId = selectedSession.getSessionId();
                    EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
                        logger.info("Trying to delete member");
                        entityManager.getTransaction().begin();
                        entityManager.createQuery("DELETE FROM Session WHERE id = ?1 ").setParameter(1, selectedSessionId).executeUpdate();
                        entityManager.getTransaction().commit();
                        sessionData.remove(selectedSession);
                        movie.getSessions().remove(selectedSession);
                        sessionTableView.refresh();

                    AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Session" + " successfully removed");

                }
                else {
                    logger.info("Trying to delete member, but no member was selected");
                }
            }
        }


}
