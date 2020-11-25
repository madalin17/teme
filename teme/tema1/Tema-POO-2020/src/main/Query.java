package main;

import actor.ActorsAwards;
import common.Constants;
import entertainment.ActorRating;
import entertainment.NumberOfAwards;
import fileio.Input;
import fileio.SerialInputData;
import fileio.MovieInputData;
import fileio.ActorInputData;
import fileio.UserInputData;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Query {

    private Query() {

    }

    public static final class User {

        private User() {

        }

        /**
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @return string to be shown in output
         */
        public static String ratings(final Input input, final int number, final String sortType) {
            SortedMap<String, Integer> mapOfRatings = new TreeMap<>();
            ArrayList<String> sortedUsers = new ArrayList<>();

            for (UserInputData user : input.getUsers()) {
                if (user.getNumberOfRatings() != 0) {
                    mapOfRatings.put(user.getUsername(), user.getNumberOfRatings());
                }
            }
            Map<String, Integer> sortedMapOfRatings = MapUtil.sortByValues(mapOfRatings);

            for (Map.Entry<String, Integer> entry : sortedMapOfRatings.entrySet()) {
                sortedUsers.add(entry.getKey());
            }
            return QueryOperations.printList(sortedUsers, number, sortType);
        }
    }

    public static final class Video {

        private Video() {

        }

        /**
         * @param input database
         * @param number N videos to be shown in output
         * @param objectType if movies or series
         * @param sortType if ascending or descending
         * @param year attribute of a movie
         * @param genre attribute of a movie
         * @return string to be shown in output
         */
        public static String rating(final Input input, final int number, final String objectType,
                                    final String sortType, final int year, final String genre) {
            ArrayList<String> sortedVideos = new ArrayList<String>();
            Map<String, Double> sortedMapOfRatings =
                    QueryOperations.ratingsMap(input, objectType, year, genre);
            for (Map.Entry<String, Double> entry : sortedMapOfRatings.entrySet()) {
                sortedVideos.add(entry.getKey());
            }
            return QueryOperations.printList(sortedVideos, number, sortType);
        }

        /**
         * @param input database
         * @param number N videos to be shown in output
         * @param objectType if movies or series
         * @param sortType if ascending or descending
         * @param year attribute of a movie
         * @param genre attribute of a movie
         * @return string to be shown in output
         */
        public static String favorite(final Input input, final int number, final String objectType,
                                      final String sortType, final int year, final String genre) {
            ArrayList<String> sortedFavorites = new ArrayList<String>();
            Map<String, Integer> sortedFavoriteMap =
                    QueryOperations.favoriteMap(input, objectType, year, genre);
            for (Map.Entry<String, Integer> entry : sortedFavoriteMap.entrySet()) {
                sortedFavorites.add(entry.getKey());
            }
            return QueryOperations.printList(sortedFavorites, number, sortType);
        }

        /**
         * @param input database
         * @param number N videos to be shown in output
         * @param objectType if movies or series
         * @param sortType if ascending or descending
         * @param year attribute of a movie
         * @param genre attribute of a movie
         * @return string to be shown in output
         */
        public static String longest(final Input input, final int number, final String objectType,
                                     final String sortType, final int year, final String genre) {
            ArrayList<String> sortedLongest = new ArrayList<String>();
            Map<String, Integer> sortedLongestMap =
                    QueryOperations.longestMap(input, objectType, year, genre);
            for (Map.Entry<String, Integer> entry : sortedLongestMap.entrySet()) {
                sortedLongest.add(entry.getKey());
            }
            return QueryOperations.printList(sortedLongest, number, sortType);
        }

        /**
         * @param input database
         * @param number N videos to be shown in output
         * @param objectType if movies or series
         * @param sortType if ascending or descending
         * @param year attribute of a movie
         * @param genre attribute of a movie
         * @return string to be shown in output
         */
        public static String mostViewed(final Input input, final int number,
                                        final String objectType, final String sortType,
                                        final int year, final String genre) {
            ArrayList<String> sortedMostViewed = new ArrayList<String>();
            Map<String, Integer> sortedMostViewedMap =
                    QueryOperations.mostViewedMap(input, objectType, year, genre);
            for (Map.Entry<String, Integer> entry : sortedMostViewedMap.entrySet()) {
                sortedMostViewed.add(entry.getKey());
            }
            return QueryOperations.printList(sortedMostViewed, number, sortType);
        }
    }

    public static final class Actor {

        private Actor() {

        }

        /**
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @return string to be shown in output
         */
        public static String average(final Input input, final int number, final String sortType) {
            int oneRating = 1;

            QueryOperations.ratingsMap(input, Constants.MOVIES, 0, null);
            QueryOperations.ratingsMap(input, Constants.SHOWS, 0, null);

            Map<String, ActorRating> map = new TreeMap<String, ActorRating>();
            for (MovieInputData movie : input.getMovies()) {
                for (String actor : movie.getCast()) {
                    if (!map.containsKey(actor) && movie.getRating() != 0) {
                        map.put(actor, new ActorRating(movie.getRating(), oneRating));
                    } else {
                        for (Map.Entry<String, ActorRating> entry : map.entrySet()) {
                            if (entry.getKey().equals(actor) && movie.getRating() != 0) {
                                double ratingSum =
                                        entry.getValue().getRatingSum() + movie.getRating();
                                int count = entry.getValue().getCount() + oneRating;
                                entry.getValue().setRatingSum(ratingSum);
                                entry.getValue().setCount(count);
                            }
                        }
                    }
                }
            }
            for (SerialInputData serial : input.getSerials()) {
                for (String actor : serial.getCast()) {
                    if (!map.containsKey(actor) && serial.getRating() != 0) {
                        map.put(actor, new ActorRating(serial.getRating(), oneRating));
                    } else {
                        for (Map.Entry<String, ActorRating> entry : map.entrySet()) {
                            if (entry.getKey().equals(actor) && serial.getRating() != 0) {
                                double ratingSum =
                                        entry.getValue().getRatingSum() + serial.getRating();
                                int count = entry.getValue().getCount() + oneRating;
                                entry.getValue().setRatingSum(ratingSum);
                                entry.getValue().setCount(count);
                            }
                        }
                    }
                }
            }

            Map<String, ActorRating> sortedMap = MapUtil.sortByValues(map);
            ArrayList<String> actors = new ArrayList<>(sortedMap.keySet());
            return QueryOperations.printList(actors, number, sortType);
        }

        /**
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @param awards list of awards, attribute of an actor
         * @return string to be shown in output
         */
        public static String awards(final Input input, final int number,
                                    final String sortType, final List<String> awards) {
            int correctAwards = awards.size();

            Map<String, NumberOfAwards> map = new TreeMap<String, NumberOfAwards>();
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
                            int newCorrectawards = entry.getValue().getCorrectAwards() + 1;
                            entry.getValue().getAwards().add(award.getKey().toString());
                            entry.getValue().setCorrectAwards(newCorrectawards);
                            entry.getValue().setTotalAwards(totalAwards);
                        }
                    }
                }
            }

            Map<String, NumberOfAwards> allAwardsMap = new TreeMap<>();
            for (Map.Entry<String, NumberOfAwards> entry : map.entrySet()) {
                if (entry.getValue().getCorrectAwards() == correctAwards) {
                    allAwardsMap.put(entry.getKey(), entry.getValue());
                }
            }

            Map<String, NumberOfAwards> sortedMap = MapUtil.sortByValues(allAwardsMap);
            ArrayList<String> actors = new ArrayList<>(sortedMap.keySet());
            return QueryOperations.printList(actors, number, sortType);
        }

        /**
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @param words strings to search for in an actor's description
         * @return string to be shown in output
         */
        public static String filterDescription(final Input input, final int number,
                                               final String sortType, final List<String> words) {
            boolean filter;
            ArrayList<String> actors = new ArrayList<String>();

            for (ActorInputData actor : input.getActors()) {
                filter = true;
                String description = actor.getCareerDescription();
                for (String word : words) {
                    Pattern pattern = Pattern.compile("[.,-;'!( ]"
                            + word + "[.,-;'!) ]", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(description);
                    if (!matcher.find()) {
                        filter = false;
                        break;
                    }
                }
                if (filter) {
                    actors.add(actor.getName());
                }
            }

            Collections.sort(actors);
            return QueryOperations.printList(actors, number, sortType);
        }

    }

}
