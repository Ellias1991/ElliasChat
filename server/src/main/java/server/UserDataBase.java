package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataBase implements AuthService {
    private static Connection connection;
    private static Statement stmt;


    public static void main(String[] args)  {

        try {
            WorkWithBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
        public UserDataBase() {
            try {
                connect();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        public static void connect () throws Exception {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:auth.db");
            stmt = connection.createStatement();
        }

        public static void disconnect () {
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



        @Override
        public String getNicknameByLoginAndPassword (String login, String password){


            return null;
        }

        @Override
        public boolean registration (String login, String password, String nickname){
            return false;
        }

        public static void WorkWithBase()throws SQLException {
            stmt.executeUpdate("INSERT INTO Users (nickname, age) VALUES ('Jenya',30);");

        }
    }


