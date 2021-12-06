package com.example.tomtep.model;

import java.io.Serializable;

public class Lake implements Serializable {
    private String id;
    private String accountId;
    private String key;
    private String name;
    private String description;
    private String creationTime;
    private String harvestTime;
    private Diet diet;
    private boolean condition;
    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getHarvestTime() {
        return harvestTime;
    }

    public void setHarvestTime(String harvestTime) {
        this.harvestTime = harvestTime;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Lake() {
    }

    public Lake(String id, String accountId, String key, String name, String description, String creationTime, String harvestTime, Diet diet, boolean condition, boolean deleted) {
        this.id = id;
        this.accountId = accountId;
        this.key = key;
        this.name = name;
        this.description = description;
        this.creationTime = creationTime;
        this.harvestTime = harvestTime;
        this.diet = diet;
        this.condition = condition;
        this.deleted = deleted;
    }
}
