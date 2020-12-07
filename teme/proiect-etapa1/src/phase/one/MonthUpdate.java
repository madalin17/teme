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

    public static void initialUpdate(final List<Consumer> consumers,
                                     final List<Distributor> distributors) {
        consumers.forEach(Consumer::bePaid);
        Distributor d = distributors.stream().sorted().collect(Collectors.toList()).get(0);
        consumers.forEach(c -> d.getConsumers().put(c, new ConsumerUtils(d.getContractLength(),
                d.calculateContractCost())));

        distributors.forEach(Distributor::bePaid);
        distributors.forEach(Distributor::pay);
    }

    public static void update(final List<Consumer> consumers, final List<Distributor> distributors,
                              final MonthlyUpdate monthlyUpdate) {
        List<Consumer> newConsumers = monthlyUpdate.getNewConsumers();
        List<CostChange> costChanges = monthlyUpdate.getCostChanges();
        consumers.addAll(consumers.size(), newConsumers);

        costChanges.forEach(cost -> {
            distributors.get(cost.getId()).setInfrastructureCost(cost.getInfrastructureCost());
            distributors.get(cost.getId()).setProductionCost(cost.getProductionCost());
        });

        consumers.stream()
                .filter(c -> !c.isBankrupt())
                .forEach(Consumer::bePaid);
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
                    c.setBankrupt(false);
                    Distributor d = availableDistributors.get(0);
                    d.getConsumers().put(c, new ConsumerUtils(d.getContractLength(),
                            d.calculateContractCost()));
                }

            }
        });
    }

    public static void payments(final List<Distributor> distributors) {
        distributors.forEach(Distributor::bePaid);
        distributors.stream()
                .filter(d -> d.getBudget() >= 0)
                .forEach(Distributor::pay);
    }

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

    public static void removeBankruptConsumer(final List<Consumer> consumers,
                                              final List<Distributor> distributors) {
        distributors.forEach(d -> consumers.stream()
                .filter(Consumer::isBankrupt)
                .forEach(c -> d.getConsumers().remove(c)));
    }

    public static void bankrupt(final List<Distributor> distributors) {
        distributors.stream()
                .filter(d -> d.getBudget() < 0)
                .forEach(d -> d.setBankrupt(true));
    }
}
