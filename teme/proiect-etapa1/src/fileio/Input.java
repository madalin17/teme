package fileio;

import java.util.List;

public final class Input {

    private final int numberOfTurns;

    private final List<Consumer> consumers;

    private final List<Distributor> distributors;

    private final List<MonthlyUpdate> monthlyUpdates;

    public Input(final int numberOfTurns, final List<Consumer> consumers,
                 final List<Distributor> distributors, final List<MonthlyUpdate> monthlyUpdates) {
        this.numberOfTurns = numberOfTurns;
        this.consumers = consumers;
        this.distributors = distributors;
        this.monthlyUpdates = monthlyUpdates;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public List<MonthlyUpdate> getMonthlyUpdates() {
        return monthlyUpdates;
    }
}
