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
    void setUp(){
        takeaway = new Takeaway(smsSender,fixedClock);
        fixedClock = Clock.fixed(Instant.parse("2026-10-30T17:52:00Z"), ZoneId.of("UTC"));

    }

    @Test
    void testPlaceOrderWithMismatchingTotalThrowsException() {
        when(order.total()).thenReturn(2150);


        int expectedTotal = 1000;

        var ex = assertThrows(IllegalArgumentException.class,
                () -> takeaway.place(order, expectedTotal));

        assertEquals("Order total " + order.total() + " does not match the expected total " + expectedTotal, ex.getMessage());

        verify(smsSender, never()).send(any());

    }

    @Test

}
