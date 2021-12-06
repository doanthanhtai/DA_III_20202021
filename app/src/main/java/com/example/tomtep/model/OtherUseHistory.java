package com.example.tomtep.model;

public class OtherUseHistory {
    private String id;
    private String lakeId;
    private String name;
    private float cost;
    private String description;
    private String useTime;
    private String updateTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
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

    public OtherUseHistory() {
    }

    public OtherUseHistory(String id, String lakeId, String name, float cost, String description, String useTime, String updateTime, boolean deleted) {
        this.id = id;
        this.lakeId = lakeId;
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.useTime = useTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
    }
}
