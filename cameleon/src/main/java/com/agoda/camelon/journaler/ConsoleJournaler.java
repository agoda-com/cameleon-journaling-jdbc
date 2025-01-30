package com.agoda.camelon.journaler;

import java.io.Console;

public class ConsoleJournaler implements CameleonJournaler {

    @Override
    public void onBegin(String methodName, String sql, String[] parameters) {
        System.out.println("Begin " + methodName + ": " + sql);
    }

    @Override
    public void onCommit(String methodName, int updatedRows, String sql, String[] parameters) {
        System.out.println("Commit "+methodName + ": " + updatedRows + " rows updated on > "+ sql);
    }

    @Override
    public void onRollback(String methodName, String sql, String[] parameters) {
        System.out.println("Rollback "+methodName + ": " + sql + " rolled back");
    }

}

