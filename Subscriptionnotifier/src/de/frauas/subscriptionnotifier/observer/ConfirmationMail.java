package de.frauas.subscriptionnotifier.observer;

import de.frauas.subscriptionnotifier.model.Subscription;
import de.frauas.subscriptionnotifier.model.User;

public class ConfirmationMail implements SubscriptionObserver {
    @Override
    public void update(User user, Subscription subscription) {
        System.out.println("Sending confirmation email to: " + user.getEmail());
        System.out.println("Subscription ID: " + subscription.getSubscriptionID());
        System.out.println("Subscribed to: " + subscription.getUrl());
    }
}
