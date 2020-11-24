package main;

import entertainment.Season;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import java.util.Map;

public class Command {

    public static boolean checkVideoExistence(Input input, String title) {
        boolean movieExists = false;
        /**
         * Verify if the video title is in the movie database
         */
        for (MovieInputData movie : input.getMovies()) {
            if (movie.getTitle().equals(title)) {
                movieExists = true;
                break;
            }
        }
        /**
         * Verify if the video title is in the serial database
         */
        if (!movieExists) {
            for (SerialInputData serial : input.getSerials()) {
                if (serial.getTitle().equals(title)) {
                    movieExists = true;
                    break;
                }
            }
        }
        /**
         * If the video is not in a database, return false
         */
        if (!movieExists) {
            return false;
        }
        return true;
    }

    public static boolean checkVideoAsSeen(Input input, String username, String title) {
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                for (Map.Entry<String, Integer> videoEntry : user.getHistory().entrySet()) {
                    if (videoEntry.getKey().equals(title)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * "action_type":"command", "type":"favorite"
     */
    public static String favorite(Input input, String username, String title) {
        UserInputData thisUser = null;
        /**
         * If video does not exist in database, return error message
         */
        if (!checkVideoExistence(input, title)) {
            return "error -> " + title + " is not in database";
        }
        /**
         * Search user in database
         */
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                thisUser = user;
                break;
            }
        }
        /**
         * Verify if the user has aleready seen the movie
         * If yes, search for the movie in the favoriteMovies list
         * If the video already exists in the list, return error message
         * Else return success message
         */

        if (thisUser.getHistory().containsKey(title)) {
            if (thisUser.getFavoriteMovies() != null) {
                for (String movieTitle : thisUser.getFavoriteMovies()) {
                    if (movieTitle.equals(title)) {
                        return "error -> " + title + " is already in favourite list";
                    }
                }
            } else thisUser.newFavoriteMovies();
            thisUser.getFavoriteMovies().add(title);
        } else {
            return "error -> " + title + " is not seen";
        }
        return "success -> " + title + " was added as favourite";
    }

    /**
     * "action_type":"command", "type":"view"
     */
    public static String view(Input input, String username, String title) {
        int onceSeen = 1;
        UserInputData thisUser = null;
        /**
         * If video does not exist in database, return error message
         */
        if (!checkVideoExistence(input, title)) {
            return "error -> " + title + " is not in database";
        }
        /**
         * Search user in database
         */
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                thisUser = user;
                break;
            }
        }
        /**
         * Check the history to see if the video was already seen
         * In this case, increment number of views and return success message
         * Otherwise, add video to history, set one as number of wiews and return success message
         */
        for (Map.Entry<String, Integer> viewedVideo : thisUser.getHistory().entrySet()) {
            if (viewedVideo.getKey().equals(title)) {
                Integer numberViews = viewedVideo.getValue() + onceSeen;
                viewedVideo.setValue(numberViews);
                return "success -> " + title + " was viewed with total views of " + viewedVideo.getValue();
            }
        }
        thisUser.getHistory().put(title, onceSeen);
        return "success -> " + title + " was viewed with total views of " + onceSeen;
    }

    /**
     * "action_type":"command", "type":"rating", "season":"0"
     */
    public static String ratingMovie(Input input, String username, String title, double grade) {
        /**
         * Return error message if movie was not seen by user
         */
        if (!checkVideoAsSeen(input, username, title)) {
            return "error -> " + title + " is not seen";
        }
        /**
         * Verify if the video title is in the movie database
         * If user already graded the movie, return error message
         * If not, add the pair <username, grade> to ratings and return success message
         */
        for (MovieInputData movie : input.getMovies()) {
            if (movie.getTitle().equals(title)) {
                for (Map.Entry<String, Double> userRating : movie.getRatings().entrySet()) {
                    if (userRating.getKey().equals(username)) {
                        return "error -> " + title + " has been already rated";
                    }
                }
                movie.getRatings().put(username, grade);
                /**
                 * Search for user in database and increment number of ratings
                 */
                for (UserInputData user : input.getUsers()) {
                    if (user.getUsername().equals(username)) {
                        int currRatings = user.getNumberOfRatings();
                        user.setNumberOfRatings(++currRatings);
                        break;
                    }
                }
                return "success -> " + title + " was rated with " + grade + " by " + username;
            }
        }
        /**
         * If video not found in movie database, return error message
         */
        return "error -> " + title + " is not in database";
    }

    /**
     * "action_type":"command", "type":"rating", "season":"x"
     */
    public static String ratingSeries(Input input, String username, String title, double grade, int seasonNumber) {
        /**
         * Return error message if movie was not seen by user
         */
        if (!checkVideoAsSeen(input, username, title)) {
            return "error -> " + title + " is not seen";
        }
        /**
         *  Verify if the video title is in the serial database
         *  If user already graded this season of the serial, return error message
         *  If not, add the pair <username, grade> to ratings and return success message
         */

        for (SerialInputData serial : input.getSerials()) {
            if (serial.getTitle().equals(title)) {
                for (Season season : serial.getSeasons()) {
                    for (Map.Entry<String, Double> userRating : season.getRatings().entrySet()) {
                        if (userRating.getKey().equals(username)) {
                            return "error -> " + title + " has been already rated";
                        }
                    }
                }
                for (Season season : serial.getSeasons()) {
                    if (season.getCurrentSeason() == seasonNumber) {
                        season.getRatings().put(username, grade);
                        break;
                    }
                }
                /**
                 * Search for user in database and increment number of ratings
                 */
                for (UserInputData user : input.getUsers()) {
                    if (user.getUsername().equals(username)) {
                        int currRatings = user.getNumberOfRatings();
                        user.setNumberOfRatings(++currRatings);
                        break;
                    }
                }
                return "success -> " + title + " was rated with " + grade + " by " + username;
            }
        }
        /**
         * If video not found in serial database, return error message
         */
        return "error -> " + title + " is not in database";
    }


}
