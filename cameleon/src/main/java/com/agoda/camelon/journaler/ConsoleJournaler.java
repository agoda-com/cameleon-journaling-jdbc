package com.agoda.camelon.journaler;

import java.io.Console;

public class ConsoleJournaler implements ICameleonJournaler {

    @Override
    public void onCommit(String methodName, int updatedRows, String sql, String[] parameters) {
        System.out.println(methodName + ": " + updatedRows + " rows updated");
    }

    @Override
    public void onRollback(String methodName, String sql, String[] parameters) {
        System.out.println(methodName + ": " + sql + " rolled back");
    }
}

