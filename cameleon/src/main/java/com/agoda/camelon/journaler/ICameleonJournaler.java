package com.agoda.camelon.journaler;

public interface ICameleonJournaler {

    void onCommit(String methodName, int updatedRows, String sql, String[] parameters);
    void onRollback(String methodName, String sql, String[] parameters);
}
