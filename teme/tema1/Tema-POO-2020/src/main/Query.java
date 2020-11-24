package main;

import actor.ActorsAwards;
import common.Constants;
import entertainment.ActorRating;
import entertainment.NumberOfAwards;
import fileio.*;
import utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {

    public static class User {

        /**
         * "action_type":"query", "object_type":"users", "number":"x"
         * "sort_type":"x", "criteria":"num_ratings"
         */
        public static String ratings(Input input, int number, String sortType) {
            SortedMap<String, Integer> mapOfRatings = new TreeMap<>();
            ArrayList<String> sortedUsers = new ArrayList<String>();
            /**
             * Put users in a sorted map by key
             */
            for (UserInputData user : input.getUsers()) {
                if (user.getNumberOfRatings() != 0) {
                    mapOfRatings.put(user.getUsername(), user.getNumberOfRatings());
                }
            }
            Map<String, Integer> sortedMapOfRatings = MapUtil.sortByValues(mapOfRatings);
            /**
             * Put users in a sorted list by number of ratings
             */
            for (Map.Entry<String, Integer> entry : sortedMapOfRatings.entrySet()) {
                sortedUsers.add(entry.getKey());
            }
            return QueryOperations.printList(sortedUsers, number, sortType);
        }
    }

    public static class Video {

        /**
         * "action_type":"query", "object_type":"x", "number":"x"
         * "filters":"x", "sort_type":"x", "criteria":"ratings"
         */
        public static String rating(Input input, int number, String objectType, String sortType, int year, String genre) {
            ArrayList<String> sortedVideos = new ArrayList<String>();
            Map<String, Double> sortedMapOfRatings = QueryOperations.ratingsMap(input, objectType, year, genre);
            for (Map.Entry<String, Double> entry : sortedMapOfRatings.entrySet()) {
                sortedVideos.add(entry.getKey());
            }
            return QueryOperations.printList(sortedVideos, number, sortType);
        }

        public static String favorite(Input input, int number, String objectType, String sortType, int year, String genre) {
            ArrayList<String> sortedFavorites = new ArrayList<String>();
            Map<String, Integer> sortedFavoriteMap = QueryOperations.favoriteMap(input, objectType, year, genre);
            for (Map.Entry<String, Integer> entry : sortedFavoriteMap.entrySet()) {
                sortedFavorites.add(entry.getKey());
            }
            return QueryOperations.printList(sortedFavorites, number, sortType);
        }

        public static String longest(Input input, int number, String objectType, String sortType, int year, String genre) {
            ArrayList<String> sortedLongest = new ArrayList<String>();
            Map<String, Integer> sortedLongestMap = QueryOperations.longestMap(input, objectType, year, genre);
            for (Map.Entry<String, Integer> entry : sortedLongestMap.entrySet()) {
                sortedLongest.add(entry.getKey());
            }
            return QueryOperations.printList(sortedLongest, number, sortType);
        }

        public static String mostViewed(Input input, int number, String objectType, String sortType, int year, String genre) {
            ArrayList<String> sortedMostViewed = new ArrayList<String>();
            Map<String, Integer> sortedMostViewedMap = QueryOperations.mostViewedMap(input, objectType, year, genre);
            for (Map.Entry<String, Integer> entry : sortedMostViewedMap.entrySet()) {
                sortedMostViewed.add(entry.getKey());
            }
            return QueryOperations.printList(sortedMostViewed, number, sortType);
        }
    }

    public static class Actor {

        public static String average(Input input, int number, String sortType) {
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
                                double ratingSum = entry.getValue().getRatingSum() + movie.getRating();
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
                                double ratingSum = entry.getValue().getRatingSum() + serial.getRating();
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

        public static String awards(Input input, int number, String sortType, List<String> awards) {
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
                        if (entry.getKey().equals(actor.getName()) &&
                            awards.contains(award.getKey().toString()) &&
                            !entry.getValue().getAwards().contains(award.getKey().toString())) {
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

        public static String filterDescription(Input input, int number, String sortType, List<String> words) {
            boolean filter;
            ArrayList<String> actors = new ArrayList<String>();

            for (ActorInputData actor : input.getActors()) {
                filter = true;
                String description = actor.getCareerDescription();
                for (String word : words) {
                    Pattern pattern = Pattern.compile("[.,-;'!( ]" + word + "[.,-;'!) ]", Pattern.CASE_INSENSITIVE);
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
