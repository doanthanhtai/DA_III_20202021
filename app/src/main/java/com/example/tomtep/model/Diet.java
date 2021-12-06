package com.example.tomtep.model;

import java.io.Serializable;
import java.util.List;

public class Diet implements Serializable {
    private String lakeId;
    private String productId;
    private String productName;
    private float amount;
    private List<String> frame;
    private int time;
    private boolean condition;

    public Diet() {
    }

    public String getLakeId() {
        return lakeId;
    }

    public void setLakeId(String lakeId) {
        this.lakeId = lakeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public List<String> getFrame() {
        return frame;
    }

    public void setFrame(List<String> frame) {
        this.frame = frame;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public Diet(String lakeId, String productId, String productName, float amount, List<String> frame, int time, boolean condition) {
        this.lakeId = lakeId;
        this.productId = productId;
        this.productName = productName;
        this.amount = amount;
        this.frame = frame;
        this.time = time;
        this.condition = condition;
    }
}