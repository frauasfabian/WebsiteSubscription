package de.frauas.subscriptionnotifier.observer;

import de.frauas.subscriptionnotifier.model.Subscription;
import de.frauas.subscriptionnotifier.model.User;

public interface SubscriptionObserver {
    void update(User user, Subscription subscription);
}
