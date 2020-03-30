package io.clutter.writer;

import java.util.List;

public interface Header {

    Header addImport(Class<?> clazz);

    Header addStaticImport(Class<?> clazz);

    Header setComment(Comment comment);

    Body addBody(List<String> body);
}
