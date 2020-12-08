package phase.one;

import energy.Consumer;
import energy.Distributor;
import fileio.CostChange;
import fileio.MonthlyUpdate;
import utils.ConsumerUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class MonthUpdate {

    private MonthUpdate() { }

    /**
     * MONTH ZERO
     * All initial consumers are paid and choose the distributor with the cheapest contract
     * This distributor collects the payment from all his consumers and all distributors pay
     * @param consumers list of initial consumers
     * @param distributors list of distributors
     */
    public static void initialUpdate(final List<Consumer> consumers,
                                     final List<Distributor> distributors) {
        consumers.forEach(Consumer::bePaid);
        Distributor d = distributors.stream().sorted().collect(Collectors.toList()).get(0);
        consumers.forEach(c -> d.getConsumers().put(c, new ConsumerUtils(d.getContractLength(),
                d.calculateContractCost())));

        MonthUpdate.payments(distributors);
    }

    /**
     * For each consumer we verify if he's already contracted or bankrupt,
     * if not, we sign the cheapest contract for him
     * @param consumers list of current consumers
     * @param distributors list of distributors
     */
    public static void newContracts(final List<Consumer> consumers,
                                    final List<Distributor> distributors) {
        consumers.forEach(c -> {
            boolean contracted = false;
            for (Distributor distributor : distributors) {
                if (distributor.getConsumers().containsKey(c) && !distributor.isBankrupt()) {
                    contracted = true;
                    break;
                }
            }
            if (!contracted && !c.isBankrupt()) {
                List<Distributor> availableDistributors = distributors.stream()
                        .filter(distributor -> !distributor.getDeniedConsumers().containsKey(c)
                                && !distributor.isBankrupt() && distributor.getBudget() != 0)
                        .sorted()
                        .collect(Collectors.toList());
                if (availableDistributors.size() != 0) {
                    Distributor d = availableDistributors.get(0);
                    d.getConsumers().put(c, new ConsumerUtils(d.getContractLength(),
                            d.calculateContractCost()));
                }

            }
        });
    }

    /**
     * MONTH i UPDATE(i less than numberOfTurns)
     * All distributors update their costs and the list of consumers is upgraded
     * All consumers that are not bankrupt are paid
     * For each consumer we verify if he's already contracted or bankrupt,
     * if not, we sign the cheapest contract for him
     * @param consumers list of current consumers
     * @param distributors list of distributors
     * @param monthlyUpdate month i update with new consumers and cost changes for distributors
     */
    public static void update(final List<Consumer> consumers, final List<Distributor> distributors,
                              final MonthlyUpdate monthlyUpdate) {
        List<Consumer> newConsumers = monthlyUpdate.getNewConsumers();
        consumers.addAll(consumers.size(), newConsumers);

        List<CostChange> costChanges = monthlyUpdate.getCostChanges();
        costChanges.forEach(cost -> {
            distributors.get(cost.getId()).setInfrastructureCost(cost.getInfrastructureCost());
            distributors.get(cost.getId()).setProductionCost(cost.getProductionCost());
        });

        consumers.stream()
                .filter(c -> !c.isBankrupt())
                .forEach(Consumer::bePaid);
        MonthUpdate.newContracts(consumers, distributors);
    }

    /**
     * All distributors are paid by their consumer-clients and pay their monthly payments
     * @param distributors list of distributors
     */
    public static void payments(final List<Distributor> distributors) {
        distributors.forEach(Distributor::bePaid);
        distributors.stream()
                .filter(d -> d.getBudget() >= 0)
                .forEach(Distributor::pay);
    }

    /**
     * Function removes all consumers for a distributor with 0 months to pay left
     * at the beginning of the month and removes all denied consumers from distributors'
     * current consumers list
     * @param distributors list of distributors
     * @return a list of integers and element i in the list corresponds to distributor with id i,
     * being the number of non-bankrupt consumers at the end of the round
     */
    public static List<Integer> removeConsumer(final List<Distributor> distributors) {
        List<Integer> sizeContractedConsumers = new ArrayList<>();
        distributors.forEach(d -> {
            int size = 0;
            for (Map.Entry<Consumer, ConsumerUtils> entry : d.getConsumers().entrySet()) {
                if (!entry.getKey().isBankrupt()) {
                    size++;
                }
            }
            sizeContractedConsumers.add(size);
        });
        distributors.forEach(d -> {
            Map<Consumer, ConsumerUtils> map = new LinkedHashMap<>();
            d.getConsumers().entrySet()
                    .stream()
                    .filter(consumer -> consumer.getValue().getMonthsLeft() == 0)
                    .forEach(consumer -> map.put(consumer.getKey(), consumer.getValue()));
            map.forEach((key, value) -> d.getConsumers().remove(key));
            if (d.getDeniedConsumers().size() != 0) {
                d.getDeniedConsumers().entrySet()
                        .stream()
                        .filter(denied -> d.getConsumers().containsKey(denied.getKey()))
                        .forEach(denied -> d.getConsumers().remove(denied.getKey()));
            }
        });
        return sizeContractedConsumers;
    }

    /**
     * Function removes all bankrupt consumers
     * from distributors' current consumers list at the end of a month
     * @param consumers list of current consumers
     * @param distributors list of distributors
     */
    public static void removeBankruptConsumer(final List<Consumer> consumers,
                                              final List<Distributor> distributors) {
        distributors.forEach(d -> consumers.stream()
                .filter(Consumer::isBankrupt)
                .forEach(c -> d.getConsumers().remove(c)));
    }

    /**
     * Function declares a distributor as being bankrupt at the eng of the month
     * @param distributors list of distributors
     */
    public static void bankrupt(final List<Distributor> distributors) {
        distributors.stream()
                .filter(d -> d.getBudget() < 0)
                .forEach(d -> d.setBankrupt(true));
    }
}
