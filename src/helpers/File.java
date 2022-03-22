package helpers;

import java.io.*;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(message);
        }
    }

    /**
     * Read text from a specific file
     * @param fileName The name of the file
     * @return A string that contains all text in file
     * @throws IOException Exception if no file, or reading bug...
     */
    public static String read(String fileName) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            return sb.toString();
        }
    }
}
