package com.example.da105_g4_v0.cart;

import com.example.da105_g4_v0.store.Store;

import java.io.Serializable;
import java.util.List;

public class CartVO implements Serializable {
    private List<Store> itemList;

    public CartVO(){

    }
    public List<Store> getItemList() {
        return itemList;
    }

    public void setItemList(List<Store> itemList) {
        this.itemList = itemList;
    }

   public void addList(Store storeItem){

        itemList.add(storeItem);
   }



}
