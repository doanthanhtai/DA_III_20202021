package com.example.tomtep.model;

import java.io.Serializable;

public class LichSuMoiTruong implements Serializable {
    private String id;
    private float pH;
    private float oXy;
    private int doMan;
    private String thoiGian;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getDoMan() {
        return doMan;
    }

    public void setDoMan(int doMan) {
        this.doMan = doMan;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public LichSuMoiTruong() {
    }

    public LichSuMoiTruong(String id, float pH, float oXy, int doMan, String thoiGian) {
        this.id = id;
        this.pH = pH;
        this.oXy = oXy;
        this.doMan = doMan;
        this.thoiGian = thoiGian;
    }
}
