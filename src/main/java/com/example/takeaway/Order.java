package com.example.takeaway;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private final Menu menu;
    private final Map<String,Integer>items =new HashMap<>();


    public Order(Menu menu){
        this.menu=menu;


    }

    public void add(String dish, int quantity){
        if(quantity<=0){
            throw new IllegalArgumentException("Quantity must be at least 1, but was "+quantity);
        }
        if (!menu.has(dish)) {
            throw new IllegalArgumentException("The " +dish +" is not on the menu");
        }
        items.merge(dish,quantity,Integer::sum);
        //items.put(dish,quantity);  using put replaces instead of add to dish.


    }

    public int total(){
        return items.entrySet().stream()
                .mapToInt(e -> menu.priceOf(e.getKey()) * e.getValue())
                .sum();
        
    }
}
