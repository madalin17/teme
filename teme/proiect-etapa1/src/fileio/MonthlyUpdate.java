package fileio;

import java.util.List;

public final class MonthlyUpdate {

    private final List<Consumer> newConsumers;

    private final List<CostChange> costChanges;

    public MonthlyUpdate(final List<Consumer> newConsumers, final List<CostChange> costChanges) {
        this.newConsumers = newConsumers;
        this.costChanges = costChanges;
    }

    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    public List<CostChange> getCostChanges() {
        return costChanges;
    }

    @Override
    public String toString() {
        return "MonthlyUpdate{" + "newConsumers=" + newConsumers
                + ", costChanges=" + costChanges + '}';
    }
}
