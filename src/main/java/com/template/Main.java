package com.template;

import com.template.Bot.Bot;
import com.template.Database.MySQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Main {

    public static final String basePackage = "com.template.Bot";
    static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static Properties database = new Properties();
    public static Properties discord = new Properties();

    public static void main(String[] args) throws IOException {

        // Load discord.properties
        logger.info("Loading Properties");
        try {
            InputStream databaseStream = new FileInputStream("src/main/resources/database.properties");
            database.load(databaseStream);
        } catch (IOException e) {
            logger.error("Could not load database.properties");
            throw new RuntimeException(e);
        }
        logger.info("database.properties loaded");
        try {
            InputStream discordStream = new FileInputStream("src/main/resources/discord.properties");
            discord.load(new InputStreamReader(discordStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error("Could not load discord.properties");
            throw new RuntimeException(e);
        }
        logger.info("discord.properties loaded");

        //Checking Database
        logger.info("Checking Database");

        if (database.getProperty("db.enabled").equalsIgnoreCase("false") || database.getProperty("db.enabled") == null){
            logger.info("MySQL is disabled");
        }else {
            MySQL.connect();
            if (!MySQL.isConnected()) {
                logger.error("Database not connected");
                System.exit(0);
            }
            logger.info("Database connected");
        }

        // Start Bot
        logger.info("Starting Bot");
        try {
            Bot.startDiscordBot();
        } catch (InterruptedException e) {
            logger.error("Error while starting Bot", e);
            System.exit(0);
        }
        logger.info("Bot started");
    }
}
