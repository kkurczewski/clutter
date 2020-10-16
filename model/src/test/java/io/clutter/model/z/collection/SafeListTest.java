package io.clutter.model.z.collection;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SafeListTest {

    @Test
    void allowBasicMutationsForNonEmptyAnnotationTarget() {
        var safeList = new SafeList<>(List.of("foo"));
        assertThat(safeList.getValues()).containsExactly("foo");

        safeList.modify(List::clear);
        assertThat(safeList.getValues()).isEmpty();
    }

    @Test
    void allowBasicMutationsForEmptyAnnotationTarget() {
        var safeList = new SafeList<>(new LinkedList<Object>());
        safeList.modify(add -> add.add("foo"));
        assertThat(safeList.getValues()).containsExactly("foo");
    }

    @Test
    void doNotAllowGetterMutations() {
        var safeList = new SafeList<>(List.of("foo"));
        assertThatThrownBy(() -> safeList.getValues().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }
}