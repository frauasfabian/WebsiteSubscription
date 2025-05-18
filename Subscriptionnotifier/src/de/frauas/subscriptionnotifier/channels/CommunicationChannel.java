package de.frauas.subscriptionnotifier.channels;

public interface CommunicationChannel {
    void sendNotification(String message);
}