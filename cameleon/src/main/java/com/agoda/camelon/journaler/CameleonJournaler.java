package com.agoda.camelon.journaler;

import java.sql.SQLWarning;

public interface CameleonJournaler {

    void onBegin(String methodName, String sql, String[] parameters);
    void onSuccess(String methodName, int updatedRows, String sql, String[] parameters);
    void onFailure(String methodName, Exception ex,String sql, String[] parameters);
    void onCommit(SQLWarning warning);
    void onRollback(SQLWarning warning);
}
