
package com.trc.massage.binding;

public class Price {

    private Double amount;
    private String currency;

    public Price() {
    }

    public Price(Double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
