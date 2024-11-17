package HospitalNotificationSystem;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @class HNSTelegramBot
 * @brief Abstract base class for sending messages to a Telegram bot.
 * 
 * This class provides functionality to send notifications to a Telegram bot 
 * using the Telegram Bot API. Subclasses should implement the `getChatId` 
 * method to retrieve the specific chat ID where the messages will be sent.
 */
public abstract class HNSTelegramBot {
    
    /** 
     * @brief File path for storing Telegram bot details.
     * 
     * The file containing the Telegram bot token and chat ID. This can be 
     * updated when changing laptops or configurations.
     */
    protected String filePath = "C:/Users/Werner Soon Shi Xu/Downloads/telegramDetails.txt";
    
    /**
     * @brief Abstract method to retrieve the chat ID.
     * 
     * Subclasses should implement this method to provide the specific chat ID 
     * from the stored file or other configuration.
     * 
     * @throws IOException If an error occurs while reading the chat ID from the file.
     */
    public abstract void getChatId() throws IOException;

    /**
     * @brief Sends a message to a Telegram bot.
     * 
     * This method sends a message to a specific chat in Telegram using the 
     * Telegram Bot API. The message, chat ID, and bot token are used to 
     * construct the request to the Telegram servers.
     * 
     * @param message The message to send to the Telegram chat.
     * @param chatId The chat ID of the recipient.
     * @param botToken The bot token to authenticate the request.
     * 
     * @throws IOException If an error occurs while sending the message.
     */
    public void sendToTele(String message, String chatId, String botToken) {
        try {
            // Build the API URL using the bot token
            String apiUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";

            // Create the payload containing the message and chat ID in JSON format
            String payload = "{\"chat_id\":\"" + chatId + "\",\"text\":\"" + message + "\"}";

            // Open a connection to the Telegram API
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send the payload to Telegram
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check the response code from Telegram's server
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Message sent successfully!");
            } else {
                System.out.println("Error: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
