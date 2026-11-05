package com.example.takeaway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class OrderTest {

    @Mock Menu menu;
     //test 5
    @Test
    void totalIsTheSumOfDishPricesTimesQuantities(){

        when(menu.priceOf("Pizza")).thenReturn(850);
        when(menu.priceOf("Cod")).thenReturn(450);

        Order order = new Order(menu);

        order.add("Pizza",2);
        order.add("Cod",1);

        assertEquals(2150,order.total());


    }

    @Test





}


