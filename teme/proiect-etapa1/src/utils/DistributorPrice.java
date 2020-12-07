package utils;

import constants.Constants;
import energy.Consumer;

import java.util.LinkedHashMap;
import java.util.Map;

public class DistributorPrice implements Comparable<DistributorPrice> {

    private final int id;

    private int infrastructureCost;

    private int productionCost;

    private Map<Consumer, ConsumerUtils> consumers;

    private int size;

    public DistributorPrice(final int id, final int infrastructureCost, final int productionCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = productionCost;
        this.consumers = new LinkedHashMap<>();
        this.size = 0;
    }

    public DistributorPrice() {
        new DistributorPrice(0, 0, 0);
        this.id = 0;
    }

    /**
     * @return id of distributor
     */
    public int getId() {
        return id;
    }

    /**
     * @return current infrastructure cost
     */
    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    /**
     * @param infrastructureCost new infrastructure cost for current month
     */
    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    /**
     * @return current production cost
     */
    public int getProductionCost() {
        return productionCost;
    }

    /**
     * @param productionCost new infrastructure cost for current month
     */
    public void setProductionCost(final int productionCost) {
        this.productionCost = productionCost;
    }

    /**
     * @return map of current consumers for this distributor
     */
    public Map<Consumer, ConsumerUtils> getConsumers() {
        return consumers;
    }

    /**
     * @param consumers set map of consumers for this distributor
     */
    public void setConsumers(final Map<Consumer, ConsumerUtils> consumers) {
        this.consumers = consumers;
    }

    /**
     * @return size of all non-bankrupt consumers the distributor has at the end of the month
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size sets new size of all non-bankrupt
     *             consumers the distributor has at the end of the month
     */
    public void setSize(final int size) {
        this.size = size;
    }

    /**
     * @return current contract cost at the beginning of the month
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public int calculateContractCost() {
        int profit = (int) Math.round(Math.floor(Constants.PENALTY * productionCost));
        if (size == 0) {
            return infrastructureCost + productionCost + profit;
        }
        return (int) (Math.round(Math.floor(infrastructureCost / size))
                + productionCost + profit);
    }

    /**
     * @param o DistributorPrice object to be compared with this
     * @return comparator for distributors
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(final DistributorPrice o) {
        if (this.calculateContractCost()
                == o.calculateContractCost()) {
            return Integer.compare(this.getId(), o.getId());
        }
        return Integer.compare(this.calculateContractCost(),
                o.calculateContractCost());
    }
}
