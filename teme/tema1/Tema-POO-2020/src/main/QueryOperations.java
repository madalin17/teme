package main;

import common.Constants;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.*;

public class QueryOperations {

    /**
     * For a list return the first(or last) @number numbers as the query format in ref
     */
    public static String printList(ArrayList<String> array, int number, String sortType) {
        String message = "Query result: [";
        if (sortType.equals(Constants.DESC)) {
            Collections.reverse(array);
        }
        if (array.size() < number) {
            number = array.size();
        }
        for (int i = 0; i < number - 1; i++) {
            message += array.get(i);
            message += ", ";
        }
        if (number != 0) {
            message += array.get(number - 1);
        }
        message += ']';
        return message;
    }

    /**
     * Returns a sorted map: sorted alphabetically by key and ascending by value
     * The map contains only movies or only series, depending on objectType
     */
    public static Map<String, Double> ratingsMap(Input input, String objectType, int year, String genre) {
        SortedMap<String, Double> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                if ((movie.getYear() == year || year == 0) && (movie.getGenres().contains(genre) || genre == null)) {
                    map.put(movie.getTitle(), movie.getRating());
                }
            }
        } else if (objectType.equals(Constants.SHOWS)) {
            for (SerialInputData serial : input.getSerials()) {
                if ((serial.getYear() == year || year == 0) && (serial.getGenres().contains(genre) || genre == null)) {
                    map.put(serial.getTitle(), serial.getRating());
                }
            }
        }
        return MapUtil.sortByValues(map);
    }

    public static Map<String, Integer> favoriteMap(Input input, String objectType, int year, String genre) {
        Map<String, Integer> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                movie.setNumberFavorite(0);
            }
            for (UserInputData user : input.getUsers())
                if (user.getFavoriteMovies() != null)
                    for (String title : user.getFavoriteMovies())
                        for (MovieInputData movie : input.getMovies())
                            if (movie.getTitle().equals(title) &&
                                    (movie.getYear() == year || year == 0) && (movie.getGenres().contains(genre) || genre == null)) {
                                int favorite = movie.getNumberFavorite();
                                favorite++;
                                movie.setNumberFavorite(favorite);
                                break;
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
            for (UserInputData user : input.getUsers())
                if (user.getFavoriteMovies() != null)
                    for (String title : user.getFavoriteMovies())
                        for (SerialInputData serial : input.getSerials())
                            if (serial.getTitle().equals(title) &&
                                    (serial.getYear() == year || year == 0) && (serial.getGenres().contains(genre) || genre == null)) {
                                int favorite = serial.getNumberFavorite();
                                serial.setNumberFavorite(++favorite);
                                break;
                            }
            for (SerialInputData serial : input.getSerials()) {
                if (serial.getNumberFavorite() != 0) {
                    map.put(serial.getTitle(), serial.getNumberFavorite());
                }
            }
        }
        return MapUtil.sortByValues(map);
    }

    public static Map<String, Integer> longestMap(Input input, String objectType, int year, String genre) {
        Map<String, Integer> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                if ((movie.getYear() == year || year == 0) && (movie.getGenres().contains(genre) || genre == null)) {
                    map.put(movie.getTitle(), movie.getDuration());
                }
            }
        } else if (objectType.equals(Constants.SHOWS)) {
            for (SerialInputData serial : input.getSerials()) {
                if ((serial.getYear() == year || year == 0) && (serial.getGenres().contains(genre) || genre == null)) {
                    map.put(serial.getTitle(), serial.getDuration());
                }
            }
        }
        return MapUtil.sortByValues(map);
    }

    public static Map<String, Integer> mostViewedMap(Input input, String objectType, int year, String genre) {
        Map<String, Integer> map = new TreeMap<>();
        if (objectType.equals(Constants.MOVIES)) {
            for (MovieInputData movie : input.getMovies()) {
                movie.setViews(0);
            }
            for (UserInputData user : input.getUsers())
                if (user.getHistory() != null)
                    for (Map.Entry<String, Integer> entry : user.getHistory().entrySet())
                        for (MovieInputData movie : input.getMovies())
                            if (movie.getTitle().equals(entry.getKey()) &&
                                    (movie.getYear() == year || year == 0) && (movie.getGenres().contains(genre) || genre == null)) {
                                int views = movie.getViews();
                                views += entry.getValue();
                                movie.setViews(views);
                                break;
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
            for (UserInputData user : input.getUsers())
                if (user.getHistory() != null)
                    for (Map.Entry<String, Integer> entry : user.getHistory().entrySet())
                        for (SerialInputData serial : input.getSerials())
                            if (serial.getTitle().equals(entry.getKey()) &&
                                    (serial.getYear() == year || year == 0) && (serial.getGenres().contains(genre) || genre == null)) {
                                int views = serial.getViews();
                                views += entry.getValue();
                                serial.setViews(views);
                                break;
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
