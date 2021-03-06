package com.example.coinmonkey;

public class CoinClass {
    private double current_price;
    private long market_cap;
    private double low_24h;
    private double high_24h;
    private double price_change_percentage_24h;
    private long total_volume;

    public CoinClass(double current_price, long market_cap, double low_24h, double high_24h, double price_change_percentage_24h, long total_volume) {
        this.current_price = current_price;
        this.market_cap = market_cap;
        this.low_24h = low_24h;
        this.high_24h = high_24h;
        this.price_change_percentage_24h = price_change_percentage_24h;
        this.total_volume = total_volume;
    }

    public double getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(double current_price) {
        this.current_price = current_price;
    }

    public long getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(long market_cap) {
        this.market_cap = market_cap;
    }

    public double getLow_24h() {
        return low_24h;
    }

    public void setLow_24h(double low_24h) {
        this.low_24h = low_24h;
    }

    public double getHigh_24h() {
        return high_24h;
    }

    public void setHigh_24h(double high_24h) {
        this.high_24h = high_24h;
    }

    public double getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }

    public void setPrice_change_percentage_24h(double price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    public long getTotal_volume() {
        return total_volume;
    }

    public void setTotal_volume(long total_volume) {
        this.total_volume = total_volume;
    }

    @Override
    public String toString() {
        return "CoinClass{" +
                "current_price=" + current_price +
                ", market_cap=" + market_cap +
                ", low_24h=" + low_24h +
                ", high_24h=" + high_24h +
                ", price_change_percentage_24h=" + price_change_percentage_24h +
                ", total_volume=" + total_volume +
                '}';
    }
}

class CoinDescription {
    public Description description;

    public CoinDescription(Description description) {
        this.description = description;
    }
    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CoinDescription{" +
                "description=" + description +
                '}';
    }
}

class Description {
    private String en;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public Description(String en) {
        this.en = en;
    }

    @Override
    public String toString() {
        return "Description{" +
                "en='" + en + '\'' +
                '}';
    }
}
