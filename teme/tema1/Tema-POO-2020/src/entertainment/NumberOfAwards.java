package entertainment;

import java.util.ArrayList;
import java.util.List;

public class NumberOfAwards implements Comparable<NumberOfAwards> {

    private List<String> awards;

    private int correctAwards;

    private int totalAwards;

    public NumberOfAwards(int correctAwards, int totalAwards) {
        this.awards = new ArrayList<String>();
        this.correctAwards = correctAwards;
        this.totalAwards = totalAwards;
    }

    public int getCorrectAwards() { return correctAwards; }

    public void setCorrectAwards(int correctAwards) { this.correctAwards = correctAwards; }

    public int getTotalAwards() { return totalAwards; }

    public void setTotalAwards(int totalAwards) { this.totalAwards = totalAwards; }

    public List<String> getAwards() { return awards; }

    public void setAwards(List<String> awards) { this.awards = awards; }

    @Override
    public int compareTo(NumberOfAwards awards) {
        return Integer.compare(getTotalAwards(), awards.getTotalAwards());
    }

    @Override
    public String toString() {
        return "NumberOfAwards{" +
                "awards=" + awards +
                ", correctAwards=" + correctAwards +
                ", totalAwards=" + totalAwards +
                '}';
    }
}
