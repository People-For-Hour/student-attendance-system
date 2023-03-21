package lk.ijse.dep10.jdbc.db;

import javafx.scene.control.Alert;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;
    private DBConnection(){

        try {
            FileReader fr = new FileReader(new File("application.properties"));
            Properties properties = new Properties();
            properties.load(fr);
            fr.close();

            String host = properties.getProperty("mysql.host","localhost");
            String port = properties.getProperty("mysql.port","3306");
            String database = properties.getProperty("mysql.database", "students_attendance");
            String username = properties.getProperty("mysql.username", "root");
            String password = properties.getProperty("mysql.password", "");

            String url = "jdbc:mysql://"+host+":"+port+"/"+database+"?createDatabaseIfNotExist=true&allowMultiQueries=true";
            connection = DriverManager.getConnection(url,username,password);

        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Configuration file does not exist").showAndWait();
            throw new RuntimeException(e);
        }catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to read configuration.").showAndWait();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to create connection. Try again, if the problem persist please contact the technical team.").showAndWait();
            throw new RuntimeException(e);
        }
    }
    public static DBConnection getInstance(){
        return (dbConnection == null) ? dbConnection = new DBConnection() : dbConnection;
    }
    public Connection connection(){
        return connection;
    }
}
