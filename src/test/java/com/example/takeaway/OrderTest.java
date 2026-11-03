package com.example.takeaway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @Mock Menu menu;

    @Test
    void totalIsTheSumOfDishPricesTimesQuantities(){

        when(menu.priceOf("Pizza")).thenReturn(850);
        when(menu.priceOf("Cod")).thenReturn(450);

        Order order = new Order(menu);

        order.Add("Pizza",2);
        o


    }





}


