package de.frauas.subscriptionnotifier.channels;

public class EmailChannel implements CommunicationChannel {
    @Override
    public void sendNotification(String message) {
        System.out.println("Notification has been sent via Email: " + message);
    }
}
