package utils;

public final class ConsumerUtils {

    private int monthsLeft;

    private boolean payed;

    private int price;

    public ConsumerUtils(final int monthsLeft, final boolean payed,
                         final int price) {
        this.monthsLeft = monthsLeft;
        this.payed = payed;
        this.price = price;
    }

    public ConsumerUtils() {
        new ConsumerUtils(0, false, 0);
    }

    public ConsumerUtils(final int monthsLeft, final int price) {
        this.monthsLeft = monthsLeft;
        this.payed = true;
        this.price = price;
    }


    public int getMonthsLeft() {
        return monthsLeft;
    }

    public void setMonthsLeft(final int monthsLeft) {
        this.monthsLeft = monthsLeft;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(final boolean payed) {
        this.payed = payed;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

}
