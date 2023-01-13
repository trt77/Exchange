public class CurrencyPair {

    private String currency1;
    private String currency2;
    private double rate;
    private double sum;

    public CurrencyPair(String currency1, String currency2, double rate, double sum) {
        this.currency1 = currency1;
        this.currency2 = currency2;
        this.rate = rate;
        this.sum = sum;
    }

    public String getCurrency1() {
        return currency1;
    }

    public String getCurrency2() {
        return currency2;
    }

    public double getRate() {
        return rate;
    }

    public double getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "" + currency1 + "/" + currency2 + ": " + rate + " SUMA: " + sum + "";
    }
}
