package com.example.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

//        Class.forName("com.mysql.cj.jdbc.Driver");
//        /*
//        "jdbc:mysql://120.27.246.198:3306/self_database",
//         */
//        Connection connection = DriverManager.getConnection("jdbc:mysql://120.27.246.198:3306/self_database",
//            "root", "123456");
//
//        Statement statement=connection.createStatement();
//
//        ResultSet execute = statement.executeQuery("select * from oauth2_application_user");
//
//        System.out.println(execute);
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
