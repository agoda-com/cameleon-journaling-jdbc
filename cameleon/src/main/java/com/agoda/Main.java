package com.agoda;

import com.agoda.camelon.ProxyConnection;
import com.agoda.camelon.ProxyDriver;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.util.concurrent.CountDownLatch;

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
        System.out.flush();
        System.err.flush();
    }

    private static ProxyConnection getConnection() throws SQLException {
      return  (ProxyConnection)DriverManager.getConnection("jdbc:cameleon://localhost:1433;databaseName=master;encrypt=false","sa","agoda123*");
    }

    private static void PrepareEnv() {
        try(var connection  = getConnection()){
            var statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS dbo.example");
            statement.execute("CREATE TABLE dbo.example (\n" +
                    "    id INT PRIMARY KEY,\n" +
                    "    name NVARCHAR(50)\n" +
                    ");");
            statement.execute("IF EXISTS (SELECT 1 FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.sp_failed_transaction') AND type = N'P')\n" +
                    "BEGIN\n" +
                    "    DROP PROCEDURE dbo.sp_failed_transaction;\n" +
                    "END");
            statement.execute("CREATE PROCEDURE sp_failed_transaction\n" +
                    "\t@Id INT,\n" +
                    "\t@Name NVARCHAR(50),\n" +
                    "\t@Id2 INT,\n" +
                    "\t@Name2 NVARCHAR(50)\n" +
                    "AS\n" +
                    "BEGIN\n" +
                    "\tINSERT INTO dbo.example (id, name) VALUES (@Id,@Name),(@Id2,@Name2);\n" +
                    "END;");
            statement.execute("INSERT INTO dbo.example (id, name) VALUES (1,'lorem ipsum');");
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
            statement.executeUpdate("insert into dbo.example (id,name) values ('10','aaa')");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void TryRolledBack()
    {
        ExecuteSP("EXEC [dbo].[sp_failed_transaction] 8, N'transaction1',89, N'transaction1';",8);
        ExecuteSP("EXEC [dbo].[sp_failed_transaction] 90, N'transaction2',89, N'transaction2';",88);
    }

    private static void ExecuteSP(String sql, int threadId) {
        try (var connection = getConnection()){
            try (var statement = connection.createStatement()) {
                System.out.println("Thread " + threadId + " started.");
                if(statement.execute(sql)) {
                   var resultSet = statement.getResultSet();
                   while(resultSet.next()){
                       System.out.println(resultSet.getString("id")
                       +":"+resultSet.getString("name"));
                   };
                }
                System.out.println("Thread " + threadId + " completed.");
            } catch (Exception e) {
                System.err.println("Thread " + threadId + " failed: " + e.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}