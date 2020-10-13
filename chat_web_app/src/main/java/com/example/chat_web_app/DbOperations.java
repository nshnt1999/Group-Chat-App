package com.example.chat_web_app;

import java.sql.*;

public class DbOperations {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if(connection==null){
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/messenger","root","abc_12345");
        }
        return connection;
    }


    public static void addUserInDb(String user) throws SQLException {
        getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users VALUES (null, ?, ?)");
        preparedStatement.setString(1, user);
        preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
        int rows_affected = preparedStatement.executeUpdate();

        if(rows_affected > 0){
            System.out.println("succesfully inserted the msg in DB");
        }else{
            System.out.println("unable to insert the msg in DB");
        }
    }


    public static void chatBackUp(String user,String msg_id,String msg) throws SQLException {
        getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat_backup VALUES (?, ?, ?)");
        preparedStatement.setString(1, msg_id);
        preparedStatement.setString(2, user);
        preparedStatement.setString(3, msg);
        int rows_affected = preparedStatement.executeUpdate();

        if(rows_affected > 0){
            System.out.println("succesfully inserted the msg in DB");
        }else{
            System.out.println("unable to insert the msg in DB");
        }
    }






}
