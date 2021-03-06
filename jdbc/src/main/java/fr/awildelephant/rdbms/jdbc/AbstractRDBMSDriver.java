package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ServerProxy;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class AbstractRDBMSDriver implements Driver {

    private static final int MAJOR_VERSION = 0;
    private static final int MINOR_VERSION = 0;

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            throw new SQLException("Invalid URL");
        }

        return new RDBMSConnection(createProxy(url));
    }

    abstract protected ServerProxy createProxy(String url);

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (url == null) {
            throw new SQLException("URL cannot be null");
        }

        return acceptsNotNullURL(url);
    }

    abstract protected boolean acceptsNotNullURL(String url);

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
