package fr.skygames.managethediscord.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomLogger {
    private final Logger logger;

    public CustomLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    public void info(String message) {
        logger.info("\u001B[32m" + message + "\u001B[0m");
    }

    public void warning(String message) {
        logger.warn("\u001B[33m" + message + "\u001B[33m");
    }

    public void severe(String message) {
        logger.error("\u001B[31m" + message + "\u001B[0m");;
    }
}
