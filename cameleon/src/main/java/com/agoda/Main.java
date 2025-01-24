package com.agoda;

import com.agoda.camelon.ProxyDriver;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new ProxyDriver(new SQLServerDriver()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try(var connection  = DriverManager.getConnection(ProxyDriver.JDBC_DRIVER_PREFIX+"//localhost:1433;databaseName=master;encrypt=false","sa","agoda123*")){
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from dbo.example");
            while(resultSet.next()){
                System.out.println(resultSet.getString("name"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}