package fr.awildelephant.rdbms.schema;

import java.util.HashMap;

public final class Alias {

    private final HashMap<String, String> aliases;

    private Alias(HashMap<String, String> aliases) {
        this.aliases = aliases;
    }

    public static Alias alias(HashMap<String, String> aliases) {
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
