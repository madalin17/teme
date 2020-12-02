package fileio;

public final class Distributor {

    private final int id;

    private final int contractLength;

    private final int initialBudget;

    private final int initialInfrastructureCost;

    private final int initialProductionCost;

    public Distributor(final int id, final int contractLength, final int initialBudget,
                       final int initialInfrastructureCost, final int initialProductionCost) {
        this.id = id;
        this.contractLength = contractLength;
        this.initialBudget = initialBudget;
        this.initialInfrastructureCost = initialInfrastructureCost;
        this.initialProductionCost = initialProductionCost;
    }

    public int getId() {
        return id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public int getInitialInfrastructureCost() {
        return initialInfrastructureCost;
    }

    public int getInitialProductionCost() {
        return initialProductionCost;
    }

    @Override
    public String toString() {
        return "Distributor{" + "id=" + id + ", contractLength=" + contractLength
                + ", initialBudget=" + initialBudget
                + ", initialInfrastructureCost=" + initialInfrastructureCost
                + ", initialProductionCost=" + initialProductionCost + '}';
    }
}
