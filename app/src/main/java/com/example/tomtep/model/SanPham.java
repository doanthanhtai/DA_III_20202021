package com.example.tomtep.model;

import java.io.Serializable;
import java.util.List;

public class SanPham implements Serializable {
    private String id;
    private String maSP;
    private String tenSP;
    private String tenNCC;
    private float giaNhap;
    private String donViDung;
    private float soLuong;
    private List<LichSuNhapHang> lichSuNhapHangs;
    private boolean daXoa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public float getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(float giaNhap) {
        this.giaNhap = giaNhap;
    }

    public String getDonViDung() {
        return donViDung;
    }

    public void setDonViDung(String donViDung) {
        this.donViDung = donViDung;
    }

    public float getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(float soLuong) {
        this.soLuong = soLuong;
    }

    public List<LichSuNhapHang> getLichSuNhapHangs() {
        return lichSuNhapHangs;
    }

    public void setLichSuNhapHangs(List<LichSuNhapHang> lichSuNhapHangs) {
        this.lichSuNhapHangs = lichSuNhapHangs;
    }

    public boolean isDaXoa() {
        return daXoa;
    }

    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
    }

    public SanPham() {
    }

    public SanPham(String id, String maSP, String tenSP, String tenNCC, float giaNhap, String donViDung, int soLuong, List<LichSuNhapHang> lichSuNhapHangs, boolean daXoa) {
        this.id = id;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.tenNCC = tenNCC;
        this.giaNhap = giaNhap;
        this.donViDung = donViDung;
        this.soLuong = soLuong;
        this.lichSuNhapHangs = lichSuNhapHangs;
        this.daXoa = daXoa;
    }
}
