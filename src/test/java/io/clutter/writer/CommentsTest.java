package io.clutter.writer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentsTest {

    @Test
    public void shouldCreateJavadoc() {
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
    public void shouldCreateComment() {
        Comments comments = Comments.comment("foo", "bar", "baz");
        assertThat(comments.getLines()).containsExactly(
                "// foo",
                "// bar",
                "// baz"
        );
    }

    @Test
    public void shouldCreateMultiLineComment() {
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