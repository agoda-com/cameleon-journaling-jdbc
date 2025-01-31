package com.agoda.camelon.journaler;

import java.io.Console;
import java.sql.SQLWarning;

public class ConsoleJournaler implements CameleonJournaler {

    @Override
    public void onBegin(String methodName, String sql, String[] parameters) {
        System.out.println("Begin " + methodName + ": " + sql);
    }

    @Override
    public void onSuccess(String methodName, int updatedRows, String sql, String[] parameters) {
        System.out.println("Success "+methodName + ": " + updatedRows + " rows updated on > "+ sql);
    }

    @Override
    public void onFailure(String methodName, Exception ex, String sql, String[] parameters) {
        System.err.println("Failure "+methodName + ": " + sql + " rolled back caused by: " + ex.getLocalizedMessage());
    }

    @Override
    public void onCommit(String sql) {
        System.out.println("Commit " + sql);
    }

    @Override
    public void onRollback(String sql) {
        System.err.println("Rollback " + sql);
    }

}

