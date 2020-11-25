package fileio;

import java.util.ArrayList;

/**
 * General information about show (video), retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public abstract class ShowInput {
    /**
     * Show's title
     */
    private final String title;
    /**
     * The year the show was released
     */
    private final int year;
    /**
     * Show casting
     */
    private final ArrayList<String> cast;
    /**
     * Show genres
     */
    private final ArrayList<String> genres;

    private int numberFavorite = 0;

    private int views = 0;

    public ShowInput(final String title, final int year,
                     final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
    }

    /**
     * @return the number of times a video apprears in a favorite list
     */
    public int getNumberFavorite() {
        return numberFavorite;
    }

    /**
     * @param numberFavorite sets the number of times a video appears in a favorite list
     */
    public void setNumberFavorite(final int numberFavorite) {
        this.numberFavorite = numberFavorite;
    }

    /**
     * @return the number of views a video has
     */
    public int getViews() {
        return views;
    }

    /**
     * @param views sets the number of views a video has
     */
    public void setViews(final int views) {
        this.views = views;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }
}
