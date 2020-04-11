package io.clutter.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.Set;

abstract class AbstractSimpleProcessor extends AbstractProcessor {

    @Override
    final public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return super.getCompletions(element, annotation, member, userText);
    }

    @Override
    final public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    final public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    final protected synchronized boolean isInitialized() {
        return super.isInitialized();
    }
}
