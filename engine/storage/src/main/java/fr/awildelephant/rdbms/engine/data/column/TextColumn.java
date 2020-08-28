package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.TextValue.textValue;

public final class TextColumn extends ObjectColumn<String> {

    public TextColumn(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    protected DomainValue transform(String value) {
        return textValue(value);
    }

    @Override
    protected String extract(DomainValue value) {
        return value.getString();
    }
}
