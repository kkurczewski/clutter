package io.clutter.printer;

import io.clutter.model.file.Comment;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    public void shouldCreateJavadoc() {
        Comment comment = Comment.javadoc("foo", "bar", "baz");
        assertThat(comment.getLines()).containsExactly(
                "/**",
                " * foo",
                " * bar",
                " * baz",
                " */"
        );
    }

    @Test
    public void shouldCreateComment() {
        Comment comment = Comment.comment("foo", "bar", "baz");
        assertThat(comment.getLines()).containsExactly(
                "// foo",
                "// bar",
                "// baz"
        );
    }

    @Test
    public void shouldCreateMultiLineComment() {
        Comment comment = Comment.multiLineComment("foo", "bar", "baz");
        assertThat(comment.getLines()).containsExactly(
                "/*",
                " foo",
                " bar",
                " baz",
                " */"
        );
    }
}