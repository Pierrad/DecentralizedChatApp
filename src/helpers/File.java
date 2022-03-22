package helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Helper class related to file
 */
public class File {
    private File() {}

    /**
     * Write a message into a specific file
     * @param fileName The name of the file
     * @param message The message
     */
    public static void write(String fileName, String message) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(message);
        }
    }
}
