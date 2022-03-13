package fr.awildelephant.rdbms.engine.bitmap;

/**
 * We add our own interface over java.util's BitSet to make it easier to change the implementation.
 */
public interface Bitmap {

    void set(int index);

    boolean get(int index);

    int cardinality();

    int nextSetBit(int from);

    /**
     * Return the index of the nth set bit in the bitmap.
     *
     * @return the index of this bit, or -1 if no such bit exists
     */
    int getBySetBitIndex(int index);
}
