package com.example.tomtep.model;

public class ImportHistory {
    private String id;
    private String productId;
    private float amount;
    private String importTime;
    private String updateTime;
    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ImportHistory() {
    }

    public ImportHistory(String id, String productId, float amount, String importTime, String updateTime, boolean deleted) {
        this.id = id;
        this.productId = productId;
        this.amount = amount;
        this.importTime = importTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
    }
}
