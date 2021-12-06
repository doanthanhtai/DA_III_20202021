package com.example.tomtep.model;

public class FeedingHistory {
    private String id;
    private String lakeId;
    private String productId;
    private String productHistoryId;
    private float amount;
    private String time;
    private String result;
    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProductHistoryId() {
        return productHistoryId;
    }

    public void setProductHistoryId(String productHistoryId) {
        this.productHistoryId = productHistoryId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public FeedingHistory() {
    }

    public FeedingHistory(String id, String lakeId, String productId, String productHistoryId, float amount, String time, String result, boolean deleted) {
        this.id = id;
        this.lakeId = lakeId;
        this.productId = productId;
        this.productHistoryId = productHistoryId;
        this.amount = amount;
        this.time = time;
        this.result = result;
        this.deleted = deleted;
    }
}
