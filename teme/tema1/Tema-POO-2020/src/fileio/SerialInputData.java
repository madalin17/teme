package fileio;

import entertainment.Season;
import java.util.ArrayList;
import java.util.Map;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    /**
     * @return duration of a series
     */
    public int getDuration() {
        int duration = 0;
        for (Season season : seasons) {
            duration += season.getDuration();
        }
        return duration;
    }

    /**
     * @return rating of a series
     */
    public double getRating() {
        double sum = 0, serialSum;
        int count = 0, serialCount;
        for (Season season : seasons) {
            count++;
            serialSum = 0;
            serialCount = 0;
            if (season.getRatings().size() == 0) {
                serialCount++;
            } else {
                for (Map.Entry<String, Double> entry : season.getRatings().entrySet()) {
                    serialSum += entry.getValue();
                    serialCount++;
                }
            }
            double seasonRating = (double) serialSum / serialCount;
            sum += seasonRating;
        }
        double rating = (double) sum / count;
        return rating;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
