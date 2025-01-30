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
        // prepare
        PrepareEnv();
        // this won't log
        SelectQuery();
        // this will log "commit"
        TryModify();
        // this will log rolled back
        TryRolledBack();
    }

    private static Connection getConnection() throws SQLException {
      return  DriverManager.getConnection("jdbc:cameleon://localhost:1433;databaseName=master;encrypt=false","sa","agoda123*");
    }

    private static void PrepareEnv() {
        try(var connection  = getConnection()){
            var statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS dbo.example;CREATE TABLE dbo.example (\n" +
                    "    name NVARCHAR(255) NOT NULL\n" +
                    ");");
            statement.execute("INSERT INTO dbo.example (name) VALUES ('test')");
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private static void SelectQuery(){
        try(var connection  = getConnection()){
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from dbo.example");
            while(resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void TryModify(){
        try(var connection  = getConnection()){
            var statement = connection.createStatement();
            statement.executeUpdate("insert into dbo.example (name) values ('aaa')");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void TryRolledBack()
    {
        var thread = new Thread(() -> {
            try(var connection  = getConnection()){
                var statement = connection.createStatement();
                statement.execute("BEGIN TRANSACTION;\n" +
                        "UPDATE dbo.example SET name = 'Transaction1' WHERE name like 'aaa';\n" +
                        "WAITFOR DELAY '00:00:10';\n" +
                        "UPDATE dbo.example SET name = 'Transaction1' WHERE name like 'aaa';\n" +
                        "COMMIT TRANSACTION;");
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        thread.start();
        try(var connection  = getConnection()) {
            var statement = connection.createStatement();
            statement.execute("BEGIN TRANSACTION;\n" +
                    "UPDATE dbo.example SET name = 'Transaction1' WHERE name like 'aaa';\n" +
                    "WAITFOR DELAY '00:00:10';\n" +
                    "UPDATE dbo.example SET name = 'Transaction1' WHERE name like 'aaa';\n" +
                    "COMMIT TRANSACTION;");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}