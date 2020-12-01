package main;

import actor.ActorsAwards;
import common.Constants;
import entertainment.ActorRating;
import entertainment.NumberOfAwards;
import fileio.Input;
import fileio.UserInputData;
import fileio.MovieInputData;
import fileio.ShowInput;
import fileio.SerialInputData;
import fileio.ActorInputData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;

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
    public static String printList(final String operation, final ArrayList<String> array,
                                   final int number, final String sortType) {
        int newNumber = number;
        StringBuilder message = new StringBuilder(operation);
        message.append(" result: [");
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
            input.getMovies()
                    .stream()
                    .filter(movie -> (movie.getYear() == year || year == 0)
                                && (movie.getGenres().contains(genre) || genre == null)
                                && movie.getRating() != 0)
                    .forEach(movie -> map.put(movie.getTitle(), movie.getRating()));
        } else if (objectType.equals(Constants.SHOWS)) {
            input.getSerials()
                    .stream()
                    .filter(serial -> (serial.getYear() == year || year == 0)
                            && (serial.getGenres().contains(genre) || genre == null)
                            && serial.getRating() != 0)
                    .forEach(serial -> map.put(serial.getTitle(), serial.getRating()));
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
            input.getMovies().forEach(movie -> movie.setNumberFavorite(0));
            for (UserInputData user : input.getUsers()) {
                if (user.getFavoriteMovies() != null) {
                    for (MovieInputData movie : input.getMovies()) {
                        QueryOperations.editNumberFavorite(movie, user, year, genre);
                    }
                }
            }
            input.getMovies()
                    .stream()
                    .filter(movie -> movie.getNumberFavorite() != 0)
                    .forEach(movie -> map.put(movie.getTitle(), movie.getNumberFavorite()));
        } else if (objectType.equals(Constants.SHOWS)) {
            input.getSerials().forEach(serial -> serial.setNumberFavorite(0));
            for (UserInputData user : input.getUsers()) {
                if (user.getFavoriteMovies() != null) {
                    for (SerialInputData serial : input.getSerials()) {
                        QueryOperations.editNumberFavorite(serial, user, year, genre);
                    }
                }
            }
            input.getSerials()
                    .stream()
                    .filter(serial -> serial.getNumberFavorite() != 0)
                    .forEach(serial -> map.put(serial.getTitle(), serial.getNumberFavorite()));
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
            input.getMovies()
                    .stream()
                    .filter(movie -> (movie.getYear() == year || year == 0)
                                && (movie.getGenres().contains(genre) || genre == null))
                    .forEach(movie -> map.put(movie.getTitle(), movie.getDuration()));
        } else if (objectType.equals(Constants.SHOWS)) {
            input.getSerials()
                    .stream()
                    .filter(serial -> (serial.getYear() == year || year == 0)
                                && (serial.getGenres().contains(genre) || genre == null))
                    .forEach(serial -> map.put(serial.getTitle(), serial.getDuration()));
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
            input.getMovies().forEach(movie -> movie.setViews(0));
            for (UserInputData user : input.getUsers()) {
                for (MovieInputData movie : input.getMovies()) {
                    QueryOperations.editViews(movie, user, year, genre);
                }
            }
            input.getMovies()
                    .stream()
                    .filter(movie -> movie.getViews() != 0)
                    .forEach(movie -> map.put(movie.getTitle(), movie.getViews()));
        } else if (objectType.equals(Constants.SHOWS)) {
            input.getSerials().forEach(serial -> serial.setViews(0));
            for (UserInputData user : input.getUsers()) {
                for (SerialInputData serial : input.getSerials()) {
                    QueryOperations.editViews(serial, user, year, genre);
                }
            }
            input.getSerials()
                    .stream()
                    .filter(serial -> serial.getViews() != 0)
                    .forEach(serial -> map.put(serial.getTitle(), serial.getViews()));
        }
        return MapUtil.sortByValues(map);
    }

    /**
     * Function searches a video's cast and adds actors to map
     * or sets a new rating and count for an actor already existing in map
     * @param map map to be edited
     * @param show video instance for which we verify if actor is in the cast
     */
    public static void editActorRatingMap(final Map<String, ActorRating> map,
                                          final ShowInput show) {
        int oneRating = 1;
        for (String actor : show.getCast()) {
            if (!map.containsKey(actor) && show.getRating() != 0) {
                map.put(actor, new ActorRating(show.getRating(), oneRating));
            } else {
                for (Map.Entry<String, ActorRating> entry : map.entrySet()) {
                    if (entry.getKey().equals(actor) && show.getRating() != 0) {
                        double ratingSum =
                                entry.getValue().getRatingSum() + show.getRating();
                        int count = entry.getValue().getCount() + oneRating;
                        entry.getValue().setRatingSum(ratingSum);
                        entry.getValue().setCount(count);
                    }
                }
            }
        }
    }

    /**
     * Function edits the number of times a video appears in users' favorites lists
     * @param show we modify views on
     * @param user we verify if has show in favorite videos list
     * @param year filter year
     * @param genre filter genre
     */
    public static void editNumberFavorite(final ShowInput show, final UserInputData user,
                                          final int year, final String genre) {
        if (user.getFavoriteMovies().contains(show.getTitle())
                && (show.getYear() == year || year == 0)
                && (show.getGenres().contains(genre) || genre == null)) {
            int favorite = show.getNumberFavorite();
            show.setNumberFavorite(++favorite);
        }
    }

    /**
     * Function edits the number of views of a show if it's in an user's history
     * @param show we modify views on
     * @param user we verify if has show in history
     * @param year filter year
     * @param genre filter genre
     */
    public static void editViews(final ShowInput show, final UserInputData user,
                                 final int year, final String genre) {
        if (user.getHistory().containsKey(show.getTitle())
                && (show.getYear() == year || year == 0)
                && (show.getGenres().contains(genre) || genre == null)) {
            int views = show.getViews() + user.getHistory().get(show.getTitle());
            show.setViews(views);
        }
    }

    /**
     * If an actor has all awards, we add to the map the entry consisting of the actor's name
     * and a class that retains the total number of awards for the actor
     * @param input database
     * @param awards list of awards to search for every actor
     * @return map af all actors and number of total awards
     */
    public static Map<String, NumberOfAwards> editAwardsMap(final Input input,
                                                            final List<String> awards) {
        Map<String, NumberOfAwards> map = new TreeMap<>();
        for (ActorInputData actor : input.getActors()) {
            if (!map.containsKey(actor.getName())) {
                map.put(actor.getName(), new NumberOfAwards(0, 0));
            }
            int totalAwards = 0;
            for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                totalAwards += entry.getValue();
            }
            for (Map.Entry<ActorsAwards, Integer> award : actor.getAwards().entrySet()) {
                for (Map.Entry<String, NumberOfAwards> entry : map.entrySet()) {
                    if (entry.getKey().equals(actor.getName())
                            && awards.contains(award.getKey().toString())
                            && !entry.getValue().getAwards()
                            .contains(award.getKey().toString())) {
                        int newCorrectAwards = entry.getValue().getCorrectAwards() + 1;
                        entry.getValue().getAwards().add(award.getKey().toString());
                        entry.getValue().setCorrectAwards(newCorrectAwards);
                        entry.getValue().setTotalAwards(totalAwards);
                    }
                }
            }
        }
        return map;
    }
}
