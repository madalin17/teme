package entertainment;

public final class ActorRating implements Comparable<ActorRating> {

    private double ratingSum;

    private int count;

    public ActorRating(final double ratingSum, final int count) {
        this.ratingSum = ratingSum;
        this.count = count;
    }

    public double getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(final double ratingSum) {
        this.ratingSum = ratingSum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    /**
     * @return rating of an actor at any point
     */
    public double getRating() {
        if (count != 0) {
            return (double) ratingSum / count;
        }
        return -1;
    }

    @Override
    public int compareTo(final ActorRating otherRating) {
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
