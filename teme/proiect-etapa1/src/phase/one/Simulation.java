package phase.one;

import energy.Consumer;
import energy.Distributor;
import fileio.MonthlyUpdate;

import java.util.List;

public final class Simulation {

    private Simulation() { }

    /**
     * Function creates the simulation with the data in the database
     * @param consumers list of consumers
     * @param distributors list of distributors
     * @param monthlyUpdates list of monthly updates
     * @param turns number of months
     */
    public static void simulation(final List<Consumer> consumers,
                                  final List<Distributor> distributors,
                                  final List<MonthlyUpdate> monthlyUpdates, final Integer turns) {
        MonthUpdate.initialUpdate(consumers, distributors);
        for (int i = 0; i < turns; i++) {
            List<Integer> sizeContractedConsumers = MonthUpdate.removeConsumer(distributors);
            for (int j = 0; j < sizeContractedConsumers.size(); j++) {
                distributors.get(j).setSize(sizeContractedConsumers.get(j));
            }
            MonthUpdate.update(consumers, distributors, monthlyUpdates.get(i));
            MonthUpdate.payments(distributors);
            MonthUpdate.removeBankruptConsumer(consumers, distributors);
            MonthUpdate.bankrupt(distributors);
        }
    }
}
