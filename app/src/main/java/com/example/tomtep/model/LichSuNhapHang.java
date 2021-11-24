package com.example.tomtep.model;

import java.io.Serializable;

public class LichSuNhapHang implements Serializable {
    private String id;
    private int soLuong;
    private String thoiGianNhap;
    private String thoiGianCapNhat;
    private boolean daXoa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getThoiGianNhap() {
        return thoiGianNhap;
    }

    public void setThoiGianNhap(String thoiGianNhap) {
        this.thoiGianNhap = thoiGianNhap;
    }

    public String getThoiGianCapNhat() {
        return thoiGianCapNhat;
    }

    public void setThoiGianCapNhat(String thoiGianCapNhat) {
        this.thoiGianCapNhat = thoiGianCapNhat;
    }

    public boolean isDaXoa() {
        return daXoa;
    }

    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
    }

    public LichSuNhapHang() {
    }

    public LichSuNhapHang(String id, int soLuong, String thoiGianNhap, String thoiGianCapNhat, boolean daXoa) {
        this.id = id;
        this.soLuong = soLuong;
        this.thoiGianNhap = thoiGianNhap;
        this.thoiGianCapNhat = thoiGianCapNhat;
        this.daXoa = daXoa;
    }
}
