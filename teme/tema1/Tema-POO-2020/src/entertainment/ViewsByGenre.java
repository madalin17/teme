package entertainment;

public class ViewsByGenre implements Comparable<ViewsByGenre> {
    private int totalNumberOfViews = 0;

    public ViewsByGenre(int totalNumberOfViews) { this.totalNumberOfViews = totalNumberOfViews; }

    public int getTotalNumberOfViews() { return totalNumberOfViews; }

    public void setTotalNumberOfViews(int totalNumberOfViews) { this.totalNumberOfViews = totalNumberOfViews; }

    @Override
    public int compareTo(ViewsByGenre otherGenre) {
        return -Integer.compare(getTotalNumberOfViews(), otherGenre.getTotalNumberOfViews());
    }
}
