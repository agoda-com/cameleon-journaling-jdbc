package com.agoda;

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
    }

    private static Connection getConnection() throws SQLException {
      return  DriverManager.getConnection("jdbc:cameleon://localhost:1433;databaseName=master;encrypt=false","sa","agoda123*");
    }

    private static void PrepareEnv() {
        try(var connection  = getConnection()){
            var statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS dbo.example");
            statement.execute("CREATE TABLE dbo.example (\n" +
                    "    id INT PRIMARY KEY,\n" +
                    "    name VARCHAR(255))");
            statement.execute("CREATE PROCEDURE sp_deadlock_sim\n" +
                    "\t@Id INT\n" +
                    "AS\n" +
                    "BEGIN\t\n" +
                    "\tINSERT INTO dbo.example (id, name)\n" +
                    "    VALUES (@Id,N'hjk');\n" +
                    "\tIF @Id =88\n" +
                    "\tBEGIN\t\n" +
                    "EXEC sys.sp_addmessage\n" +
                    "    @msgnum = 60000,\n" +
                    "    @severity = 16,\n" +
                    "    @msgtext = N'This is a test message with one numeric parameter (%d), one string parameter (%s), and another string parameter (%s).',\n" +
                    "    @lang = 'us_english';\n" +
                    "\n" +
                    "DECLARE @msg NVARCHAR(2048) = FORMATMESSAGE(60000, 500, N'First string', N'second string');\n" +
                    "\n" +
                    "THROW 60000, @msg, 1;\n" +
                    "\t\t\n" +
                    "\tEND\t\n" +
                    "END\n");
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
        var latch = new CountDownLatch(1);

        var thread1 = new Thread(() -> ExecuteDeadlock(latch,8));
        var thread2 = new Thread(() -> ExecuteDeadlock(latch,88));
        thread2.start();
        thread1.start();
    }

    private static void ExecuteDeadlock(CountDownLatch latch, int threadId) {
        try ( var connection = getConnection();
              var statement = connection.createStatement()) {
            System.out.println("Thread " + threadId + " started.");
            statement.execute( "EXEC [dbo].[sp_deadlock_sim] @Id="+threadId+";");
            System.out.println("Thread " + threadId + " completed.");

        } catch (SQLException e) {
            System.err.println("Thread " + threadId + " failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}