package com.agoda.camelon.journaler;

public interface CameleonJournaler {

    void onBegin(String methodName, String sql, String[] parameters);
    void onCommit(String methodName, int updatedRows, String sql, String[] parameters);
    void onRollback(String methodName, String sql, String[] parameters);
}
