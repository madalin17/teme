package main;

import entertainment.Season;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.Map;

public final class Command {

    private Command() {

    }

    /**
     * Function favorite searches the user and verifies if the movie was seen
     * If yes, it verifies if it already exists in user's favorite movies list
     * If not, it adds it and returns a success message; if nor, returns error messages
     * @param input database
     * @param username user that wants to add a film to his favorites list
     * @param title name of video to be put in a favorites list
     * @return string to be shown in output
     */
    public static String favorite(final Input input,
                                  final String username, final String title) {
        UserInputData thisUser = null;

        if (CommandOperations.checkVideoExistence(input, title)) {
            return "error -> " + title + " is not in database";
        }
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                thisUser = user;
                break;
            }
        }

        assert thisUser != null;
        if (thisUser.getHistory().containsKey(title)) {
            if (thisUser.getFavoriteMovies() != null) {
                for (String movieTitle : thisUser.getFavoriteMovies()) {
                    if (movieTitle.equals(title)) {
                        return "error -> " + title + " is already in favourite list";
                    }
                }
            } else {
                thisUser.newFavoriteMovies();
            }
            thisUser.getFavoriteMovies().add(title);
        } else {
            return "error -> " + title + " is not seen";
        }
        return "success -> " + title + " was added as favourite";
    }

    /**
     * Function view searches the user and verifies if the movie already exist in his history
     * If yes, it increases the number of times it was viewed
     * If not, it creates a new entry in user's history with the show's title being only once seen
     * @param input database
     * @param username user that saw the video
     * @param title title of seen video
     * @return string to be shown in output
     */
    public static String view(final Input input,
                              final String username, final String title) {
        int onceSeen = 1;
        UserInputData thisUser = null;
        if (CommandOperations.checkVideoExistence(input, title)) {
            return "error -> " + title + " is not in database";
        }
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                thisUser = user;
                break;
            }
        }

        assert thisUser != null;
        for (Map.Entry<String, Integer> viewedVideo : thisUser.getHistory().entrySet()) {
            if (viewedVideo.getKey().equals(title)) {
                Integer numberViews = viewedVideo.getValue() + onceSeen;
                viewedVideo.setValue(numberViews);
                return "success -> " + title
                        + " was viewed with total views of " + viewedVideo.getValue();
            }
        }
        thisUser.getHistory().put(title, onceSeen);
        return "success -> " + title + " was viewed with total views of " + onceSeen;
    }

    /**
     * Function ratingMovie searches the user and verifies if movie was already rated
     * If yes, it returns an error message
     * If not, creates a new entry in the movie's ratings with the username and the rating
     * And return a success message
     * @param input database
     * @param username user that rates
     * @param title name of video to be rated
     * @param grade rating of video to be rated
     * @return string to be shown in output
     */
    public static String ratingMovie(final Input input, final String username,
                                     final String title, final double grade) {
        if (CommandOperations.checkVideoAsSeen(input, username, title)) {
            return "error -> " + title + " is not seen";
        }

        for (MovieInputData movie : input.getMovies()) {
            if (movie.getTitle().equals(title)) {
                for (Map.Entry<String, Double> userRating : movie.getRatings().entrySet()) {
                    if (userRating.getKey().equals(username)) {
                        return "error -> " + title + " has been already rated";
                    }
                }
                movie.getRatings().put(username, grade);
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
        return "error -> " + title + " is not in database";
    }

    /**
     * Function ratingSeries searches the user and verifies if any serial season was already rated
     * If yes, it returns an error message
     * If not, creates a new entry in the current season's ratings with the username and the rating
     * And returns a success message
     * @param input database
     * @param username user that rates
     * @param title name of video to be rated
     * @param grade rating of video to be rated
     * @param seasonNumber season to be rated
     * @return string to be shown in output
     */
    public static String ratingSeries(final Input input,
                                      final String username, final String title,
                                      final double grade, final int seasonNumber) {
        if (CommandOperations.checkVideoAsSeen(input, username, title)) {
            return "error -> " + title + " is not seen";
        }

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
        return "error -> " + title + " is not in database";
    }

}
