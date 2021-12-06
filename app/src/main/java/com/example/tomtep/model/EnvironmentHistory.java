package com.example.tomtep.model;

public class EnvironmentHistory {
    private String id;
    private String lakeId;
    private float pH;
    private float oXy;
    private int salinity;
    private String updateTime;

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

    public float getpH() {
        return pH;
    }

    public void setpH(float pH) {
        this.pH = pH;
    }

    public float getoXy() {
        return oXy;
    }

    public void setoXy(float oXy) {
        this.oXy = oXy;
    }

    public int getSalinity() {
        return salinity;
    }

    public void setSalinity(int salinity) {
        this.salinity = salinity;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public EnvironmentHistory() {
    }

    public EnvironmentHistory(String id, String lakeId, float pH, float oXy, int salinity, String updateTime) {
        this.id = id;
        this.lakeId = lakeId;
        this.pH = pH;
        this.oXy = oXy;
        this.salinity = salinity;
        this.updateTime = updateTime;
    }
}
