package com.example.takeaway;

import java.util.Map;


public class Menu {

    private final Map<String, Integer> menuPrices;


    public Menu(Map<String, Integer> pricesInPence) {
        this.menuPrices = Map.copyOf( pricesInPence);
    }

    public int priceOf(String dish) {
        if (!this.menuPrices.containsKey(dish)){
            throw new IllegalArgumentException("The "+dish +" is not on the menu");
        }
        return this.menuPrices.get(dish);
    }

    public Map<String, Integer>  dishes(){

        return this.menuPrices;

    }

}

