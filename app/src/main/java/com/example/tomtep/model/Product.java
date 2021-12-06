package com.example.tomtep.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String accountId;
    private String key;
    private String name;
    private String supplier;
    private float importPrice;
    private String measure;
    private float amount;
    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return accountId;
    }

    public void setEmail(String email) {
        this.accountId = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public float getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(float importPrice) {
        this.importPrice = importPrice;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Product() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Product(String id, String accountId, String key, String name, String supplier, float importPrice, String measure, float amount, boolean deleted) {
        this.id = id;
        this.accountId = accountId;
        this.key = key;
        this.name = name;
        this.supplier = supplier;
        this.importPrice = importPrice;
        this.measure = measure;
        this.amount = amount;
        this.deleted = deleted;
    }
}
