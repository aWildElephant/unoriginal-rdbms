package fr.awildelephant.rdbms.client;

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
    public int getDriverMajorVersion() {
        return 1;
    }

    @Override
    public int getDriverMinorVersion() {
        return 0;
    }
}
