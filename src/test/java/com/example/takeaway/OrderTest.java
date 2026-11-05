package com.example.takeaway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class OrderTest {

    @Mock Menu menu;
     //test 5
    @Test
    void totalIsTheSumOfDishPricesTimesQuantities(){
        when(menu.has("Pizza")).thenReturn(true);
        when(menu.has("Cod")).thenReturn(true);
        when(menu.priceOf("Pizza")).thenReturn(850);
        when(menu.priceOf("Cod")).thenReturn(450);

        Order order = new Order(menu);

        order.add("Pizza",2);
        order.add("Cod",1);

        assertEquals(2150,order.total());
    }

    //change to parametirisesTest
    @Test
    void zeroQuantityIsRejected(){
        Order order=new Order(menu);
        String dish="Pizza";
        int quantity=0;

        var ex = assertThrows(IllegalArgumentException.class,
                () -> order.add(dish,quantity));

        assertEquals("Quantity must be at least 1, but was "+quantity ,ex.getMessage());
    }

    @Test
    void negativeQuantityIsRejected(){
        Order order=new Order(menu);
        String dish="Pizza";
        int quantity2=-1;

        var ex2 = assertThrows(IllegalArgumentException.class,
                () -> order.add(dish,quantity2));

        assertEquals("Quantity must be at least 1, but was "+quantity2,ex2.getMessage());
    }

    @Test
    void dishNotOnTheMenuCannotBeAdded(){

        Order order=new Order(menu);

        String dish ="Pizza of Doom";

        var ex = assertThrows(IllegalArgumentException.class,
                () -> order.add(dish, 1));

        assertEquals("The "+dish +" is not on the menu", ex.getMessage());
    }






}


