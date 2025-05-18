package de.frauas.subscriptionnotifier.model;

import de.frauas.subscriptionnotifier.channels.CommunicationChannel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.util.Timer;
import java.util.TimerTask;

public class Subscription {
    private String subscriptionID;
    private String url;
    private int frequencyInSeconds;
    private CommunicationChannel channel;

    private Timer timer;
    private String lastContentHash = "";

    private HttpClient httpClient;

    public Subscription(String subscriptionID, String url, int frequencyInSeconds, CommunicationChannel channel) {
        this.subscriptionID = subscriptionID;
        this.url = url;
        this.frequencyInSeconds = frequencyInSeconds;
        this.channel = channel;

        this.httpClient = HttpClient.newHttpClient();
    }

    public void startChecking() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForUpdates();
            }
        }, 0, frequencyInSeconds * 1000L);
    }

    public void stopChecking() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void checkForUpdates() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String content = response.body();
                String currentHash = sha256(content);

                if (!currentHash.equals(lastContentHash)) {
                    lastContentHash = currentHash;
                    Notification notification = new Notification("Website " + url + " has been updated.");
                    channel.sendNotification(notification.getMessage());
                }
            } else {
                System.out.println("Failed to fetch URL " + url + ", status code: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("Error checking updates for " + url + ": " + e.getMessage());
        }
    }

    private String sha256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(input.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Getters added here:
    public String getSubscriptionID() {
        return subscriptionID;
    }

    public String getUrl() {
        return url;
    }

    public int getFrequencyInSeconds() {
        return frequencyInSeconds;
    }
    
    public CommunicationChannel getChannel() {
        return channel;
    }

}
