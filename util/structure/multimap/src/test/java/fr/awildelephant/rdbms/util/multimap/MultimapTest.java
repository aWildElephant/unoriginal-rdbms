package fr.awildelephant.rdbms.util.multimap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MultimapTest {

    private Multimap<String, String> multimap;

    @BeforeEach
    void setUp() {
        multimap = new Multimap<>();
    }

    @Test
    void it_should_add_a_single_element_to_the_values() {
        multimap.putSingle("hello", "there");
        multimap.putSingle("hello", "world");

        assertThat(multimap.get("hello")).containsExactly("there", "world");
    }

    @Test
    void it_should_remove_a_single_element_and_return_true_because_there_was_another_one() {
        multimap.put("hello", List.of("there", "world"));
        boolean result = multimap.removeSingle("hello", "world");

        assertThat(result).isTrue();
    }

    @Test
    void it_should_remove_the_last_element_and_return_true() {
        multimap.put("hello", List.of("there", "world"));
        multimap.removeSingle("hello", "world");
        boolean result = multimap.removeSingle("hello", "there");

        assertThat(result).isFalse();
    }
}