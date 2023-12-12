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

/**
 * This is class for working with DB using Hibernate
 */
public class DataBaseHandler {
    /**
     * This is logger
     */

    private static final Logger logger = LogManager.getLogger("mainLogger");

    /**
     * This method for getting list of movies from DB
     * @param persistenceUnitName is the name of persistence file for connecting to the DB
     * @param groupsData is empty list of movies for flowing from DB
     */
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

    /**
     * This is function for saving movie to DB
     * @param movie is movie for saving to DB
     */
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
        alert.setContentText("Фильм успешно добавлен, " + "Его номер " + movie.getMovieId());
        alert.showAndWait();
    }

    /**
     * This is function for searching movies by name
     * @param movieName is name of movie for searching
     * @return list of movies searched by name
     */
    public static List<Movie> getSearchedByNameMovies (String movieName){
        logger.info("getting searched by name movies");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Movie> searchedMovies = em.createQuery("select m from Movie m where lower(m.movieName) = lower(:name)", Movie.class).setParameter("name", movieName).getResultList();
        em.getTransaction().commit();
        return searchedMovies;
    }

    /**
     * This is function for searching movies by month
     * @param month is name of month for searching
     * @return list of movies searching by month
     */
    public static List<Movie> getSearchedByMonthMovies(String month) {
        logger.info("getting searched by month movies");
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


    /**
     * This is function for saving session to DB
     * @param movie is movie which session we are going to save
     * @param session is saving session
     */
    public static void saveSessionToDB(Movie movie,Session session) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
            logger.info("Saving new tour to DataBase");

            em.getTransaction().begin();
            movie.getSessions().add(session);
            session.setMovie(movie);
            em.persist(session);
            em.getTransaction().commit();

            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Успех!", null,  session.getSessionId() + " сеанс" + " успешно добавлен");
            logger.info(session.getSessionDate() + "session" + " successfully saved to DB");


    }


    /**
     * This is function for editing session in DB
     * @param selectedSessionId is ID of editing session
     * @param paramValue is map included session's fields and values for session
     * @param persistenceUnitName is the name of persistence file for connecting to the DB
     */
    public static void editDataSession(int selectedSessionId, Map<String,String> paramValue, String persistenceUnitName) {
        logger.info("editing session");
        EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE Session SET countOfSold = ?1, date = ?2, time = ?3 WHERE id = ?4")
                    .setParameter(1, Integer.parseInt(paramValue.get("countOfSold")))
                    .setParameter(2, LocalDate.parse(paramValue.get("date")))
                    .setParameter(3, LocalTime.parse(paramValue.get("time")))
                    .setParameter(4,selectedSessionId)
                    .executeUpdate();
            entityManager.getTransaction().commit();

        AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Успех!", null,  "сеанс" + " успешно изменён");

    }


    /**
     * This is function for editing session in DB
     * @param selectedMovieId
     * @param paramValue is map included session's fields and values for movie
     * @param persistenceUnitName is the name of persistence file for connecting to the DB
     */
    public static void editDataMovie(int selectedMovieId, Map<String,String> paramValue, String persistenceUnitName) {
        logger.info("editing movie");
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

        AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Успех!", null,   "Фильм" + " успешно изменён");

    }


    /**
     * This is function dor removing movie from DB
     * @param movie is movie to delete
     */
    public static void removeMovie( Movie movie){
        boolean k = AlertHandler.makeConfAlertWindow(Alert.AlertType.INFORMATION, "ПОДТВЕРЖДЕНИЕ!", null,   "Вы уверены, что хотите удалить данный фильм?");
       logger.info("checking wish to delete movie" + movie.getMovieName());
        if (k==true) {
            logger.info("deleting movie");
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
            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Успех!", null,   "Фильм" + "успешно удалён");
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


    /**
     * This is function for removing session
     * @param movie is movie which session we are going to remove
     * @param sessionTableView is tableView showing list of session (it's here for update)
     * @param sessionData is list of sessions
     * @param persistenceUnitName is the name of persistence file for connecting to the DB
     */
        public static void removeSession(Movie movie, TableView <Session> sessionTableView, ObservableList<Session> sessionData, String persistenceUnitName) {
            boolean k = AlertHandler.makeConfAlertWindow(Alert.AlertType.INFORMATION, "ПОДТВЕРЖДЕНИЕ!", null, "Вы уверены, что хотите удалить данный сеанс?");
            logger.info("checking wish to delete session" + persistenceUnitName);
            if (k) {
                logger.info("deleting session");
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

                    AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Успех!", null, "Сеанс" + " успешно удалён");

                }

            }
        }


}
