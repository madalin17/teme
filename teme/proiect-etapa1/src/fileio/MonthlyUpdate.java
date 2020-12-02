package fileio;

import java.util.List;

public class MonthlyUpdate {

    public final List<Consumer> newConsumers;

    public final List<CostChange> costChanges;

    public MonthlyUpdate(List<Consumer> newConsumers, List<CostChange> costChanges) {
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
