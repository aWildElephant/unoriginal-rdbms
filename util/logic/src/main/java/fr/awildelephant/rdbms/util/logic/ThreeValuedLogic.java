package fr.awildelephant.rdbms.util.logic;

import java.util.function.Supplier;

public enum ThreeValuedLogic {

    TRUE {
        @Override
        public ThreeValuedLogic negate() {
            return FALSE;
        }

        @Override
        public ThreeValuedLogic and(Supplier<ThreeValuedLogic> other) {
            return other.get();
        }

        @Override
        public ThreeValuedLogic or(Supplier<ThreeValuedLogic> other) {
            return TRUE;
        }
    },
    FALSE {
        @Override
        public ThreeValuedLogic negate() {
            return TRUE;
        }

        @Override
        public ThreeValuedLogic and(Supplier<ThreeValuedLogic> other) {
            return FALSE;
        }

        @Override
        public ThreeValuedLogic or(Supplier<ThreeValuedLogic> other) {
            return other.get();
        }
    },
    UNKNOWN {
        @Override
        public ThreeValuedLogic negate() {
            return UNKNOWN;
        }

        @Override
        public ThreeValuedLogic and(Supplier<ThreeValuedLogic> other) {
            return UNKNOWN;
        }

        @Override
        public ThreeValuedLogic or(Supplier<ThreeValuedLogic> other) {
            return other.get();
        }
    };

    public abstract ThreeValuedLogic negate();

    public abstract ThreeValuedLogic and(final Supplier<ThreeValuedLogic> other);

    public abstract ThreeValuedLogic or(final Supplier<ThreeValuedLogic> other);
}
