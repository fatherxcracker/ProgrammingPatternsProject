package com.example.sharpburgermanager.logging;
import java.io.IOException;
import java.util.logging.*;

public class SharpBurgerLogger {
    private static final Logger logger = Logger.getLogger("SharpBurgerLogger");

    static {
        try {
            FileHandler fileHandler =
                    new FileHandler("sharpburger.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.setLevel(Level.ALL); // So it captures everything
            fileHandler.setLevel(Level.ALL);

            logger.setUseParentHandlers(false); // No console output

        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
