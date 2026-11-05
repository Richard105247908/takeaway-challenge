package com.example.takeaway;

import java.time.Clock;

public class Takeaway {
    private final SmsSender smsSender;
    private final Clock clock;

    public Takeaway(SmsSender smsSender, Clock clock) {
        this.smsSender=smsSender;
        this.clock=clock;


    }

    public void place(Order order, int expectedTotal) {

        if (order.total()<expectedTotal){

        }

    }
}
