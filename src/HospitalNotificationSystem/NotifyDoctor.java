package HospitalNotificationSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * NotifyDoctor
 * Singleton class for notifying a doctor via Telegram.
 * 
 * This class allows sending notifications to a doctor through Telegram using 
 * the Telegram Bot API. It uses a singleton pattern to ensure only one instance 
 * of the notification system exists. The bot token and chat ID are read from 
 * a configuration file.
 */
public class NotifyDoctor extends HNSTelegramBot {
    
    /** Singleton instance of the NotifyDoctor class. */
    private static NotifyDoctor instance_doctor;
    
    /** The chat ID for sending messages to the doctor. */
    private String chatId;
    
    /** The Telegram bot token used for authentication. */
    private String botToken;

    /**
     * Private constructor to prevent direct instantiation.
     * 
     * This constructor ensures that the class can only be instantiated 
     * through the `getInstance()` method to enforce the Singleton pattern.
     */
    private NotifyDoctor() {}

    /**
     * Retrieves the singleton instance of the NotifyDoctor class.
     * 
     * If the instance does not exist, it will create a new one. This method 
     * ensures that only one instance of the class is used throughout the 
     * application.
     * 
     * @return Singleton instance of the NotifyDoctor class.
     */
    public static NotifyDoctor getInstance() {
        if (instance_doctor == null) {
            instance_doctor = new NotifyDoctor();
        }
        return instance_doctor;
    }

    /**
     * Retrieves the chat ID and bot token from a configuration file.
     * 
     * This method reads the configuration file specified by `filePath` to 
     * retrieve the bot token and chat ID. The file should contain the bot 
     * token and chat ID separated by a "|" symbol.
     * 
     * @throws IOException If an error occurs while reading the file.
     */
    @Override
    public void getChatId() throws IOException {
        // Read the contents of the file (Conducted this way to prevent leakage of API Key)
        String content = new String(Files.readAllBytes(Paths.get(filePath))).trim();

        // Split the content using the "|" separator
        String[] parts = content.split("\\|");
        botToken = parts[0].trim();  // botToken is the fifth part
        chatId = parts[1].trim();    // chatId is the sixth part
    }

    /**
     * Sends a notification message to the doctor via Telegram.
     * 
     * This method sends a message to the doctor's Telegram chat using the 
     * bot token and chat ID. It first retrieves the bot token and chat ID 
     * by calling `getChatId()`, then it sends the message to Telegram.
     * 
     * @param message The message to send to the doctor.
     */
    public void notifyDoctorUser(String message) {
        try {
            // Retrieve chat ID and bot token
            getChatId();
        } catch (Exception e) {
            System.out.println("Unable to get telegram Details. Doctor's telegram will not be notified.");
            return;
        }

        // Send the message to the doctor via Telegram
        super.sendToTele(message, chatId, botToken);
        System.out.println("Doctor Telegram Notification Success");
    }
}
