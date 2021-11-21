package com.example.tomtep.model;

import java.io.Serializable;

public class LichSuChoAn implements Serializable {
    private String id;
    private SanPham sanPham;
    private float luongChoAn;
    private String thoiGianChoAn;
    private String ketQua;
    private boolean daXoa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public float getLuongChoAn() {
        return luongChoAn;
    }

    public void setLuongChoAn(float luongChoAn) {
        this.luongChoAn = luongChoAn;
    }

    public String getThoiGianChoAn() {
        return thoiGianChoAn;
    }

    public void setThoiGianChoAn(String thoiGianChoAn) {
        this.thoiGianChoAn = thoiGianChoAn;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public boolean isDaXoa() {
        return daXoa;
    }

    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
    }

    public LichSuChoAn() {
    }

    public LichSuChoAn(String id, SanPham sanPham, float luongChoAn, String thoiGianChoAn, String ketQua, boolean daXoa) {
        this.id = id;
        this.sanPham = sanPham;
        this.luongChoAn = luongChoAn;
        this.thoiGianChoAn = thoiGianChoAn;
        this.ketQua = ketQua;
        this.daXoa = daXoa;
    }
}
