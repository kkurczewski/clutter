package io.clutter.processor;

import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.exception.ValidationFailed;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
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
     * Provides {@link ProcessorAggregate} with annotated classes for processing
     * and {@link FileGenerator} for generating new files
     * <p>
     * This method provides default implementation for {@link javax.annotation.processing.Processor#process(Set, RoundEnvironment)}
     */
    public void process(ProcessorAggregate elements, FileGenerator fileGenerator) {
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
        process(new ProcessorAggregate(annotations, roundEnv), new FileGenerator(processingEnv.getFiler()));
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

    private void validate(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (validator == null) {
            return;
        }

        var violations = annotations
                .stream()
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Collection::stream)
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .collect(toMap(type -> valueOf(type.getQualifiedName()), validator::validate));

        var nonEmptyViolations = violations
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
