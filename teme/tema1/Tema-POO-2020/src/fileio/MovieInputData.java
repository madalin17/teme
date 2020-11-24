package fileio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;

    private Map<String, Double> ratings = new HashMap<String, Double>();

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public Map<String, Double> getRatings() { return ratings; }

    public double getRating() {
        double sum = 0;
        int count = 0;
        if (ratings.size() == 0) {
            return 0;
        }
        for (Map.Entry<String, Double> entry : ratings.entrySet()) {
            sum += entry.getValue();
            count++;
        }
        double rating = (double) sum / count;
        return rating;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }
}
