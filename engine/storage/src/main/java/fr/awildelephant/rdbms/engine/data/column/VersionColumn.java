package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.version.Version;

import static fr.awildelephant.rdbms.data.value.VersionValue.versionValue;

public final class VersionColumn extends ObjectColumn<Version> implements WriteableColumn {

    public VersionColumn(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public void set(int index, DomainValue value) {
        backingArray.set(index, extract(value));
    }

    @Override
    protected DomainValue transform(Version value) {
        return versionValue(value);
    }

    @Override
    protected Version extract(DomainValue value) {
        return value.getVersion();
    }
}
