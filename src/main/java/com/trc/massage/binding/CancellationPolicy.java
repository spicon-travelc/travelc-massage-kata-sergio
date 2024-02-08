
package com.trc.massage.binding;

import java.time.LocalDate;

public class CancellationPolicy {

    private LocalDate date;
    private Price price;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Double getAmount() {
        return price.getAmount();
    }
}
