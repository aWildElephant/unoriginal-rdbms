package fr.awildelephant.rdbms.version;

public final class EndOfTimesVersion implements Version {

    private static final Version INSTANCE = new EndOfTimesVersion();

    private EndOfTimesVersion() {

    }

    public static Version getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isAfter(Version version) {
        return true;
    }
}
