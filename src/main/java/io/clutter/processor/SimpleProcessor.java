package io.clutter.processor;

import io.clutter.javax.factory.ClassFactory;
import io.clutter.javax.factory.types.BoxedTypeFactory;
import io.clutter.model.classtype.ClassType;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static io.clutter.javax.extractor.Filters.CLASS;
import static io.clutter.javax.extractor.Filters.INTERFACE;
import static java.lang.String.format;
import static java.util.stream.Collectors.*;

/**
 * Simple implementation of {@link Processor}.<br>
 * <br>
 * <p>
 * In order to enable it create file under path (take care with directories):
 * <pre>
 *     src/resources/META-INF/services/javax.annotation.processing.Processor
 * </pre>
 * with content equal to canonical name of {@link Processor} instance, e.g.
 * <pre>
 *     io.clutter.processor.SimpleProcessor
 * </pre>
 * You may need to subclass this class and provide custom behaviour
 * {@see SimpleProcessor#process(Map, FileGenerator)} method
 * </p>
 * @see SimpleProcessor#process(Map, FileGenerator)
 */
public class SimpleProcessor extends AbstractProcessor {

    private static final String ANNOTATION_WILDCARD = "*";

    private final SourceVersion sourceVersion;
    private final Set<String> annotationTypes;

    @SafeVarargs
    public SimpleProcessor(SourceVersion version, Class<? extends Annotation>... annotations) {
        this.sourceVersion = Objects.requireNonNull(version, "Version may not be null");
        Objects.requireNonNull(annotations, "Annotations may not be null");

        if (annotations.length > 0) {
            this.annotationTypes = Stream.of(annotations)
                    .map(Class::getCanonicalName)
                    .collect(toSet());
        } else {
            this.annotationTypes = Set.of(ANNOTATION_WILDCARD);
        }
    }

    /**
     * Provides map with annotated classes for processing
     * and {@link FileGenerator} for generating new files.
     * <br><br>
     * Use {@link SimpleProcessor#logError(String)}, {@link SimpleProcessor#logWarn(String)} or {@link SimpleProcessor#logInfo(String)} to log on console.
     */
    public void process(Map<Class<? extends Annotation>, Set<ClassType>> aggregate, FileGenerator fileGenerator) {
        logInfo(format("Found %s files annotated with one of following annotations: %s", aggregate.size(), aggregate.keySet().stream().map(Class::getCanonicalName).collect(joining(", "))));
    }

    /**
     * Returns true if annotations should not be processed by subsequent annotation processors
     *
     * @see javax.annotation.processing.Processor#process(Set, RoundEnvironment)
     */
    protected boolean claim() {
        return false;
    }

    @Override
    final public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        try {
            process(aggregate(annotations, roundEnv), new FileGenerator(processingEnv.getFiler()));
        } catch (Exception e) {
            e.printStackTrace();
            logError("Annotation processing failed: " + e);
        }
        return claim();
    }

    @Override
    final public Set<String> getSupportedAnnotationTypes() {
        return annotationTypes;
    }

    @Override
    final public SourceVersion getSupportedSourceVersion() {
        return sourceVersion;
    }

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

    final protected void logError(String message) {
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    final protected void logWarn(String message) {
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message);
    }

    final protected void logInfo(String message) {
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    private Map<Class<? extends Annotation>, Set<ClassType>> aggregate(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return annotations
                .stream()
                .collect(toMap(
                        this::getAnnotationType,
                        annotation -> roundEnv
                                .getElementsAnnotatedWith(annotation)
                                .stream()
                                .filter(CLASS.or(INTERFACE))
                                .map(TypeElement.class::cast)
                                .map(ClassFactory::from)
                                .collect(toSet())));
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> getAnnotationType(TypeElement annotation) {
        return (Class<? extends Annotation>) BoxedTypeFactory
                .from(annotation.asType())
                .getType();
    }
}
