package chatapp.internal.logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());
    private static final String LOG_FILE_PATH = "application.log";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    static {
        try {
            Handler fileHandler = new FileHandler(LOG_FILE_PATH);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            CustomFormatter formatter = new CustomFormatter();
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            logger.addHandler(consoleHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Custom formatter to include class name and method name in logs
    static class CustomFormatter extends java.util.logging.Formatter {
        @Override
        public String format(java.util.logging.LogRecord record) {
            String color = ANSI_RESET;

            if (record.getLevel() == Level.SEVERE) {
                color = ANSI_RED;
            } else if (record.getLevel() == Level.WARNING) {
                color = ANSI_YELLOW;
            } else if (record.getLevel() == Level.INFO) {
                color = ANSI_CYAN;
            }

            return color + "[" + record.getLevel() + "] "
                    + record.getSourceClassName() + "."
                    + record.getSourceMethodName() + ": "
                    + record.getMessage() + ANSI_RESET + "\n";
        }
    }

    // Method to get the logger instance
    public static Logger getLogger() {
        return logger;
    }
}