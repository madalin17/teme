package fileio;

public final class Consumer {

    private final int id;

    private int income;

    private final int monthlyIncome;

    private boolean contracted;

    private int monthsLeft;

    private boolean payed;

    private int oldContract;

    private boolean bankrupt;

    private int price;

    public Consumer(final int id, final int initialIncome, final int monthlyIncome) {
        this.id = id;
        this.income = initialIncome;
        this.monthlyIncome = monthlyIncome;
        this.contracted = false;
        this.monthsLeft = 0;
        this.payed = true;
        this.oldContract = 0;
        this.bankrupt = false;
        this.price = 0;
    }

    public int getId() {
        return id;
    }

    public int getIncome() {
        return income;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public boolean isContracted() {
        return contracted;
    }

    public int getMonthsLeft() {
        return monthsLeft;
    }

    public boolean isPayed() {
        return !payed;
    }

    public int getOldContract() {
        return oldContract;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public int getPrice() {
        return price;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void setContracted(boolean contracted) {
        this.contracted = contracted;
    }

    public void setMonthsLeft(int monthsLeft) {
        this.monthsLeft = monthsLeft;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public void setOldContract(int oldContract) {
        this.oldContract = oldContract;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Consumer{"
                + "id=" + id
                + ", income=" + income
                + ", monthlyIncome=" + monthlyIncome
                + ", contracted=" + contracted
                + ", monthsLeft=" + monthsLeft
                + ", payed=" + payed
                + ", oldContract=" + oldContract
                + ", bankrupt=" + bankrupt
                + ", price=" + price
                + '}';
    }
}
