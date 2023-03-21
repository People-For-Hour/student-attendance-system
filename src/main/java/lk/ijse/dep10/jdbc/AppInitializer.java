package lk.ijse.dep10.jdbc;

import javafx.application.Application;
import javafx.stage.Stage;
import lk.ijse.dep10.jdbc.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                System.out.println("Database connection is about to close.");
                if(DBConnection.getInstance().connection()!=null &&
                               !DBConnection.getInstance().connection().isClosed()){
                    DBConnection.getInstance().connection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        generateSchemaIfNotExist();
    }

    private void generateSchemaIfNotExist() {
        try {
            Connection connection = DBConnection.getInstance().connection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SHOW TABLES ");

            HashSet<String> tableSet = new HashSet<>();
            while (rst.next()){
                tableSet.add(rst.getString(1));
            }
            boolean tableExist = tableSet.contains(Set.of("Attendance", "Picture", "Student", "User"));
            if (!tableExist) {
                System.out.println("Schema is about to auto generate.");
                stm.execute(readSchemaScript());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private String readSchemaScript(){
        InputStream is = getClass().getResourceAsStream("/schema.sql");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            String line;
            StringBuilder dbScript = new StringBuilder();
            while ((line=br.readLine())!=null){
                dbScript.append(line).append("\n");
            }
            return dbScript.toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
