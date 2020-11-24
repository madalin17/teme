package entertainment;

public class ActorRating implements Comparable<ActorRating> {

    private double ratingSum;

    private int count;

    public ActorRating(double ratingSum, int count) {
        this.ratingSum = ratingSum;
        this.count = count;
    }

    public double getRatingSum() { return ratingSum; }

    public void setRatingSum(double ratingSum) { this.ratingSum = ratingSum; }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public double getRating() {
        if (count != 0) {
            return (double) ratingSum / count;
        }
        return -1;
    }

    @Override
    public int compareTo(ActorRating otherRating) {
        return Double.compare(getRating(), otherRating.getRating());
    }

    @Override
    public String toString() {
        return "ActorRating{"
                + "ratingSum=" + ratingSum
                + ", count=" + count
                + '}';
    }
}
