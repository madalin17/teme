package main;

import actor.ActorsAwards;
import common.Constants;
import entertainment.ActorRating;
import entertainment.NumberOfAwards;
import fileio.Input;
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
         * Function ratings creates a sorted by values TreeMap with usernames and number of ratings
         * and prints the first/last @param number keys of the map
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @return string to be shown in output
         */
        public static String ratings(final Input input, final int number, final String sortType) {
            SortedMap<String, Integer> mapOfRatings = new TreeMap<>();

            for (UserInputData user : input.getUsers()) {
                if (user.getNumberOfRatings() != 0) {
                    mapOfRatings.put(user.getUsername(), user.getNumberOfRatings());
                }
            }
            Map<String, Integer> sortedMapOfRatings = MapUtil.sortByValues(mapOfRatings);

            ArrayList<String> sortedUsers = new ArrayList<>(sortedMapOfRatings.keySet());
            return QueryOperations.printList(Constants.QUERY_OPERATION,
                                                sortedUsers, number, sortType);
        }
    }

    public static final class Video {

        private Video() {

        }

        /**
         * Function rating creates a list of movies/serials sorted by year and genre and rating
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
            Map<String, Double> sortedMapOfRatings =
                    QueryOperations.ratingsMap(input, objectType, year, genre);
            ArrayList<String> sortedVideos = new ArrayList<>(sortedMapOfRatings.keySet());
            return QueryOperations.printList(Constants.QUERY_OPERATION,
                                                sortedVideos, number, sortType);
        }

        /**
         * Function favorite creates a list of movies/serials sorted by year, genre and
         * number of times they appear in users' favorites lists
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
            Map<String, Integer> sortedFavoriteMap =
                    QueryOperations.favoriteMap(input, objectType, year, genre);
            ArrayList<String> sortedFavorites = new ArrayList<>(sortedFavoriteMap.keySet());
            return QueryOperations.printList(Constants.QUERY_OPERATION,
                                                sortedFavorites, number, sortType);
        }

        /**
         * Function longest creates a list of movies/serials sorted by year, genre and duration
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
            Map<String, Integer> sortedLongestMap =
                    QueryOperations.longestMap(input, objectType, year, genre);
            ArrayList<String> sortedLongest = new ArrayList<>(sortedLongestMap.keySet());
            return QueryOperations.printList(Constants.QUERY_OPERATION,
                                                sortedLongest, number, sortType);
        }

        /**
         * Function mostViewed creates a list of movies sorted by year, genre and views
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
            Map<String, Integer> sortedMostViewedMap =
                    QueryOperations.mostViewedMap(input, objectType, year, genre);
            ArrayList<String> sortedMostViewed = new ArrayList<>(sortedMostViewedMap.keySet());
            return QueryOperations.printList(Constants.QUERY_OPERATION,
                                                sortedMostViewed, number, sortType);
        }
    }

    public static final class Actor {

        private Actor() {

        }

        /**
         * Function average creates a sorted by ratings TreeMap that contains actors' name and
         * a class that contains sum of ratings and number of ratings for each actor
         * and the first or last @param number of keys are shown in output
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @return string to be shown in output
         */
        public static String average(final Input input, final int number, final String sortType) {
            QueryOperations.ratingsMap(input, Constants.MOVIES, 0, null);
            QueryOperations.ratingsMap(input, Constants.SHOWS, 0, null);

            Map<String, ActorRating> map = new TreeMap<>();
            input.getMovies().forEach(movie -> QueryOperations.editActorRatingMap(map, movie));
            input.getSerials().forEach(serial -> QueryOperations.editActorRatingMap(map, serial));

            Map<String, ActorRating> sortedMap = MapUtil.sortByValues(map);
            ArrayList<String> actors = new ArrayList<>(sortedMap.keySet());
            return QueryOperations.printList(Constants.QUERY_OPERATION, actors, number, sortType);
        }

        /**
         * Function awards creates a sorted by number of total awards Treemap containing
         * an actor name and a class that remembers number of total awards and a list of awards
         * and the first or last @param number of keys are shown in output
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @param awards list of awards, attribute of an actor
         * @return string to be shown in output
         */
        public static String awards(final Input input, final int number,
                                    final String sortType, final List<String> awards) {
            int correctAwards = awards.size();

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

            Map<String, NumberOfAwards> allAwardsMap = new TreeMap<>();
            for (Map.Entry<String, NumberOfAwards> entry : map.entrySet()) {
                if (entry.getValue().getCorrectAwards() == correctAwards) {
                    allAwardsMap.put(entry.getKey(), entry.getValue());
                }
            }

            Map<String, NumberOfAwards> sortedMap = MapUtil.sortByValues(allAwardsMap);
            ArrayList<String> actors = new ArrayList<>(sortedMap.keySet());
            return QueryOperations.printList(Constants.QUERY_OPERATION, actors, number, sortType);
        }

        /**
         * Function filterDescription verifies if an actor's description contains all words listed
         * in case insensitive and adds actor's name to a list in this case
         * List is sorted and the first/last @param number actors are shown in output
         * @param input database
         * @param number N users to be shown in output
         * @param sortType sorting type, if ascending or descending
         * @param words strings to search for in an actor's description
         * @return string to be shown in output
         */
        public static String filterDescription(final Input input, final int number,
                                               final String sortType, final List<String> words) {
            boolean filter;
            ArrayList<String> actors = new ArrayList<>();

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
            return QueryOperations.printList(Constants.QUERY_OPERATION, actors, number, sortType);
        }

    }

}
