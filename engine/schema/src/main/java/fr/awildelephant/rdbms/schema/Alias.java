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

    public String get(String name) {
        final String alias = aliases.get(name);

        if (alias == null) {
            return name;
        }

        return alias;
    }

    public String revert(String name) {
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (name.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return name;
    }
}
