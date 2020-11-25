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
     * @param input database
     * @param title video
     * @return true if the video exists in input, false otherwise
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

        if (!movieExists) {
            return false;
        }
        return true;
    }

    /**
     * @param input database
     * @param username user
     * @param title name of the movie
     * @return true if title exists in user's history and false otherwise
     */
    public static boolean checkVideoAsSeen(final Input input,
                                           final String username, final String title) {
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

}
