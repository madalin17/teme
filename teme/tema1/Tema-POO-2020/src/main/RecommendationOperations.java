package main;

import common.Constants;
import fileio.Input;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Collections;

public final class RecommendationOperations {

    private RecommendationOperations() {

    }

    /**
     * unction returns correct message to be printed in output for search recommendation
     * @param array list of all movies/series/users/actors that pass some conditions
     * @param number N movies/series/users/actors to be shown in output
     * @param sortType string that determines if the sorting is ascending or descending
     * @return string to be shown in output
     */
    public static String printList(final ArrayList<String> array,
                                   final int number, final String sortType) {
        int newNumber = number;
        StringBuilder message = new StringBuilder("SearchRecommendation result: [");
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

}
