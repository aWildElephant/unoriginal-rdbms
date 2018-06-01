package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.server.RDBMS;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class RDBMSDriver implements Driver {

    private static final int MAJOR_VERSION = 0;
    private static final int MINOR_VERSION = 0;

    private final RDBMS system = new RDBMS();

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            throw new SQLException("Invalid URL");
        }

        return new RDBMSConnection(system);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (url == null) {
            throw new SQLException("URL cannot be null");
        }

        return url.startsWith("client:rdbms:mem:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return false; // TODO: pass the JDBC compliance test ;)
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
