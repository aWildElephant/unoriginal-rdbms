package fr.awildelephant.rdbms.engine.bitmap;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BitSetBackedBitmapTest {

    @Test
    void nextSetBit_should_include_given_index() {
        final Bitmap bitmap = new BitSetBackedBitmap(3);
        bitmap.set(0);
        bitmap.set(2);
        assertThat(bitmap.nextSetBit(0)).isEqualTo(0);
    }

    @Test
    void getBySetBitIndex_should_get_the_first_set_bit() {
        final Bitmap bitmap = new BitSetBackedBitmap(2);
        bitmap.set(0);
        assertThat(bitmap.getBySetBitIndex(0)).isEqualTo(0);
    }

    @Test
    void getBySetBitIndex_should_get_the_third_set_bit() {
        final Bitmap bitmap = new BitSetBackedBitmap(5);
        bitmap.set(0);
        bitmap.set(1);
        bitmap.set(4);
        assertThat(bitmap.getBySetBitIndex(2)).isEqualTo(4);
    }

    @Test
    void getBySetBitIndex_should_return_minus_one_if_not_found() {
        final Bitmap bitmap = new BitSetBackedBitmap(2);
        bitmap.set(1);
        assertThat(bitmap.getBySetBitIndex(1)).isEqualTo(-1);
    }
}
