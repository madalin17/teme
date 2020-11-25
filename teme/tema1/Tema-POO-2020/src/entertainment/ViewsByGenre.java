package entertainment;

public final class ViewsByGenre implements Comparable<ViewsByGenre> {
    private int totalNumberOfViews = 0;

    public ViewsByGenre(final int totalNumberOfViews) {
        this.totalNumberOfViews = totalNumberOfViews;
    }

    public int getTotalNumberOfViews() {
        return totalNumberOfViews;
    }

    public void setTotalNumberOfViews(final int totalNumberOfViews) {
        this.totalNumberOfViews = totalNumberOfViews;
    }

    @Override
    public int compareTo(final ViewsByGenre otherGenre) {
        return -Integer.compare(getTotalNumberOfViews(), otherGenre.getTotalNumberOfViews());
    }
}
