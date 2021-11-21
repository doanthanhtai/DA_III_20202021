package com.example.tomtep.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ao implements Serializable {
    private String id;
    private String maAo;
    private String tenAo;
    private String moTaAo;
    private String ngayTao;
    private String ngayThu;
    private CheDoAn cheDoAn;
    private List<LichSuMoiTruong> lichSuMoiTruongs;
    private List<LichSuChiDungKhac> lichSuChiDungKhacs;
    private List<LichSuSuDungSanPham> lichSuSuDungSanPhams;
    private boolean trangThai;
    private boolean daXoa;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaAo() {
        return maAo;
    }

    public void setMaAo(String maAo) {
        this.maAo = maAo;
    }

    public String getTenAo() {
        return tenAo;
    }

    public void setTenAo(String tenAo) {
        this.tenAo = tenAo;
    }

    public String getMoTaAo() {
        return moTaAo;
    }

    public void setMoTaAo(String moTaAo) {
        this.moTaAo = moTaAo;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNgayThu() {
        return ngayThu;
    }

    public void setNgayThu(String ngayThu) {
        this.ngayThu = ngayThu;
    }

    public CheDoAn getCheDoAn() {
        return cheDoAn;
    }

    public void setCheDoAn(CheDoAn cheDoAn) {
        this.cheDoAn = cheDoAn;
    }

    public List<LichSuMoiTruong> getLichSuMoiTruongs() {
        return lichSuMoiTruongs;
    }

    public void setLichSuMoiTruongs(List<LichSuMoiTruong> lichSuMoiTruongs) {
        this.lichSuMoiTruongs = lichSuMoiTruongs;
    }

    public List<LichSuChiDungKhac> getLichSuChiDungKhacs() {
        return lichSuChiDungKhacs;
    }

    public void setLichSuChiDungKhacs(List<LichSuChiDungKhac> lichSuChiDungKhacs) {
        this.lichSuChiDungKhacs = lichSuChiDungKhacs;
    }

    public List<LichSuSuDungSanPham> getLichSuSuDungSanPhams() {
        return lichSuSuDungSanPhams;
    }

    public void setLichSuSuDungSanPhams(List<LichSuSuDungSanPham> lichSuSuDungSanPhams) {
        this.lichSuSuDungSanPhams = lichSuSuDungSanPhams;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public boolean isDaXoa() {
        return daXoa;
    }

    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
    }

    public Ao() {
    }

    public Ao(String id, String maAo, String tenAo, String moTaAo, String ngayTao, String ngayThu, CheDoAn cheDoAn, List<LichSuMoiTruong> lichSuMoiTruongs, List<LichSuChiDungKhac> lichSuChiDungKhacs, List<LichSuSuDungSanPham> lichSuSuDungSanPhams, boolean trangThai, boolean daXoa) {
        this.id = id;
        this.maAo = maAo;
        this.tenAo = tenAo;
        this.moTaAo = moTaAo;
        this.ngayTao = ngayTao;
        this.ngayThu = ngayThu;
        this.cheDoAn = cheDoAn;
        this.lichSuMoiTruongs = lichSuMoiTruongs;
        this.lichSuChiDungKhacs = lichSuChiDungKhacs;
        this.lichSuSuDungSanPhams = lichSuSuDungSanPhams;
        this.trangThai = trangThai;
        this.daXoa = daXoa;
    }
}
