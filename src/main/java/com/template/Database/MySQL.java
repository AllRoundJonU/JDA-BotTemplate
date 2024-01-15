package com.template.Database;

import com.template.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Properties;

public class MySQL {

    public static Connection database;
    private static OffsetDateTime databaseLastConnection;
    static final Logger logger = LoggerFactory.getLogger(MySQL.class);
    private static final Properties config = Main.database; //Main.config;

    public static boolean isConnected() {
        return (database != null);
    }

    public static void connect(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException ex){
            logger.error("Cannot find MySQL Driver");
            return;
        }

        String host = config.getProperty("db.host");
        String user = config.getProperty("db.user");
        String password = config.getProperty("db.password");
        String databaseTable = config.getProperty("db.name");

        if (!isConnected()){
            try {
                database = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + databaseTable, user, password);
                databaseLastConnection = OffsetDateTime.now();

                Thread databaseThread = new Thread(() -> {
                    while (true) {
                        if(OffsetDateTime.now().minusHours(6).isAfter(databaseLastConnection)){
                            MySQL.updateWithoutException("CREATE TABLE IF NOT EXISTS LebensZeichen(Lebenszeichen VARCHAR(50), PRIMARY KEY (Lebenszeichen))");
                            MySQL.updateWithoutException("DROP TABLE IF EXISTS LebensZeichen");
                        }
                        try {
                            Thread.sleep(1000 * 60 * 60);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage());
                        }
                    }
                });
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static void updateWithoutException(String qry) {
        if (isConnected()) {
            try {
                database.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            databaseLastConnection = OffsetDateTime.now();
        }
    }

    public static boolean update(String qry, String... args){
        if (isConnected()) {
            try {

                for (int i = 0; i < args.length; i++) {
                    args[i] = validateAndSanitizeInput(args[i]);
                }

                qry = String.format(qry, (Object) args);
                database.createStatement().executeUpdate(qry);
                databaseLastConnection = OffsetDateTime.now();
                return true;
            } catch (SQLException e) {
                logger.error("Error while updating the Database", e);
                logger.error(e.getMessage());
            }
        }
        return false;
    }

    public static ResultSet query(String qry, String... args){
        if (isConnected()) {
            try {
                ResultSet rs = database.createStatement().executeQuery(qry);
                databaseLastConnection = OffsetDateTime.now();

                if (rs.next()) return rs;
                return null;

            } catch (SQLException e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return null;
    }

    private static String validateAndSanitizeInput(String input) {
        // Entfernen ungültiger Zeichen
        input = input.replaceAll("\\s+", "");
        input = input.replaceAll("\\t", "");
        input = input.replaceAll("\\p{C}", "");

        // Ersetzen gefährlicher Zeichen durch sichere Zeichen
        input = input.replaceAll("'", "''");
        input = input.replaceAll("\"", "'");
        input = input.replaceAll(";", ";");
        input = input.replaceAll("%", "%%");

        return input;
    }

}
