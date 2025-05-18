package de.frauas.subscriptionnotifier.channels;

public class SmsChannel implements CommunicationChannel {
    @Override
    public void sendNotification(String message) {
        System.out.println("Notification has been sent via SMS: " + message);
    }
}
