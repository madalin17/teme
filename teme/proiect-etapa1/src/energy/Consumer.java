package energy;

public final class Consumer implements EnergyInstance {

    private final int id;

    private int income;

    private final int monthlyIncome;

    private boolean bankrupt;

    private int debt;

    private Distributor debtDistributor;

    public Consumer(final int id, final int income, final int monthlyIncome) {
        this.id = id;
        this.income = income;
        this.monthlyIncome = monthlyIncome;
        this.bankrupt = false;
        this.debt = 0;
        this.debtDistributor = null;
    }

    public int getId() {
        return id;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(final int income) {
        this.income = income;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(final int debt) {
        this.debt = debt;
    }

    public Distributor getDebtDistributor() {
        return debtDistributor;
    }

    public void setDebtDistributor(final Distributor debtDistributor) {
        this.debtDistributor = debtDistributor;
    }

    /**
     * pay operation for a consumer it is done in distributor's bePaid
     */
    @Override
    public void pay() {

    }

    @Override
    public void bePaid() {
        income += monthlyIncome;
    }
}
