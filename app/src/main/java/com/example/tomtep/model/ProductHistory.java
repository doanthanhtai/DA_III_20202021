package com.example.tomtep.model;

public class ProductHistory {
    private String id;
    private String lakeId;
    private String productId;
    private float amount;
    private String useTime;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ProductHistory(String id, String lakeId, String productId, float amount, String useTime, boolean deleted) {
        this.id = id;
        this.lakeId = lakeId;
        this.productId = productId;
        this.amount = amount;
        this.useTime = useTime;
        this.deleted = deleted;
    }

    public ProductHistory() {
    }
}
