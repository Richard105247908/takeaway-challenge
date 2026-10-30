package com.example.takeaway;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

class MenuTest {

    // Test #1 (RED): the menu knows the price of each dish.
    @Test
    void menuReturnsThePriceOfADish() {
        Menu menu = new Menu(Map.of("Margherita", 850, "Tiramisu", 450));

        assertEquals(850, menu.priceOf("Margherita"));
    }
}
