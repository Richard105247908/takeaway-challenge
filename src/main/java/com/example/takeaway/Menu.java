package com.example.takeaway;

import java.util.Map;

// Deliberately incomplete: this compiles, but the first test is RED.
// Make it green with the smallest change you can, then refactor.
// Prices are in pence (int) to avoid floating-point rounding errors.
public class Menu {

    private final Map<String,Integer> menuPrices;


    public Menu(Map<String, Integer> pricesInPence) {
        this.menuPrices=pricesInPence;
    }


    public int priceOf(String dish) {
        return 0;
    }

}

