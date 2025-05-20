package de.frauas.subscriptionnotifier.ui;

import de.frauas.subscriptionnotifier.channels.EmailChannel;
import de.frauas.subscriptionnotifier.channels.SmsChannel;
import de.frauas.subscriptionnotifier.model.Subscription;
import de.frauas.subscriptionnotifier.model.User;
import de.frauas.subscriptionnotifier.channels.CommunicationChannel;
import de.frauas.subscriptionnotifier.observer.SubscriptionObserver;
import de.frauas.subscriptionnotifier.observer.ConfirmationMail;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final List<User> users = new ArrayList<>();
    private User selectedUser;
    private final List<SubscriptionObserver> observers = new ArrayList<>();

    private JTextField usernameField, emailField, phoneField;
    private JTextField urlField, frequencyField;
    private JComboBox<String> channelBox;
    private JComboBox<String> userDropdown;
    private JButton addUserButton, addSubscriptionButton, removeSubscriptionButton;
    private JTextArea outputArea;
    private JButton viewSubscriptionsButton;
    private JButton editSubscriptionButton;


    public MainFrame() {
        setTitle("Website Subscription Notifier");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        observers.add(new ConfirmationMail());

        JPanel userPanel = new JPanel();
        userPanel.setBorder(BorderFactory.createTitledBorder("User Management"));

        usernameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        addUserButton = new JButton("Add User");
        userDropdown = new JComboBox<>();
        userDropdown.addActionListener(e -> selectUser());

        userPanel.setLayout(new GridLayout(4, 2, 5, 5));
        userPanel.add(new JLabel("Username:"));
        userPanel.add(usernameField);
        userPanel.add(new JLabel("Email:"));
        userPanel.add(emailField);
        userPanel.add(new JLabel("Phone Number:"));
        userPanel.add(phoneField);
        userPanel.add(addUserButton);
        userPanel.add(userDropdown);

        JPanel subscriptionPanel = new JPanel();
        subscriptionPanel.setBorder(BorderFactory.createTitledBorder("Subscription"));
        urlField = new JTextField();
        frequencyField = new JTextField();
        channelBox = new JComboBox<>(new String[]{"Email", "SMS"});
        addSubscriptionButton = new JButton("Add Subscription");
        removeSubscriptionButton = new JButton("Remove Subscription");
        viewSubscriptionsButton = new JButton("View Subscriptions");
        viewSubscriptionsButton.addActionListener(e -> viewSubscriptions());
        editSubscriptionButton = new JButton("Edit Subscription");
        editSubscriptionButton.addActionListener(e -> editSubscription());


        subscriptionPanel.setLayout(new GridLayout(6, 2, 5, 5));
        subscriptionPanel.add(new JLabel("Website URL:"));
        subscriptionPanel.add(urlField);
        subscriptionPanel.add(new JLabel("Frequency (sec):"));
        subscriptionPanel.add(frequencyField);
        subscriptionPanel.add(new JLabel("Channel:"));
        subscriptionPanel.add(channelBox);
        subscriptionPanel.add(addSubscriptionButton);
        subscriptionPanel.add(removeSubscriptionButton);
        subscriptionPanel.add(viewSubscriptionsButton);
        subscriptionPanel.add(editSubscriptionButton);


        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        inputPanel.add(userPanel);
        inputPanel.add(subscriptionPanel);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addUserButton.addActionListener(e -> addUser());
        addSubscriptionButton.addActionListener(e -> addSubscription());
        removeSubscriptionButton.addActionListener(e -> removeSelectedSubscription());
    }

    private void addUser() {
        String name = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            outputArea.setText("Please enter username, email, and optionally phone number.");
            return;
        }

        String id = "user" + (users.size() + 1);
        User user = new User(id, name, email, phone);
        users.add(user);
        userDropdown.addItem(name);
        userDropdown.setSelectedItem(name);
        selectedUser = user;
        outputArea.setText("User " + name + " added.");
    }

    private void selectUser() {
        String name = (String) userDropdown.getSelectedItem();
        if (name != null) {
            for (User user : users) {
                if (user.getUsername().equals(name)) {
                    selectedUser = user;
                    outputArea.setText("Selected user: " + name);
                    break;
                }
            }
        }
    }

    private void addSubscription() {
        if (selectedUser == null) {
            outputArea.setText("Please select a user first.");
            return;
        }

        String url = urlField.getText().trim();
        String freqText = frequencyField.getText().trim();
        String channelType = (String) channelBox.getSelectedItem();

        if (url.isEmpty() || freqText.isEmpty()) {
            outputArea.setText("Please fill in all subscription fields.");
            return;
        }

        try {
            int freq = Integer.parseInt(freqText);
            CommunicationChannel channel;
            if ("Email".equals(channelType)) {
                channel = new EmailChannel();
            } else {
                channel = new SmsChannel();
            }

            String subId = "sub" + (selectedUser.getSubscriptions().size() + 1);
            Subscription subscription = new Subscription(subId, url, freq, channel);
            selectedUser.addSubscription(subscription);
            for (SubscriptionObserver observer : observers) {
                observer.update(selectedUser, subscription);
            }
            outputArea.setText("Subscription to " + url + " added for user " + selectedUser.getUsername());
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid frequency. Please enter a number.");
        }
    }

    private void removeSelectedSubscription() {
        if (selectedUser == null) {
            outputArea.setText("Please select a user.");
            return;
        }

        List<Subscription> subs = selectedUser.getSubscriptions();
        if (subs.isEmpty()) {
            outputArea.setText("No subscriptions to remove.");
            return;
        }

        String[] options = subs.stream()
                .map(sub -> sub.getSubscriptionID() + " - " + sub.getUrl())
                .toArray(String[]::new);

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Select subscription to remove:",
                "Remove Subscription",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice != null) {
            String subscriptionID = choice.split(" - ")[0];
            selectedUser.removeSubscription(subscriptionID);
            outputArea.setText("Removed subscription: " + choice);
        }
    }
    
    private void viewSubscriptions() {
        if (selectedUser == null) {
            outputArea.setText("Please select a user.");
            return;
        }

        List<Subscription> subs = selectedUser.getSubscriptions();
        if (subs.isEmpty()) {
            outputArea.setText("No subscriptions found for user " + selectedUser.getUsername());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Subscriptions for user ").append(selectedUser.getUsername()).append(":\n");
        for (Subscription sub : subs) {
            sb.append("ID: ").append(sub.getSubscriptionID())
              .append(", URL: ").append(sub.getUrl())
              .append(", Frequency: ").append(sub.getFrequencyInSeconds()).append(" sec\n");
        }

        outputArea.setText(sb.toString());
    }
    
    private void editSubscription() {
        if (selectedUser == null) {
            outputArea.setText("Please select a user.");
            return;
        }

        List<Subscription> subs = selectedUser.getSubscriptions();
        if (subs.isEmpty()) {
            outputArea.setText("No subscriptions available to edit.");
            return;
        }

        String[] options = subs.stream()
                .map(sub -> sub.getSubscriptionID() + " - " + sub.getUrl())
                .toArray(String[]::new);

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Select a subscription to edit:",
                "Edit Subscription",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice != null) {
            String subscriptionID = choice.split(" - ")[0];
            Subscription selectedSub = null;

            for (Subscription sub : subs) {
                if (sub.getSubscriptionID().equals(subscriptionID)) {
                    selectedSub = sub;
                    break;
                }
            }

            if (selectedSub != null) {
                String newUrl = JOptionPane.showInputDialog(this, "Enter new URL:", selectedSub.getUrl());
                String newFreq = JOptionPane.showInputDialog(this, "Enter new frequency (sec):", selectedSub.getFrequencyInSeconds());
                String[] channels = {"Email", "SMS"};
                String newChannel = (String) JOptionPane.showInputDialog(this, "Select new channel:", "Channel",
                        JOptionPane.PLAIN_MESSAGE, null, channels,
                        selectedSub.getChannel() instanceof EmailChannel ? "Email" : "SMS");

                try {
                    int freq = Integer.parseInt(newFreq);
                    CommunicationChannel channel = "Email".equals(newChannel) ? new EmailChannel() : new SmsChannel();

                    // Stop old subscription and create a new one with the same ID
                    selectedSub.stopChecking();
                    Subscription updatedSub = new Subscription(subscriptionID, newUrl, freq, channel);

                    // Replace the subscription
                    selectedUser.removeSubscription(subscriptionID);
                    selectedUser.addSubscription(updatedSub);

                    outputArea.setText("Subscription updated successfully.");
                } catch (NumberFormatException e) {
                    outputArea.setText("Invalid frequency value.");
                }
            }
        }
    }

}
