package fr.awildelephant.rdbms.jdbc.abstraction;

public interface ServerProxy {

    ResultProxy execute(String query);
}
