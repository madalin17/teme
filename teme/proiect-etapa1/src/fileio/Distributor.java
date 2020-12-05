package fileio;

import java.util.ArrayList;
import java.util.List;

public final class Distributor implements Comparable<Distributor> {

    private final int id;

    private final int contractLength;

    private int budget;

    private int infrastructureCost;

    private int productionCost;

    private List<Consumer> consumers;

    private List<Consumer> deniedConsumers;

    private boolean bankrupt;

    public Distributor(final int id, final int contractLength, final int initialBudget,
                       final int initialInfrastructureCost, final int initialProductionCost) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = initialBudget;
        this.infrastructureCost = initialInfrastructureCost;
        this.productionCost = initialProductionCost;
        this.consumers = new ArrayList<>();
        this.deniedConsumers = new ArrayList<>();
        this.bankrupt = false;
    }

    public int getId() {
        return id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public int getBudget() {
        return budget;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public List<Consumer> getDeniedConsumers() {
        return deniedConsumers;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setInfrastructureCost(int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public void setProductionCost(int productionCost) {
        this.productionCost = productionCost;
    }

    public void setConsumers(List<Consumer> consumers) {
        this.consumers = consumers;
    }

    public void setDeniedConsumers(List<Consumer> deniedConsumers) {
        this.deniedConsumers = deniedConsumers;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    @Override
    public String toString() {
        return "Distributor{"
                + "id=" + id
                + ", contractLength=" + contractLength
                + ", budget=" + budget
                + ", infrastructureCost=" + infrastructureCost
                + ", productionCost=" + productionCost
                + ", consumers=" + consumers
                + ", deniedConsumers=" + deniedConsumers
                + '}';
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public int calculateContractCost() {
        int profit = (int) Math.round(Math.floor(0.2 * productionCost));
        if (consumers.size() == 0) {
            return infrastructureCost + productionCost + profit;
        }
        return (int) (Math.round(Math.floor(infrastructureCost / consumers.size()))
                + productionCost + profit);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Distributor o) {
        if (this.calculateContractCost() == o.calculateContractCost()) {
            return Integer.compare(this.getId(), o.getId());
        }
        return Integer.compare(this.calculateContractCost(), o.calculateContractCost());
    }

    public void payed(Consumer consumer) {
        if (consumers.contains(consumer)) {
            int contract = calculateContractCost();
            consumer.setPrice(contract);
            int budget = consumer.getIncome();

            int penalty = (int) Math.round(Math.floor(1.2 * consumer.getOldContract()));
            if (consumer.isPayed() && budget > contract + penalty) {
                consumer.setIncome(budget - contract - penalty);
            } else if (consumer.isPayed() && budget < contract + penalty) {
                deniedConsumers.add(consumer);
                consumers.remove(consumer);
                consumer.setContracted(false);
                consumer.setBankrupt(true);
                consumer.setPrice(0);
                return;
            }

            if (budget > contract) {
                consumer.setIncome(budget - contract);
                this.budget += contract;
            } else {
                consumer.setPayed(false);
                consumer.setOldContract(contract);
            }

            int monthsLeft = consumer.getMonthsLeft();
            consumer.setMonthsLeft(--monthsLeft);

        }
    }

    public void removeConsumer(Consumer consumer) {
        if (consumers.contains(consumer) && consumer.getMonthsLeft() == 0) {
            consumers.remove(consumer);
            consumer.setContracted(false);
            consumer.setPrice(0);
        }
    }

    public void pay() {
        this.budget -= infrastructureCost;
        this.budget -= productionCost * consumers.size();
    }

    public void bankruptcy() {
        bankrupt = (consumers.size() == 0) && (budget < infrastructureCost);
    }

}
