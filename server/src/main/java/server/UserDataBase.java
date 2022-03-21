package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataBase {
 private static Connection connection;
 private static Statement stmt;


    public static void main(String[] args) {

        try {
            connect();
            System.out.println("Connection ok");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }


    }

    public static void connect() throws Exception {
    Class.forName("org.sqlite.JDBC");
    connection= DriverManager.getConnection("jdbc:sqlite:auth.db");
    stmt=connection.createStatement();
    }

    public static void disconnect()  {
        try {
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
