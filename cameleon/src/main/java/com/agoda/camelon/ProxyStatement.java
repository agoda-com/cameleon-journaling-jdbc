package com.agoda.camelon;

import com.agoda.camelon.journaler.ConsoleJournaler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProxyStatement implements Statement {
    private final Statement statement;
    private final ConsoleJournaler changeEventCapture = new ConsoleJournaler();
    public ProxyStatement(Statement statement) {
        this.statement = statement;
    }
    public final List<SQLException> Exceptions = new ArrayList<>();
    private final List<String> ExecuteSqls = new ArrayList<>();

    @Override
    public String toString() {
        return ExecuteSqls.stream().collect(Collectors.joining("\n"));
    }
    @Override
    public boolean execute(String sql) throws SQLException {
        boolean result = false;
        try {
            ExecuteSqls.add(sql);
            changeEventCapture.onBegin("excute", sql,new String[0]);
            result =statement.execute(sql);
            var updateCount= statement.getUpdateCount();
            changeEventCapture.onSuccess("execute", updateCount, sql, new String[0]);
        }catch (SQLException sqlException)
        {
            Exceptions.add(sqlException);
            changeEventCapture.onFailure("execute", sqlException,sql, new String[0]);
            throw sqlException;
        }
        return result;
    }


    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        ResultSet result = null;
        try {
            ExecuteSqls.add(sql);
            changeEventCapture.onBegin("executeQuery", sql,new String[0]);
            result =statement.executeQuery(sql);
            var updateCount= statement.getUpdateCount();
            changeEventCapture.onSuccess("executeQuery", updateCount, sql, new String[0]);
        }catch (SQLException sqlException)
        {
            Exceptions.add(sqlException);
            changeEventCapture.onFailure("executeQuery", sqlException,sql, new String[0]);
            throw sqlException;
        }
        return result;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        int result = -1;
        try {
            ExecuteSqls.add(sql);
            changeEventCapture.onBegin("executeUpdate", sql,new String[0]);
            result =statement.executeUpdate(sql);
            var updateCount= statement.getUpdateCount();
            changeEventCapture.onSuccess("executeUpdate", updateCount, sql, new String[0]);
        }catch (SQLException sqlException)
        {
            Exceptions.add(sqlException);
            changeEventCapture.onFailure("executeUpdate", sqlException,sql, new String[0]);
            throw sqlException;
        }
        return result;
    }

    @Override
    public void close() throws SQLException {
        statement.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return statement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        statement.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return statement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        statement.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        statement.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return statement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        statement.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        statement.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return statement.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        statement.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        statement.setCursorName(name);
    }
    @Override
    public ResultSet getResultSet() throws SQLException {
        return statement.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return statement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return statement.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        statement.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return statement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        statement.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return statement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return statement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return statement.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        statement.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        statement.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return statement.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return statement.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return statement.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return statement.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return statement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {

        return statement.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return statement.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return statement.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return statement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return statement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        statement.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return statement.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        statement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return statement.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
