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
import java.util.Set;
import java.util.List;

public final class Recommendation {

    private Recommendation() {

    }

    /**
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String standard(final Input input, final String username) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        for (MovieInputData movie : input.getMovies()) {
            boolean movieExists = false;
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                if (movie.getTitle().equals(entry.getKey())) {
                    movieExists = true;
                    break;
                }
            }
            if (!movieExists) {
                return "StandardRecommendation result: " + movie.getTitle();
            }
        }
        for (SerialInputData serial : input.getSerials()) {
            boolean movieExists = false;
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                if (serial.getTitle().equals(entry.getKey())) {
                    movieExists = true;
                    break;
                }
            }
            if (!movieExists) {
                return "StandardRecommendation result: " + serial.getTitle();
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    /**
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String bestUnseen(final Input input, final String username) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        if (user == null) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        ArrayList<String> titles = new ArrayList<String>(user.getHistory().keySet());
        Map<String, Double> map = new LinkedHashMap<String, Double>();

        for (MovieInputData movie : input.getMovies()) {
            if (!titles.contains(movie.getTitle())) {
                map.put(movie.getTitle(), movie.getRating());
            }
        }
        for (SerialInputData serial : input.getSerials()) {
            if (!titles.contains(serial.getTitle())) {
                map.put(serial.getTitle(), serial.getRating());
            }
        }

        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        System.out.println();
        if (sortedMap.size() == 0) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        ArrayList<String> bestUnseen = new ArrayList<String>(sortedMap.keySet());
        return "BestRatedUnseenRecommendation result: " + bestUnseen.get(0);
    }

    /**
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String popular(final Input input, final String username) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        if (user == null || !user.getSubscriptionType().equals(Constants.PREMIUM)) {
            return "PopularRecommendation cannot be applied";
        }

        ArrayList<String> seenVideos = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            seenVideos.add(entry.getKey());
        }

        QueryOperations.mostViewedMap(input, Constants.MOVIES, 0, null);
        QueryOperations.mostViewedMap(input, Constants.SHOWS, 0, null);

        Map<String, ViewsByGenre> map = new TreeMap<String, ViewsByGenre>();
        for (MovieInputData movie : input.getMovies()) {
            if (movie.getViews() != 0) {
                for (String genre : movie.getGenres()) {
                    if (!map.containsKey(genre)) {
                        map.put(genre, new ViewsByGenre(movie.getViews()));
                    } else {
                        for (Map.Entry<String, ViewsByGenre> entry : map.entrySet()) {
                            if (entry.getKey().equals(genre)) {
                                int views = entry.getValue().getTotalNumberOfViews()
                                        + movie.getViews();
                                entry.getValue().setTotalNumberOfViews(views);
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (SerialInputData serial : input.getSerials()) {
            if (serial.getViews() != 0) {
                for (String genre : serial.getGenres()) {
                    if (!map.containsKey(genre)) {
                        map.put(genre, new ViewsByGenre(serial.getViews()));
                    } else {
                        for (Map.Entry<String, ViewsByGenre> entry : map.entrySet()) {
                            if (entry.getKey().equals(genre)) {
                                int views = entry.getValue().getTotalNumberOfViews()
                                        + serial.getViews();
                                entry.getValue().setTotalNumberOfViews(views);
                                break;
                            }
                        }
                    }
                }
            }
        }

        Map<String, ViewsByGenre> sortedMap = MapUtil.sortByValues(map);

        String mostPopularGenre = null;
        int firstFreeGenre = 0;

        while (firstFreeGenre < sortedMap.size()) {
            int currentGenre = 0;
            for (Map.Entry<String, ViewsByGenre> entry : sortedMap.entrySet()) {
                if (currentGenre == firstFreeGenre) {
                    mostPopularGenre = entry.getKey();
                    break;
                }
                currentGenre++;
            }
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
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String favorite(final Input input, final String username) {
        UserInputData thisUser = RecommendationOperations.thisUser(input, username);
        if (thisUser == null || !thisUser.getSubscriptionType().equals(Constants.PREMIUM)) {
            return "FavoriteRecommendation cannot be applied!";
        }
        ArrayList<String> seenVideos = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : thisUser.getHistory().entrySet()) {
            seenVideos.add(entry.getKey());
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        for (MovieInputData movie : input.getMovies()) {
            movie.setNumberFavorite(0);
        }
        for (UserInputData user : input.getUsers()) {
            if (user.getFavoriteMovies() != null) {
                for (String title : user.getFavoriteMovies()) {
                    for (MovieInputData movie : input.getMovies()) {
                        if (movie.getTitle().equals(title)) {
                            int favorite = movie.getNumberFavorite();
                            favorite++;
                            movie.setNumberFavorite(favorite);
                            break;
                        }
                    }
                }
            }
        }
        for (MovieInputData movie : input.getMovies()) {
            if (movie.getNumberFavorite() != 0) {
                map.put(movie.getTitle(), movie.getNumberFavorite());
            }
        }
        for (SerialInputData serial : input.getSerials()) {
            serial.setNumberFavorite(0);
        }
        for (UserInputData user : input.getUsers()) {
            if (user.getFavoriteMovies() != null) {
                for (String title : user.getFavoriteMovies()) {
                    for (SerialInputData serial : input.getSerials()) {
                        if (serial.getTitle().equals(title)) {
                            int favorite = serial.getNumberFavorite();
                            serial.setNumberFavorite(++favorite);
                            break;
                        }
                    }
                }
            }
        }
        for (SerialInputData serial : input.getSerials()) {
            if (serial.getNumberFavorite() != 0) {
                map.put(serial.getTitle(), serial.getNumberFavorite());
            }
        }

        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        Set<String> vids = sortedMap.keySet();
        List<String> videos = new ArrayList<>(vids);
        for (String video : videos) {
            if (!seenVideos.contains(video)) {
                return "FavoriteRecommendation result: " + video;
            }
        }
        return "FavoriteRecommendation cannot be applied!";
    }

    /**
     * @param input database
     * @param username user to perform recommendation on
     * @return string to be shown in output
     */
    public static String search(final Input input, final String username, final String genre) {
        UserInputData user = RecommendationOperations.thisUser(input, username);
        if (user == null || !user.getSubscriptionType().equals(Constants.PREMIUM)) {
            return "SearchRecommendation cannot be applied";
        }
        ArrayList<String> seenVideos = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            seenVideos.add(entry.getKey());
        }

        Map<String, Double> movieMap =
                QueryOperations.ratingsMap(input, Constants.MOVIES, 0, genre);
        Map<String, Double> serialMap =
                QueryOperations.ratingsMap(input, Constants.SHOWS, 0, genre);

        Map<String, Double> map = new TreeMap<>(movieMap);
        serialMap.forEach((key, value) -> map.put(key, value));
        Map<String, Double> sortedMap = MapUtil.sortByValues(map);

        ArrayList<String> sortedVideos = new ArrayList<String>();
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            if (!seenVideos.contains(entry.getKey())) {
                sortedVideos.add(entry.getKey());
            }
        }

        if (sortedVideos.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        }
        return RecommendationOperations.printList(sortedVideos, sortedVideos.size(), Constants.ASC);
    }
}
