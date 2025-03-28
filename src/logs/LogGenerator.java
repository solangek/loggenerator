import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/** a program that generates log files with random log entries
 * and writes them to the specified output directory.
 * usage: java LogFileGenerator <output_directory> <num_files> <max_size_kb>
 *     <output_directory> - the directory where log files will be written (should exist)
 *     <num_files> - the number of log files to generate
 *     <max_size_kb> - the maximum size of each log file in kilobytes
 *     Example: java LogFileGenerator logs 10 100
 */
public class LogGenerator {
    private static final String[] LOG_LEVELS = {"INFO", "WARNING", "ERROR"};
    private static final String[] SOURCES = {"Server1", "Server2", "Server3", "Server4", "Database", "LoadBalancer"};
    private static final String[] MESSAGES = {
            "User logged in", "Database connection failed", "High memory usage detected",
            "CPU usage exceeded threshold", "Failed login attempt", "Service restarted successfully"
    };

    private static final Random RANDOM = new Random();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java LogFileGenerator <output_directory> <num_files> <max_size_kb>");
            return;
        }

        String outputDir = args[0];
        int numFiles = Integer.parseInt(args[1]);
        int maxSizeKB = Integer.parseInt(args[2]);

        generateLogFiles(outputDir, numFiles, maxSizeKB);
    }

    public static void generateLogFiles(String outputDir, int numFiles, int maxSizeKB) {
        File directory = new File(outputDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (int i = 1; i <= numFiles; i++) {
            File logFile = new File(outputDir, "logfile_" + i + ".log");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
                int currentSize = 0;
                long startTime = System.currentTimeMillis() - (24L * 60 * 60 * 1000); // Start from 24 hours ago

                while (currentSize < maxSizeKB * 1024) {
                    String logEntry = generateRandomLogEntry(startTime);
                    writer.write(logEntry);
                    writer.newLine();
                    currentSize += logEntry.length() + 1; // +1 for newline character

                    // Advance the timestamp randomly (between 1 second to 5 minutes)
                    startTime += (RANDOM.nextInt(300) + 1) * 1000;
                }
                System.out.println("Generated: " + logFile.getAbsolutePath() + " (" + maxSizeKB + " KB)");
            } catch (IOException e) {
                System.err.println("Error writing file: " + logFile.getName());
                e.printStackTrace();
            }
        }
    }

    private static String generateRandomLogEntry(long timestamp) {
        String formattedTime = "[" + DATE_FORMAT.format(new Date(timestamp)) + "]";
        String level = "[" + LOG_LEVELS[RANDOM.nextInt(LOG_LEVELS.length)] + "]";
        String source = "[" + SOURCES[RANDOM.nextInt(SOURCES.length)] + "]";
        String message = "[" + MESSAGES[RANDOM.nextInt(MESSAGES.length)] + "]";
        return formattedTime + " " + level + " " + source + " " + message;
    }
}