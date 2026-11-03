package com.example.takeaway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

class MenuTest {

    // Test #1 (RED): the menu knows the price of each dish.
    @Test
    void menuReturnsThePriceOfADish() {
        Menu menu = new Menu(Map.of("Margherita", 850, "Tiramisu", 450));

        assertEquals(850, menu.priceOf("Margherita"));
    }

    @Test
    void menuReturnThePriceOfADishNull(){
        Menu menu = new Menu(Map.of("Pizza", 850, "Cod", 450));

        String dish="Magherita";

        var ex = assertThrows(IllegalArgumentException.class,
                () ->menu.priceOf(dish));


        assertEquals("The " +dish +" is not on the menu", ex.getMessage());


    }
}
