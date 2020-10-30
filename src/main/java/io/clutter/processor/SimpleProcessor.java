package io.clutter.processor;

import io.clutter.javax.factory.JxClassAdapter;
import io.clutter.javax.factory.JxBoxedTypeAdapter;
import io.clutter.model.clazz.Construct;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.*;

/**
 * Base implementation of {@link Processor}.<br>
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
 * </p>
 *
 * @see SimpleProcessor#process(Map, SourceFileGenerator)
 */
public class SimpleProcessor extends AbstractProcessor {

    private static final String ANNOTATION_WILDCARD = "*";

    private final SourceVersion sourceVersion;
    private final Set<String> annotationTypes;

    public SimpleProcessor(SourceVersion version, Set<Class<? extends Annotation>> annotations) {
        this.sourceVersion = requireNonNull(version, "Version may not be null");
        requireNonNull(annotations, "Annotations may not be null");

        this.annotationTypes = annotations.isEmpty()
            ? Set.of(ANNOTATION_WILDCARD)
            : annotations
            .stream()
            .map(Class::getCanonicalName)
            .collect(toSet());
    }

    /**
     * Provides map with annotated classes for processing
     * and {@link SourceFileGenerator} for generating new files.
     * <br><br>
     * Use {@link SimpleProcessor#logError(String)}, {@link SimpleProcessor#logWarn(String)} or {@link SimpleProcessor#logInfo(String)} to log on console.
     */
    public void process(Map<Class<? extends Annotation>, Set<Construct>> aggregate, SourceFileGenerator sourceFileGenerator) {
        logInfo(format("Found %s files annotated with one of following annotations: %s",
            aggregate.size(),
            aggregate.keySet().stream().map(Class::getCanonicalName).collect(joining(", "))
        ));
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
            process(aggregate(annotations, roundEnv), new SourceFileGenerator(processingEnv.getFiler()));
        } catch (Exception e) {
            e.printStackTrace();
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

    private Map<Class<? extends Annotation>, Set<Construct>> aggregate(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return annotations
            .stream()
            .collect(toMap(
                this::getAnnotationType,
                annotation -> collectClasses(roundEnv, annotation)
            ));
    }

    private Set<Construct> collectClasses(RoundEnvironment roundEnv, TypeElement annotation) {
        return roundEnv
            .getElementsAnnotatedWith(annotation)
            .stream()
            .filter(element -> element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE)
            .map(TypeElement.class::cast)
            .map(JxClassAdapter::from)
            .collect(toSet());
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> getAnnotationType(TypeElement annotation) {
        return (Class<? extends Annotation>) JxBoxedTypeAdapter.from(annotation.asType()).getType();
    }
}
