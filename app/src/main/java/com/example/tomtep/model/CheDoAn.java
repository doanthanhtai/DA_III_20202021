package com.example.tomtep.model;

import java.io.Serializable;
import java.util.List;

public class CheDoAn implements Serializable {
    private SanPham sanPhamChoAn;
    private float luongChoAn;
    private List<String> khungGioChoAn;
    private String thoiGianChoAn;
    private boolean trangThai;

    public SanPham getSanPhamChoAn() {
        return sanPhamChoAn;
    }

    public void setSanPhamChoAn(SanPham sanPhamChoAn) {
        this.sanPhamChoAn = sanPhamChoAn;
    }

    public float getLuongChoAn() {
        return luongChoAn;
    }

    public void setLuongChoAn(float luongChoAn) {
        this.luongChoAn = luongChoAn;
    }

    public List<String> getKhungGioChoAn() {
        return khungGioChoAn;
    }

    public void setKhungGioChoAn(List<String> khungGioChoAn) {
        this.khungGioChoAn = khungGioChoAn;
    }

    public String getThoiGianChoAn() {
        return thoiGianChoAn;
    }

    public void setThoiGianChoAn(String thoiGianChoAn) {
        this.thoiGianChoAn = thoiGianChoAn;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public CheDoAn() {
    }

    public CheDoAn(SanPham sanPhamChoAn, float luongChoAn, List<String> khungGioChoAn, String thoiGianChoAn, boolean trangThai) {
        this.sanPhamChoAn = sanPhamChoAn;
        this.luongChoAn = luongChoAn;
        this.khungGioChoAn = khungGioChoAn;
        this.thoiGianChoAn = thoiGianChoAn;
        this.trangThai = trangThai;
    }

}
