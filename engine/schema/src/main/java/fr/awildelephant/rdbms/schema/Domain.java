package fr.awildelephant.rdbms.schema;

public enum Domain {
    BOOLEAN {
        @Override
        public boolean canBeUsedAs(Domain domain) {
            return domain == BOOLEAN;
        }
    },
    DATE {
        @Override
        public boolean canBeUsedAs(Domain domain) {
            return domain == DATE;
        }
    },
    DECIMAL {
        @Override
        public boolean canBeUsedAs(Domain domain) {
            return domain == DECIMAL;
        }
    },
    INTEGER {
        @Override
        public boolean canBeUsedAs(Domain domain) {
            return domain == INTEGER || domain == DECIMAL;
        }
    },
    INTERVAL {
        @Override
        public boolean canBeUsedAs(Domain domain) {
            return domain == INTERVAL;
        }
    },
    NULL {
        @Override
        public boolean canBeUsedAs(Domain domain) {
            return true;
        }
    },
    TEXT {
        @Override
        public boolean canBeUsedAs(Domain domain) {
            return domain == TEXT;
        }
    };

    public abstract boolean canBeUsedAs(Domain domain);
}
