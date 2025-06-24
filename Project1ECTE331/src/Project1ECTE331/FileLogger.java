package Project1ECTE331;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Random;

public class FileLogger {
    public static final int MAX_ATTEMPTS = 3;

    public static void log(String logBase, String logMessage) {
        Random failureSimulator = new Random();
        boolean logWritten = false;

        for (int attempt = 0; attempt <= MAX_ATTEMPTS; attempt++) {
            String currentFile = (attempt == 0) ? logBase + ".txt" : logBase + attempt + ".txt";
            try {
                // Simulate 40% failure rate
                if (failureSimulator.nextInt(100) < 40) {
                    throw new IOException("Artificial write failure");
                }

                try (PrintWriter writer = new PrintWriter(new FileWriter(currentFile, true))) {
                    writer.println("[" + LocalDateTime.now() + "] " + logMessage);
                }

                logWritten = true;
                break;
            } catch (IOException ex) {
                // Continue to next backup
            }
        }

        if (!logWritten) {
            try (PrintWriter fallbackWriter = new PrintWriter(new FileWriter("principal_log.txt", true))) {
                fallbackWriter.println("[" + LocalDateTime.now() + "] Logging failed for message: " + logMessage);
            } catch (IOException finalEx) {
                finalEx.printStackTrace();
            }
        }
    }
}
