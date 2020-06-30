package com.example.lakecircle.ui.home.merchant;

import java.util.Comparator;

public class MerchantChangeNumComparator implements Comparator<Merchant> {

    @Override
    public int compare(Merchant o1, Merchant o2) {
        return o1.getChange_num()-o2.getChange_num();
    }
}
