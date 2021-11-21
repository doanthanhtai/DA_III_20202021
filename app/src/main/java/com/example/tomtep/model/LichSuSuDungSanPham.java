package com.example.tomtep.model;

import java.io.Serializable;
import java.util.List;

public class LichSuSuDungSanPham implements Serializable {
    private String id;
    private SanPham sanPham;
    private int soLuong;
    private String thoiGianDung;
    private String thoiGianCapNhat;
    private List<LichSuChoAn> lichSuChoAns;
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getThoiGianDung() {
        return thoiGianDung;
    }

    public void setThoiGianDung(String thoiGianDung) {
        this.thoiGianDung = thoiGianDung;
    }

    public String getThoiGianCapNhat() {
        return thoiGianCapNhat;
    }

    public void setThoiGianCapNhat(String thoiGianCapNhat) {
        this.thoiGianCapNhat = thoiGianCapNhat;
    }

    public List<LichSuChoAn> getLichSuChoAns() {
        return lichSuChoAns;
    }

    public void setLichSuChoAns(List<LichSuChoAn> lichSuChoAns) {
        this.lichSuChoAns = lichSuChoAns;
    }

    public boolean isDaXoa() {
        return daXoa;
    }

    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
    }

    public LichSuSuDungSanPham() {
    }

    public LichSuSuDungSanPham(String id, SanPham sanPham, int soLuong, String thoiGianDung, String thoiGianCapNhat, List<LichSuChoAn> lichSuChoAns, boolean daXoa) {
        this.id = id;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.thoiGianDung = thoiGianDung;
        this.thoiGianCapNhat = thoiGianCapNhat;
        this.lichSuChoAns = lichSuChoAns;
        this.daXoa = daXoa;
    }
}
