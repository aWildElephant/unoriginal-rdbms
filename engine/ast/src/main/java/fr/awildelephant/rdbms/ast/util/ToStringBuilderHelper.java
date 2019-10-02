package fr.awildelephant.rdbms.ast.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class ToStringBuilderHelper {

    private ToStringBuilderHelper() {

    }

    public static ToStringBuilder toStringBuilder(Object instance) {
        return new ToStringBuilder(instance, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
