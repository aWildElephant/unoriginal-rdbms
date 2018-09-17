package fr.awildelephant.rdbms.schema;

import java.util.Map;

public final class Alias {

    private final Map<String, String> aliases;

    private Alias(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    public static Alias alias(Map<String, String> aliases) {
        return new Alias(aliases);
    }

    public String get(String original) {
        final String alias = aliases.get(original);

        if (alias == null) {
            return original;
        }

        return alias;
    }
}
