package fr.awildelephant.rdbms.evaluator;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.ColumnReference;

public interface Values {

    DomainValue valueOf(ColumnReference column);
}
