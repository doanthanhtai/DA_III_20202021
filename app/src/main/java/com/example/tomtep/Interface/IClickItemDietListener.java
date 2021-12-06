package com.example.tomtep.Interface;

import com.example.tomtep.model.Diet;

public interface IClickItemDietListener {
    void onClick(Diet diet);
    void onLongClick(Diet diet);
    void onClickFeeding(Diet diet, boolean condition);
}
