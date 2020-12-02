package fileio;

public final class CostChange {

    private final int id;

    private final int infrastructureCost;

    private final int productionCost;

    public CostChange(final int id, final int infrastructureCost, final int productionCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = productionCost;
    }

    public int getId() {
        return id;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    @Override
    public String toString() {
        return "CostChange{" + "id=" + id + ", infrastructureCost=" + infrastructureCost
                + ", productionCost=" + productionCost + '}';
    }
}
