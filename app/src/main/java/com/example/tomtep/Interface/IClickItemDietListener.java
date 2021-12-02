package com.example.tomtep.Interface;

import com.example.tomtep.model.Ao;

public interface IClickItemDietListener {
    void onClick(Ao ao);
    void onLongClick(Ao ao);
    void onChoAn(Ao ao, boolean trangThai);
}
