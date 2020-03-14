package io.clutter.writer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentsTest {

    @Test
    void shouldCreateJavadoc() {
        Comments comments = Comments.javadoc("foo", "bar", "baz");
        assertThat(comments.getLines()).containsExactly(
                "/**",
                " * foo",
                " * bar",
                " * baz",
                " */"
        );
    }

    @Test
    void shouldCreateComment() {
        Comments comments = Comments.comment("foo", "bar", "baz");
        assertThat(comments.getLines()).containsExactly(
                "// foo",
                "// bar",
                "// baz"
        );
    }

    @Test
    void shouldCreateMultiLineComment() {
        Comments comments = Comments.multiLineComment("foo", "bar", "baz");
        assertThat(comments.getLines()).containsExactly(
                "/*",
                " foo",
                " bar",
                " baz",
                " */"
        );
    }
}