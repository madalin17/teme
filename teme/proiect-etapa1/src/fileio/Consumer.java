package fileio;

public class Consumer {

    private final int id;

    private final int initialIncome;

    private final int monthlyIncome;

    public Consumer(int id, int initialIncome, int monthlyIncome) {
        this.id = id;
        this.initialIncome = initialIncome;
        this.monthlyIncome = monthlyIncome;
    }

    public int getId() {
        return id;
    }

    public int getInitialIncome() {
        return initialIncome;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    @Override
    public String toString() {
        return "Consumer{" + "id=" + id + ", initialIncome=" + initialIncome
                + ", monthlyIncome=" + monthlyIncome + '}';
    }
}
