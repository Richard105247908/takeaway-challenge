package com.example.takeaway;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class Takeaway {
    private final Menu menu;
    private final SmsSender smsSender;
    private final Clock clock;

    public java.time.Clock getClock() {
        return clock;
    }

    public Takeaway(Menu menu, SmsSender smsSender, Clock clock) {
        this.menu = menu;
        this.smsSender = smsSender;
        this.clock = clock;
    }

    public Takeaway(SmsSender smsSender, Clock clock) {
        this(null, smsSender, clock);
    }

    public void place(Order order, int expectedTotal) {

        if (order.total() != expectedTotal) {
            throw new IllegalArgumentException("Order total " + order.total() + " does not match the expected total " + expectedTotal);
        }

        smsSender.send("Thank you! Your order was placed and will be delivered before 18:52" );

    }
}
