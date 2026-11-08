package com.example.takeaway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TakeawayTest {

    @Mock
    Order order;
    @Mock
    SmsSender smsSender;
    Takeaway takeaway;
    Clock fixedClock;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2026-10-30T17:52:00Z"), ZoneId.of("UTC"));
        takeaway = new Takeaway(smsSender, fixedClock);

    }

    @Test
    void testPlaceOrderWithMismatchingTotalThrowsException() {
        when(order.total()).thenReturn(2150);


        int expectedTotal = 1000;

        var ex = assertThrows(IllegalArgumentException.class,
                () -> takeaway.place(order, expectedTotal));

        assertEquals("Order total 2150 does not match the expected total 1000", ex.getMessage());
        verify(smsSender, never()).send(any());
    }

    @Test
    void orderCalculatesDeliveryTimeBasedOnClock() {

        when(order.total()).thenReturn(2150);
        takeaway.place(order, 2150);

        verify(smsSender).send("Thank you! Your order was placed and will be delivered before 18:52");
    }

    @Test
    void deliveryTimeIsOneHourAfterTheClockTime() {
        Clock morning = Clock.fixed(Instant.parse("2026-10-30T09:15:00Z"), ZoneId.of("UTC"));
        Takeaway morningTakeaway = new Takeaway(smsSender, morning);
        when(order.total()).thenReturn(2150);

        morningTakeaway.place(order, 2150);
        verify(smsSender).send("Thank you! Your order was placed and will be delivered before 10:15");
    }

    @Test
    void deliveryTimeWrapsAroundMidnight() {
        Clock midnightClock = Clock.fixed(Instant.parse("2026-10-30T23:30:00Z"), ZoneId.of("UTC"));
        takeaway = new Takeaway(smsSender, midnightClock);
        when(order.total()).thenReturn(2150);

        takeaway.place(order, 2150);

        verify(smsSender).send("Thank you! Your order was placed and will be delivered before 00:30");
    }
    
}

