package main;

import common.Constants;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.TreeMap;
import java.util.SortedMap;

public final class QueryOperations {

    private QueryOperations() {

    }

    /**
     * Function returns correct message to be printed in output for every query
     * @param array list of all movies/series/users/actors that pass some conditions
     * @param number N movies/series/users/actors to be shown in output
     * @param sortType string that determines if the sorting is ascending or descending
     * @return string to be shown in output
     */
    public static String printList(final ArrayList<String> array,
                                   final int number, final String sortType) {
        int newNumber = number;
        StringBuilder message = new StringBuilder("Query result: [");
        if (sortType.equals(Constants.DESC)) {
            Collections.reverse(array);
        }
        if (array.size() < number) {
            newNumber = array.size();
        }
        for (int i = 0; i < newNumber - 1; i++) {
            message.append(array.get(i));
            message.append(", ");
        }
        if (newNumber != 0) {
            message.append(array.get(newNumber - 1));
        }
        message.append(']');
        return message.toString();
    }

    /**
     * Function returns a sorted by value Treemap of movies/series filtered by an year and a genre,
     * where the value represents corresponding rating
     * @param input the database
     * @param objectType determines if we operate on shows or movies
     * @param year attribute of a movie
     * @param genre attribute of a movie
     * @return a TreeMap with serial or movie titles and rating for a year and a genre
     */
    public static Map<String, Double> ratingsMap(final Input input, final String objectType,
                                                 final int year, final String genre) {
        SortedMap<String, Double> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                if ((movie.getYear() == year || year == 0)
                        && (movie.getGenres().contains(genre) || genre == null)
                        && movie.getRating() != 0) {
                    map.put(movie.getTitle(), movie.getRating());
                }
            }
        } else if (objectType.equals(Constants.SHOWS)) {
            for (SerialInputData serial : input.getSerials()) {
                if ((serial.getYear() == year || year == 0)
                        && (serial.getGenres().contains(genre) || genre == null)
                        && serial.getRating() != 0) {
                    map.put(serial.getTitle(), serial.getRating());
                }
            }
        }
        return MapUtil.sortByValues(map);
    }

    /**
     * Function returns a sorted by value Treemap of movies/series filtered by an year and a genre,
     * where the value represents corresponding times it appears in favorite lists for users
     * @param input the database
     * @param objectType determines if we operate on shows or movies
     * @param year attribute of a movie
     * @param genre attribute of a movie
     * @return a TreeMap with serial or movie titles
     * and the number of times it appears in users' favorite lists for a year and a genre
     */
    public static Map<String, Integer> favoriteMap(final Input input, final String objectType,
                                                   final int year, final String genre) {
        Map<String, Integer> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                movie.setNumberFavorite(0);
            }
            for (UserInputData user : input.getUsers()) {
                if (user.getFavoriteMovies() != null) {
                    for (String title : user.getFavoriteMovies()) {
                        for (MovieInputData movie : input.getMovies()) {
                            if (movie.getTitle().equals(title)
                                    && (movie.getYear() == year || year == 0)
                                    && (movie.getGenres().contains(genre) || genre == null)) {
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
        } else if (objectType.equals(Constants.SHOWS)) {
            for (SerialInputData serial : input.getSerials()) {
                serial.setNumberFavorite(0);
            }
            for (UserInputData user : input.getUsers()) {
                if (user.getFavoriteMovies() != null) {
                    for (String title : user.getFavoriteMovies()) {
                        for (SerialInputData serial : input.getSerials()) {
                            if (serial.getTitle().equals(title)
                                    && (serial.getYear() == year || year == 0)
                                    && (serial.getGenres().contains(genre) || genre == null)) {
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
        }
        return MapUtil.sortByValues(map);
    }

    /**
     * Function returns a sorted by value Treemap of movies/series filtered by an year and a genre,
     * where the value represents corresponding total duration
     * @param input the database
     * @param objectType determines if we operate on shows or movies
     * @param year attribute of a movie
     * @param genre attribute of a movie
     * @return a TreeMap with serial or movie titles and duration for a year and a genre
     */
    public static Map<String, Integer> longestMap(final Input input, final String objectType,
                                                  final int year, final String genre) {
        Map<String, Integer> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                if ((movie.getYear() == year || year == 0)
                        && (movie.getGenres().contains(genre) || genre == null)) {
                    map.put(movie.getTitle(), movie.getDuration());
                }
            }
        } else if (objectType.equals(Constants.SHOWS)) {
            for (SerialInputData serial : input.getSerials()) {
                if ((serial.getYear() == year || year == 0)
                        && (serial.getGenres().contains(genre) || genre == null)) {
                    map.put(serial.getTitle(), serial.getDuration());
                }
            }
        }
        return MapUtil.sortByValues(map);
    }

    /**
     * Function returns a sorted by value Treemap of movies/series filtered by an year and a genre,
     * where the value represents corresponding times a show was viewed by users
     * @param input the database
     * @param objectType determines if we operate on shows or movies
     * @param year attribute of a movie
     * @param genre attribute of a movie
     * @return a TreeMap with serial or movie titles and views for a year and a genre
     */
    public static Map<String, Integer> mostViewedMap(final Input input, final String objectType,
                                                     final int year, final String genre) {
        Map<String, Integer> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                movie.setViews(0);
            }
            for (UserInputData user : input.getUsers()) {
                if (user.getHistory() != null) {
                    for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                        for (MovieInputData movie : input.getMovies()) {
                            if (movie.getTitle().equals(entry.getKey())
                                    && (movie.getYear() == year || year == 0)
                                    && (movie.getGenres().contains(genre) || genre == null)) {
                                int views = movie.getViews();
                                views += entry.getValue();
                                movie.setViews(views);
                                break;
                            }
                        }
                    }
                }
            }
            for (MovieInputData movie : input.getMovies()) {
                if (movie.getViews() != 0) {
                    map.put(movie.getTitle(), movie.getViews());
                }
            }
        } else if (objectType.equals(Constants.SHOWS)) {
            for (SerialInputData serial : input.getSerials()) {
                serial.setViews(0);
            }
            for (UserInputData user : input.getUsers()) {
                if (user.getHistory() != null) {
                    for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                        for (SerialInputData serial : input.getSerials()) {
                            if (serial.getTitle().equals(entry.getKey())
                                    && (serial.getYear() == year || year == 0)
                                    && (serial.getGenres().contains(genre) || genre == null)) {
                                int views = serial.getViews();
                                views += entry.getValue();
                                serial.setViews(views);
                                break;
                            }
                        }
                    }
                }
            }
            for (SerialInputData serial : input.getSerials()) {
                if (serial.getViews() != 0) {
                    map.put(serial.getTitle(), serial.getViews());
                }
            }
        }
        return MapUtil.sortByValues(map);
    }
}
