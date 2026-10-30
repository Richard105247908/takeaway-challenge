package com.example.takeaway;

// Your own small interface in front of Twilio. Tests mock this, so no real texts are sent.
// Only the real app uses a TwilioSmsSender (you write that last).
public interface SmsSender {
    void send(String message);
}
