import fileio.*;

import java.util.List;

public final class Main {

    private Main() { }

    /**
     * Main method
     * @param args not used
     */
    public static void main(final String[] args) {
        //String inputPath = args[0];
        //String outputPath = args[1];
        String inputPath = "checker/resources/in/basic_1.json";
        String outputPath = "./output";

        InputLoader inputLoader = new InputLoader(inputPath);
        Input input = inputLoader.readData();

        List<Consumer> consumers = input.getConsumers();
        List<Distributor> distributors = input.getDistributors();

        for (int i = 0; i < input.getNumberOfTurns(); i++) {
            List<Consumer> newConsumers = input.getMonthlyUpdates().get(i).getNewConsumers();
            List<CostChange> costChanges = input.getMonthlyUpdates().get(i).getCostChanges();

            costChanges.forEach(cost -> {
                Distributor d = distributors.get(cost.getId());
                d.setInfrastructureCost(cost.getInfrastructureCost());
                d.setProductionCost(cost.getProductionCost());
            });

            distributors.forEach(d -> consumers.forEach(d::removeConsumer));

            consumers.addAll(consumers.size(), newConsumers);
            System.out.println(distributors);
            consumers.forEach(c -> {
                int monthlyIncome = c.getMonthlyIncome();
                int budget = c.getIncome();
                c.setIncome(budget + monthlyIncome);
                if (c.getMonthsLeft() == 0) {
                    c.setContracted(false);
                }
                if (!c.isContracted()) {
                    distributors.stream().sorted().forEach(d -> {
                        if (!c.isContracted()
                                && (d.getDeniedConsumers() == null
                                || !d.getDeniedConsumers().contains(c))) {
                            c.setContracted(true);
                            c.setMonthsLeft(d.getContractLength());

                            List<Consumer> prevConsumers = d.getConsumers();
                            prevConsumers.add(c);
                            d.setConsumers(prevConsumers);
                        }
                    });
                    if (!c.isContracted()) {
                        c.setBankrupt(true);
                    }
                }
            });
            distributors.forEach(d -> consumers.forEach(d::payed));
            System.out.println(distributors);
            distributors.forEach(Distributor::bankruptcy);
            distributors.stream().filter(d -> !d.isBankrupt()).forEach(Distributor::pay);
            System.out.println(distributors);
            System.out.println();
        }

        OutputLoader outputLoader = new OutputLoader(outputPath, consumers, distributors);
        outputLoader.writeFile();
    }
}
