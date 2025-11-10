package model;

import java.io.Serializable;

public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;

    private String category;
    private double limit;

    public Budget(String category, double limit) {
        this.category = category;
        this.limit = limit;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }
}

