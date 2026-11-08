package com.example.takeaway;

import java.time.Clock;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Takeaway {
    private final SmsSender smsSender;
    private final Clock clock;


    public Takeaway(SmsSender smsSender, Clock clock) {
        this.smsSender = smsSender;
        this.clock = clock;
    }

    public void place(Order order, int expectedTotal) {
        int orderTotal = order.total();

        if (orderTotal != expectedTotal) {
            throw new IllegalArgumentException("Order total " + order.total() + " does not match the expected total " + expectedTotal);
        }

        LocalTime deliveryTime = LocalTime.now(clock).plusHours(1);
        String formattedTime = deliveryTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        smsSender.send("Thank you! Your order was placed and will be delivered before 18:52");

    }
}
