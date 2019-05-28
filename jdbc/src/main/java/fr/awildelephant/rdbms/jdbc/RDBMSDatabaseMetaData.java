package fr.awildelephant.rdbms.jdbc;

public class RDBMSDatabaseMetaData extends AbstractDatabaseMetaData {

    private static final RDBMSDatabaseMetaData META_DATA = new RDBMSDatabaseMetaData();

    static RDBMSDatabaseMetaData getMetaData() {
        return META_DATA;
    }

    @Override
    public String getDatabaseProductName() {
        return "RDBMS";
    }

    @Override
    public int getDatabaseMajorVersion() {
        return 1;
    }

    @Override
    public int getDatabaseMinorVersion() {
        return 0;
    }

    @Override
    public String getDatabaseProductVersion() {
        return getDatabaseMajorVersion() + "." + getDatabaseMinorVersion();
    }

    @Override
    public String getDriverName() {
        return AbstractRDBMSDriver.class.getName();
    }

    @Override
    public int getDriverMajorVersion() {
        return getDatabaseMajorVersion();
    }

    @Override
    public int getDriverMinorVersion() {
        return getDatabaseMinorVersion();
    }

    @Override
    public String getDriverVersion() {
        return getDriverMajorVersion() + "." + getDriverMinorVersion();
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() {
        return false;
    }

    @Override
    public boolean supportsMultipleResultSets() {
        return false;
    }

    @Override
    public boolean supportsSchemasInDataManipulation() {
        return false;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() {
        return false;
    }

    @Override
    public boolean supportsStoredProcedures() {
        return false;
    }
}
