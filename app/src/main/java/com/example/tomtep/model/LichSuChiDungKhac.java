package com.example.tomtep.model;

import java.io.Serializable;

public class LichSuChiDungKhac implements Serializable {
    private String id;
    private String tenChiDung;
    private float phi;
    private String moTa;
    private String thoiGianChiDung;
    private String thoiGianCapNhat;
    private boolean daXoa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenChiDung() {
        return tenChiDung;
    }

    public void setTenChiDung(String tenChiDung) {
        this.tenChiDung = tenChiDung;
    }

    public float getPhi() {
        return phi;
    }

    public void setPhi(float phi) {
        this.phi = phi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getThoiGianChiDung() {
        return thoiGianChiDung;
    }

    public void setThoiGianChiDung(String thoiGianChiDung) {
        this.thoiGianChiDung = thoiGianChiDung;
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

    public LichSuChiDungKhac() {
    }

    public LichSuChiDungKhac(String id, String tenChiDung, float phi, String moTa, String thoiGianChiDung, String thoiGianCapNhat, boolean daXoa) {
        this.id = id;
        this.tenChiDung = tenChiDung;
        this.phi = phi;
        this.moTa = moTa;
        this.thoiGianChiDung = thoiGianChiDung;
        this.thoiGianCapNhat = thoiGianCapNhat;
        this.daXoa = daXoa;
    }
}
