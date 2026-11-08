package com.example.takeaway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TakeawayFeatureTest {

    @Mock SmsSender smsSender;

    @Test
    void orders() {
        Menu menu =new Menu(Map.of("Pizza",850, "Cod",450));
        Order order = new Order(menu);

        Clock fixedClock = Clock.fixed(Instant.parse("2026-10-30T17:52:00Z"), ZoneId.of("UTC"));

        Takeaway takeaway = new Takeaway(smsSender,fixedClock);

        // Act
        order.add("Pizza",1);
        order.add("Pizza",1);
        order.add("Cod",1);

        takeaway.place(order, 2150);

        // Assert
        verify(smsSender).send("Thank you! Your order was placed and will be delivered before 18:52");
    }

    @Test
    void rejectsCheckoutAndSendsNoTextWhenExpectedTotalIsIncorrect() {
        // Arrange
        Menu menu = new Menu(Map.of("Pizza", 850, "Cod", 450));

        Order order=new Order(menu);
        Clock fixedClock = Clock.fixed(Instant.parse("2026-10-30T17:52:00Z"), ZoneId.of("UTC"));
        Takeaway takeaway = new Takeaway(smsSender, fixedClock);

        order.add("Pizza",1);
        order.add("Pizza",1);
        order.add("Cod",1);

        int wrongTotal = 3000;

        // Act & Assert
        var ex = assertThrows(IllegalArgumentException.class,
                () -> takeaway.place(order, wrongTotal));

        assertEquals("Order total 2150 does not match the expected total 3000", ex.getMessage());
        verify(smsSender, never()).send(any());


    }

    }
