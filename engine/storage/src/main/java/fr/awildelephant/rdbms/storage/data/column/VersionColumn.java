package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.version.Version;

import static fr.awildelephant.rdbms.data.value.VersionValue.versionValue;

public final class VersionColumn extends ObjectColumn<Version> implements WriteableColumn {

    public VersionColumn(final int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public void set(final int index, final DomainValue value) {
        backingArray.set(index, extract(value));
    }

    public void setGeneric(final int index, final Version value) {
        backingArray.set(index, value);
    }

    @Override
    protected DomainValue transform(final Version value) {
        return versionValue(value);
    }

    @Override
    protected Version extract(final DomainValue value) {
        return value.getVersion();
    }
}
