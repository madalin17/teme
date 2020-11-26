package entertainment;

import java.util.ArrayList;
import java.util.List;

public final class NumberOfAwards implements Comparable<NumberOfAwards> {

    private List<String> awards;

    private int correctAwards;

    private int totalAwards;

    public NumberOfAwards(final int correctAwards, final int totalAwards) {
        this.awards = new ArrayList<>();
        this.correctAwards = correctAwards;
        this.totalAwards = totalAwards;
    }

    public int getCorrectAwards() {
        return correctAwards;
    }

    public void setCorrectAwards(final int correctAwards) {
        this.correctAwards = correctAwards;
    }

    public int getTotalAwards() {
        return totalAwards;
    }

    public void setTotalAwards(final int totalAwards) {
        this.totalAwards = totalAwards;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(final List<String> awards) {
        this.awards = awards;
    }

    @Override
    public int compareTo(final NumberOfAwards numberAwards) {
        return Integer.compare(getTotalAwards(), numberAwards.getTotalAwards());
    }

    @Override
    public String toString() {
        return "NumberOfAwards{"
                + "awards=" + awards
                + ", correctAwards=" + correctAwards
                + ", totalAwards=" + totalAwards
                + '}';
    }
}
