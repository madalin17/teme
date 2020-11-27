package main;

import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.Map;

public final class CommandOperations {

    private CommandOperations() {

    }

    /**
     * Function verifies if the video exists in the movie/serial database
     * @param input database
     * @param title video
     * @return false if the video exists in input, true otherwise
     */
    public static boolean checkVideoExistence(final Input input, final String title) {
        boolean movieExists = false;

        for (MovieInputData movie : input.getMovies()) {
            if (movie.getTitle().equals(title)) {
                movieExists = true;
                break;
            }
        }

        if (!movieExists) {
            for (SerialInputData serial : input.getSerials()) {
                if (serial.getTitle().equals(title)) {
                    movieExists = true;
                    break;
                }
            }
        }

        return !movieExists;
    }

    /**
     * Function verifies if an user already seen a movie
     * @param input database
     * @param username user
     * @param title name of the movie
     * @return false if title exists in user's history, true otherwise
     */
    public static boolean checkVideoAsSeen(final Input input,
                                           final String username, final String title) {
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                for (Map.Entry<String, Integer> videoEntry : user.getHistory().entrySet()) {
                    if (videoEntry.getKey().equals(title)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param input database
     * @param username of user to be found
     * @return corresponding user from database
     */
    public static UserInputData getUser(final Input input, final String username) {
        UserInputData thisUser = null;
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                thisUser = user;
                break;
            }
        }
        assert thisUser != null;
        return thisUser;
    }

    /**
     * Function increments the number of videos an user has rated
     * @param input database
     * @param username of user to perform operation on
     */
    public static void incrementRatings(final Input input, final String username) {
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                int currRatings = user.getNumberOfRatings();
                user.setNumberOfRatings(++currRatings);
                break;
            }
        }
    }

}
