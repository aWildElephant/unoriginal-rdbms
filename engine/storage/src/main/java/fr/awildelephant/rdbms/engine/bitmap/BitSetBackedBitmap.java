package fr.awildelephant.rdbms.engine.bitmap;

import java.util.BitSet;

public class BitSetBackedBitmap implements Bitmap {

    private final BitSet bitSet;

    public BitSetBackedBitmap(int size) {
        this.bitSet = new BitSet(size);
    }

    @Override
    public void set(int index) {
        bitSet.set(index);
    }

    @Override
    public boolean get(int index) {
        return bitSet.get(index);
    }

    @Override
    public int cardinality() {
        return bitSet.cardinality();
    }

    @Override
    public int nextSetBit(int fromIndex) {
        return bitSet.nextSetBit(fromIndex);
    }

    @Override
    public int getBySetBitIndex(int index) {
        int setBitCount = 0;
        int current = -1;

        do {
            current = nextSetBit(current + 1);
            if (current < 0) {
                return -1;
            }
            setBitCount++;
        } while (setBitCount <= index);

        return current;
    }
}
