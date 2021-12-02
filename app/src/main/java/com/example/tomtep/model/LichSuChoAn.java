package com.example.tomtep.model;

import java.io.Serializable;

public class LichSuChoAn implements Serializable {
    private boolean tonTai;
    private String ketQua;

    public LichSuChoAn(boolean tonTai, String ketQua) {
        this.tonTai = tonTai;
        this.ketQua = ketQua;
    }

    public LichSuChoAn() {
    }

    public boolean isTonTai() {
        return tonTai;
    }

    public void setTonTai(boolean tonTai) {
        this.tonTai = tonTai;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }
}