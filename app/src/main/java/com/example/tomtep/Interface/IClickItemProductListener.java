package com.example.tomtep.Interface;

import com.example.tomtep.model.Product;

public interface IClickItemProductListener {
    void onClickEnterQuatity(Product product);

    void onClickItemProduct(Product product);

    boolean onLongClickItemProduct(Product product);
}
