package com.agoda.camelon;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class ProxyDriver  implements Driver {

    public static final String JDBC_DRIVER_PREFIX = "jdbc:cameleon:";
    private final Driver realDriver;

    public ProxyDriver(Driver realDriver) {
        this.realDriver = realDriver;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if(!acceptsURL(url))
            throw new SQLException("Not a valid URL, only accepts jdbc:cameleon");
        var realUrl = url.replace(JDBC_DRIVER_PREFIX,"jdbc:sqlserver:");
        return new ProxyConnection(realDriver.connect(realUrl, info));
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(JDBC_DRIVER_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return realDriver.getPropertyInfo(url,info);
    }

    @Override
    public int getMajorVersion() {
        return realDriver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return realDriver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return realDriver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return realDriver.getParentLogger();
    }
}
