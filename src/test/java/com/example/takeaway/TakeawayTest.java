package com.example.takeaway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TakeawayTest {

    @Mock Order order;
    @Mock SmsSender smsSender;

    @Test
    void testPlaceOrderWithMismatchingTotalThrowsException() {
        when(order.total()).thenReturn(2150);

        Clock fixedClock= Clock.fixed(Instant.parse("2026-10-30T17:52:00Z"), ZoneId.of("UTC"));
        Takeaway takeaway=new Takeaway(smsSender, fixedClock);

        var ex=assertThrows(IllegalArgumentException.class,
                ()-> takeaway.place(order,1000));

        verify(smsSender,never()).send(any());

    }
}
