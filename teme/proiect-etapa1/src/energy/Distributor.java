package energy;

import constants.Constants;
import utils.ConsumerUtils;
import utils.DistributorPrice;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Distributor extends DistributorPrice implements EnergyInstance {

    private final int contractLength;

    private int budget;

    private Map<Consumer, ConsumerUtils> consumers;

    private Map<Consumer, ConsumerUtils> deniedConsumers;

    private boolean bankrupt;


    public Distributor(final int id, final int contractLength, final int budget,
                       final int infrastructureCost, final int productionCost) {
        super(id, infrastructureCost, productionCost);
        this.contractLength = contractLength;
        this.budget = budget;
        this.consumers = new LinkedHashMap<>();
        this.deniedConsumers = new LinkedHashMap<>();
        this.bankrupt = false;
    }

    public int getId() {
        return super.getId();
    }

    public int getContractLength() {
        return contractLength;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    public int getInfrastructureCost() {
        return super.getInfrastructureCost();
    }

    /**
     * Function sets a new infrastructure cost after a month of cost changes for this distributor
     * @param infrastructureCost new infrastructure for distributor
     */
    public void setInfrastructureCost(final int infrastructureCost) {
        super.setInfrastructureCost(infrastructureCost);
    }

    public int getProductionCost() {
        return super.getProductionCost();
    }

    /**
     * Function sets a new production cost after a month of cost changes for this distributor
     * @param productionCost new production for distributor
     */
    public void setProductionCost(final int productionCost) {
        super.setProductionCost(productionCost);
    }

    public Map<Consumer, ConsumerUtils> getConsumers() {
        return consumers;
    }

    public void setConsumers(final Map<Consumer, ConsumerUtils> consumers) {
        this.consumers = consumers;
    }

    public Map<Consumer, ConsumerUtils> getDeniedConsumers() {
        return deniedConsumers;
    }

    public void setDeniedConsumers(final Map<Consumer, ConsumerUtils> deniedConsumers) {
        this.deniedConsumers = deniedConsumers;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    @Override
    public void pay() {
        this.budget -= super.getInfrastructureCost();
        this.budget -= super.getProductionCost() * consumers.size();
    }

    @Override
    public void bePaid() {
        consumers.forEach((key, value) -> {
            int income = key.getIncome();
            int contract = value.getPrice();
            int debt = key.getDebt();
            Distributor debtDistributor = key.getDebtDistributor();
            int penalty = (int) Math.round(Math.floor(Constants.PENALTY * contract));
            int oldPenalty = (int) Math.round(Math.floor(Constants.PENALTY * debt));
            int monthsLeft = value.getMonthsLeft();

            if (!value.isPayed() && monthsLeft >= 1) {
                if (income >= 2 * contract + penalty) {
                    key.setIncome(income - 2 * contract - penalty);
                    this.budget += 2 * contract + penalty;
                    value.setMonthsLeft(--monthsLeft);
                } else {
                    key.setBankrupt(true);
                    deniedConsumers.put(key, value);
                }
            } else if (debt != 0) {
                if (income >= contract + oldPenalty + debt) {
                    key.setIncome(income - contract - oldPenalty - debt);
                    int newBudget = debtDistributor.getBudget();
                    debtDistributor.setBudget(newBudget + debt + oldPenalty);
                    this.budget += contract;
                    value.setMonthsLeft(--monthsLeft);
                } else {
                    key.setBankrupt(true);
                    debtDistributor.getDeniedConsumers().put(key, value);
                    deniedConsumers.put(key, value);
                }
            } else if (income >= contract) {
                key.setIncome(income - contract);
                value.setMonthsLeft(--monthsLeft);
                this.budget += contract;
            } else if (monthsLeft == 1) {
                key.setDebt(contract);
                key.setDebtDistributor(this);
                value.setMonthsLeft(--monthsLeft);
            } else {
                value.setMonthsLeft(--monthsLeft);
                value.setPayed(false);
            }
        });
    }
}
