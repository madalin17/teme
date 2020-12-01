package main;

import entertainment.ViewsByGenre;
import fileio.Input;
import fileio.ShowInput;
import fileio.UserInputData;

import java.util.Map;

public final class RecommendationOperations {

    private RecommendationOperations() {

    }

    /**
     * @param input database
     * @param username username of user to look for in the database
     * @return user with respective username
     */
    public static UserInputData thisUser(final Input input, final String username) {
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     *
     * @param show video we verify if it is in user's history
     * @param user we perform recommendation on
     * @return string to be shown in output
     */
    public static String verifyUnseenVideo(final ShowInput show, final UserInputData user) {
        if (!user.getHistory().containsKey(show.getTitle())) {
            return "StandardRecommendation result: " + show.getTitle();
        }
        return null;
    }

    /**
     * @param map map to be edited
     * @param show video instance for which we add its views to its corresponding genres in the map
     */
    public static void editViewsByGenreMap(final Map<String, ViewsByGenre> map,
                                           final ShowInput show) {
        if (show.getViews() != 0) {
            for (String genre : show.getGenres()) {
                if (!map.containsKey(genre)) {
                    map.put(genre, new ViewsByGenre(show.getViews()));
                } else {
                    for (Map.Entry<String, ViewsByGenre> entry : map.entrySet()) {
                        if (entry.getKey().equals(genre)) {
                            int views = entry.getValue().getTotalNumberOfViews()
                                    + show.getViews();
                            entry.getValue().setTotalNumberOfViews(views);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Function changes numberFavorite variable for all movies
     * that appear at least one time in an favorite movies list of an user
     * @param input database
     */
    public static void editNumberFavorite(final Input input) {
        input.getMovies().forEach(movie -> movie.setNumberFavorite(0));
        input.getSerials().forEach(serial -> serial.setNumberFavorite(0));
        for (UserInputData user : input.getUsers()) {
            if (user.getFavoriteMovies() != null) {
                input.getMovies()
                        .stream()
                        .filter(movie -> user.getFavoriteMovies().contains(movie.getTitle()))
                        .forEach(movie -> {
                            int favorite = movie.getNumberFavorite();
                            movie.setNumberFavorite(++favorite);
                        });
                input.getSerials()
                        .stream()
                        .filter(serial -> user.getFavoriteMovies().contains(serial.getTitle()))
                        .forEach(serial -> {
                            int favorite = serial.getNumberFavorite();
                            serial.setNumberFavorite(++favorite);
                        });
            }
        }
    }

}
