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
     * @param array list of all movies/series/users/actors that pass some conditions
     * @param number N movies/series/users/actors to be shown in output
     * @param sortType string that determines if the sorting is ascendent or descendent
     * @return string to be shown in output
     */
    public static String printList(final ArrayList<String> array,
                                   final int number, final String sortType) {
        int newNumber = number;
        String message = "SearchRecommendation result: [";
        if (sortType.equals(Constants.DESC)) {
            Collections.reverse(array);
        }
        if (array.size() < number) {
            newNumber = array.size();
        }
        for (int i = 0; i < newNumber - 1; i++) {
            message += array.get(i);
            message += ", ";
        }
        if (newNumber != 0) {
            message += array.get(newNumber - 1);
        }
        message += ']';
        return message;
    }

    /**
     * @param input database
     * @param username username of user to look for in the database
     * @return
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
