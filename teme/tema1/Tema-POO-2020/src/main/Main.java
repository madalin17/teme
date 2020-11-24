package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        String username, title, message = null, sortType, genre, objectType;
        double grade;
        int seasonNumber, number, year;
        List<String> awards, words;
        for (ActionInputData action : input.getCommands()) {
            if (action.getActionType().equals(Constants.COMMAND)) {
                username = action.getUsername();
                title = action.getTitle();
                if (action.getType().equals(Constants.FAVORITE)) {
                    message = Command.favorite(input, username, title);
                }
                if (action.getType().equals(Constants.VIEW)) {
                    message = Command.view(input, username, title);
                }
                if (action.getType().equals(Constants.RATING)) {
                    grade = action.getGrade();
                    if (action.getSeasonNumber() == 0) {
                        message = Command.ratingMovie(input, username, title, grade);
                    } else {
                        seasonNumber = action.getSeasonNumber();
                        message = Command.ratingSeries(input, username, title, grade, seasonNumber);
                    }
                }
            }
            if (action.getActionType().equals(Constants.QUERY)) {
                sortType = action.getSortType();
                number = action.getNumber();
                objectType = action.getObjectType();
                if (action.getFilters().get(0).get(0) != null) {
                    year = Integer.parseInt(action.getFilters().get(0).get(0));
                } else {
                    year = 0;
                }
                genre = action.getFilters().get(1).get(0);
                if (action.getObjectType().equals(Constants.USERS) &&
                    action.getCriteria().equals(Constants.NUM_RATINGS)) {
                    message = Query.User.ratings(input, number, sortType);
                }
                if (action.getObjectType().equals(Constants.MOVIES) ||
                    action.getObjectType().equals(Constants.SHOWS)) {
                    if (action.getCriteria().equals(Constants.RATINGS)) {
                        message = Query.Video.rating(input, number, objectType, sortType, year, genre);
                    }
                    if (action.getCriteria().equals(Constants.FAVORITE)) {
                        message = Query.Video.favorite(input, number, objectType, sortType, year, genre);
                    }
                    if (action.getCriteria().equals(Constants.LONGEST)) {
                        message = Query.Video.longest(input, number, objectType, sortType, year, genre);
                    }
                    if (action.getCriteria().equals(Constants.MOSTVIEWED)) {
                        message = Query.Video.mostViewed(input, number, objectType, sortType, year, genre);
                    }
                }
                if (action.getObjectType().equals(Constants.ACTORS)) {
                    if (action.getCriteria().equals(Constants.AVERAGE)) {
                        message = Query.Actor.average(input, number, sortType);
                    }
                    if (action.getCriteria().equals(Constants.AWARDS)) {
                        awards = action.getFilters().get(3);
                        message = Query.Actor.awards(input, number, sortType, awards);
                    }
                    if (action.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)) {
                        words = action.getFilters().get(2);
                        message = Query.Actor.filterDescription(input, number, sortType, words);
                    }
                }
            }
            if (action.getActionType().equals(Constants.RECOMMENDATION)) {
                username = action.getUsername();
                if (action.getType().equals(Constants.STANDARD)) {
                    message = Recommendation.standard(input, username);
                }
                if (action.getType().equals(Constants.BESTUNSEEN)) {
                    message = Recommendation.bestUnseen(input, username);
                }
                if (action.getType().equals(Constants.POPULAR)) {
                    message = Recommendation.popular(input, username);
                }
                if (action.getType().equals(Constants.FAVORITE)) {
                    message = Recommendation.favorite(input, username);
                }
                if (action.getType().equals(Constants.SEARCH)) {
                    genre = action.getGenre();
                    message = Recommendation.search(input, username, genre);
                }
            }
            JSONObject jsonObject = fileWriter.writeFile(action.getActionId(), message);
            arrayResult.add(jsonObject);
        }

        fileWriter.closeJSON(arrayResult);
    }
}