package de.frauas.subscriptionnotifier.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userID;
    private String username;
    private String email;
    private String phoneNumber;
    private List<Subscription> subscriptions;

    public User(String userID, String username, String email, String phoneNumber) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.subscriptions = new ArrayList<>();
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.startChecking();
    }

    public void removeSubscription(String subscriptionID) {
        subscriptions.removeIf(sub -> {
            if (sub.getSubscriptionID().equals(subscriptionID)) {
                sub.stopChecking();
                return true;
            }
            return false;
        });
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void viewSubscriptions() {
        System.out.println("Subscriptions for user " + username + ":");
        for (Subscription sub : subscriptions) {
            System.out.println("ID: " + sub.getSubscriptionID() + ", URL: " + sub.getUrl() + ", frequency: " + sub.getFrequencyInSeconds() + " sec");
        }
    }

    // Getters and setters (optional)
    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

}
