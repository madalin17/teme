package fileio;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about an user, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class UserInputData {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private Map<String, Integer> history; // final
    /**
     * Movies added to favorites
     */
    private ArrayList<String> favoriteMovies; // final

    private int numberOfRatings;

    public UserInputData(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
        this.numberOfRatings = 0;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(final int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    /**
     * Creating new favorites list for an user if null
     */
    public void newFavoriteMovies() {
        this.favoriteMovies = new ArrayList<String>();
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + username + '\'' + ", subscriptionType='"
                + subscriptionType + '\'' + ", history="
                + history + ", favoriteMovies="
                + favoriteMovies + '}';
    }
}
