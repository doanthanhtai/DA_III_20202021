package com.example.tomtep.Interface;

import com.example.tomtep.model.Lake;

public interface IClickItemLakeListener {
    void onClick(Lake lake);

    boolean onLongClick(Lake lake);
}
