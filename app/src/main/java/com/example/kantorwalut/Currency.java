package com.example.kantorwalut;

import java.math.BigDecimal;

public class Currency {
    private int id;
    private String name;
    private BigDecimal fromValue;
    private BigDecimal toValue;

    public Currency(int id, String name, BigDecimal fromValue, BigDecimal toValue) {
        this.id = id;
        this.name = name;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFromValue() {
        return fromValue;
    }

    public void setFromValue(BigDecimal fromValue) {
        this.fromValue = fromValue;
    }

    public BigDecimal getToValue() {
        return toValue;
    }

    public void setToValue(BigDecimal toValue) {
        this.toValue = toValue;
    }
}
