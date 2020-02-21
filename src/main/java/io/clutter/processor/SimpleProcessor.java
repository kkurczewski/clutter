package io.clutter.processor;

import io.clutter.processor.extractor.TypeExtractor;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.ValidationOutput;
import io.clutter.processor.validator.exception.ValidationFailed;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class SimpleProcessor extends AbstractProcessor {

    private static final String ANNOTATION_WILDCARD = "*";

    private final SourceVersion sourceVersion;
    private final Set<String> annotationTypes;
    private final TypeValidator validator;

    @SafeVarargs
    public SimpleProcessor(SourceVersion version, Class<? extends Annotation>... annotations) {
        this(version, null, annotations);
    }

    @SafeVarargs
    public SimpleProcessor(SourceVersion version, TypeValidator validator, Class<? extends Annotation>... annotations) {
        this.sourceVersion = Objects.requireNonNull(version, "Version may not be null");
        this.validator = validator;
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
     *
     * @see javax.annotation.processing.Processor#process(Set, RoundEnvironment)
     */
    protected void process(Map<TypeElement, Set<TypeElement>> rootElements) {
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

        validate(annotations, roundEnv);

        Map<TypeElement, Set<TypeElement>> types = annotations
                .stream()
                .collect(toMap(Function.identity(), getAnnotatedClasses(roundEnv)));

        process(types);
        return types.isEmpty() || claim();
    }

    @Override
    final public Set<String> getSupportedAnnotationTypes() {
        return annotationTypes;
    }

    @Override
    final public SourceVersion getSupportedSourceVersion() {
        return sourceVersion;
    }

    private Function<TypeElement, Set<TypeElement>> getAnnotatedClasses(RoundEnvironment roundEnv) {
        return annotation -> roundEnv
                .getElementsAnnotatedWith(annotation)
                .stream()
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .collect(toSet());
    }

    private void validate(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (validator == null) {
            return;
        }

        Map<String, List<ValidationOutput>> violations = annotations
                .stream()
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Collection::stream)
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .map(TypeExtractor::new)
                .collect(toMap(TypeExtractor::getTypeQualifiedName, validator::validate));

        Map<String, List<ValidationOutput>> nonEmptyViolations = violations
                .entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        if (!nonEmptyViolations.isEmpty()) {
            throw new ValidationFailed(violations);
        }
    }
}
