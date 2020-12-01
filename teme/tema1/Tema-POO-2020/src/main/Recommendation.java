package main;

import common.Constants;
import entertainment.ViewsByGenre;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;

public final class Recommendation {

    private Recommendation() {

    }

    /**
     * Function standard returns first unseen video in the database for an user
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String standard(final Input input, final String username) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        assert user != null;
        for (MovieInputData movie : input.getMovies()) {
            if (RecommendationOperations.verifyUnseenVideo(movie, user) != null) {
                return RecommendationOperations.verifyUnseenVideo(movie, user);
            }
        }
        for (SerialInputData serial : input.getSerials()) {
            if (RecommendationOperations.verifyUnseenVideo(serial, user) != null) {
                return RecommendationOperations.verifyUnseenVideo(serial, user);
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    /**
     * Function bestUnseen creates a LinkedHashMap sorted descending by rating that contains
     * all unseen videos of an user and their rating
     * and if the map is not empty returns first video(with maximum rating)
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String bestUnseen(final Input input, final String username) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        assert user != null;
        ArrayList<String> titles = new ArrayList<>(user.getHistory().keySet());
        Map<String, Double> map = new LinkedHashMap<>();

        input.getMovies()
                .stream()
                .filter(movie -> !titles.contains(movie.getTitle()))
                .forEach(movie -> map.put(movie.getTitle(), movie.getRating()));
        input.getSerials()
                .stream()
                .filter(serial -> !titles.contains(serial.getTitle()))
                .forEach(serial -> map.put(serial.getTitle(), serial.getRating()));

        Map<String, Double> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        if (sortedMap.size() == 0) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        ArrayList<String> bestUnseen = new ArrayList<>(sortedMap.keySet());
        return "BestRatedUnseenRecommendation result: " + bestUnseen.get(0);
    }

    /**
     * Function popular creates a TreeMap that contains genres and number of views for each genre
     * The map is sorted descending and we remember the first genre for witch the user has at least
     * a video that has not seen yet
     * We return the first unseen movie in database from this genre
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String popular(final Input input, final String username) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        if (user == null || !user.getSubscriptionType().equals(Constants.PREMIUM)) {
            return "PopularRecommendation cannot be applied!";
        }

        ArrayList<String> seenVideos = new ArrayList<>(user.getHistory().keySet());

        QueryOperations.mostViewedMap(input, Constants.MOVIES, 0, null);
        QueryOperations.mostViewedMap(input, Constants.SHOWS, 0, null);

        Map<String, ViewsByGenre> map = new TreeMap<>();
        input.getMovies().forEach(movie ->
                RecommendationOperations.editViewsByGenreMap(map, movie));
        input.getSerials().forEach(serial ->
                RecommendationOperations.editViewsByGenreMap(map, serial));

        Map<String, ViewsByGenre> sortedMap = MapUtil.sortByValues(map);

        int firstFreeGenre = 0;
        while (firstFreeGenre < sortedMap.size()) {
            List<String> genres = new ArrayList<>(sortedMap.keySet());
            String mostPopularGenre = genres.get(firstFreeGenre);
            for (MovieInputData movie : input.getMovies()) {
                if (movie.getGenres().contains(mostPopularGenre)
                        && !seenVideos.contains(movie.getTitle())) {
                    return "PopularRecommendation result: " + movie.getTitle();
                }
            }
            for (SerialInputData serial : input.getSerials()) {
                if (serial.getGenres().contains(mostPopularGenre)
                        && !seenVideos.contains(serial.getTitle())) {
                    return "PopularRecommendation result: " + serial.getTitle();
                }
            }
            firstFreeGenre++;
        }
        return "PopularRecommendation cannot be applied!";
    }

    /**
     * Function favorite creates a LinkedHashMap that contains a movie/serial
     * and the number of times it appears is users' favorites lists
     * The map is sorted descending by value and returns first unseen video
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String favorite(final Input input, final String username) {
        UserInputData thisUser = RecommendationOperations.thisUser(input, username);
        if (thisUser == null || !thisUser.getSubscriptionType().equals(Constants.PREMIUM)) {
            return "FavoriteRecommendation cannot be applied!";
        }
        ArrayList<String> seenVideos = new ArrayList<>(thisUser.getHistory().keySet());

        Map<String, Integer> map = new HashMap<>();
        RecommendationOperations.editNumberFavorite(input);
        input.getMovies()
                .stream()
                .filter(movie -> movie.getNumberFavorite() != 0
                            && !seenVideos.contains(movie.getTitle()))
                .forEach(movie -> map.put(movie.getTitle(), movie.getNumberFavorite()));
        input.getSerials()
                .stream()
                .filter(serial -> serial.getNumberFavorite() != 0
                            && !seenVideos.contains(serial.getTitle()))
                .forEach(serial -> map.put(serial.getTitle(), serial.getNumberFavorite()));

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        List<String> videos = new ArrayList<>(sortedMap.keySet());
        if (videos.size() != 0) {
            return "FavoriteRecommendation result: " + videos.get(0);
        }
        return "FavoriteRecommendation cannot be applied!";
    }

    /**
     * Function search creates a sorted by ratings TreeMap with movies from the same genre
     * and their rating; all unseen movies from this map are moved to an arraylist shown to output
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String search(final Input input, final String username, final String genre) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        if (user == null || !user.getSubscriptionType().equals(Constants.PREMIUM)) {
            return "SearchRecommendation cannot be applied!";
        }
        ArrayList<String> seenVideos = new ArrayList<>(user.getHistory().keySet());
        Map<String, Double> map = new TreeMap<>();
        input.getMovies()
                .stream()
                .filter(movie -> movie.getGenres().contains(genre)
                        && !seenVideos.contains(movie.getTitle()))
                .forEach(movie -> map.put(movie.getTitle(), movie.getRating()));
        input.getSerials()
                .stream()
                .filter(serial -> serial.getGenres().contains(genre)
                            && !seenVideos.contains(serial.getTitle()))
                .forEach(serial -> map.put(serial.getTitle(), serial.getRating()));

        Map<String, Double> sortedMap = MapUtil.sortByValues(map);
        ArrayList<String> sortedVideos = new ArrayList<>(sortedMap.keySet());
        if (sortedVideos.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        }
        return QueryOperations.printList(Constants.RECOMMENDATION_OPERATION,
                                            sortedVideos, sortedVideos.size(), Constants.ASC);
    }
}
