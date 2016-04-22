package de.chefkoch.raclette.compiler;

import com.squareup.javapoet.JavaFile;
import de.chefkoch.raclette.routing.Nav;
import de.chefkoch.raclette.routing.Route;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

/**
 * ./gradlew --no-daemon -Dorg.gradle.debug=true clean compileDebugJavaWithJavac
 * <p>
 * Created by christophwidulle on 22.04.16.
 */
@SupportedAnnotationTypes("de.chefkoch.raclette.routing.Nav.InjectParams")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class NavParamsGenerator extends AbstractProcessor {

    private ProcessingEnvironment environment;

    public ProcessingEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public synchronized void init(final ProcessingEnvironment environment) {
        super.init(environment);
        this.environment = environment;

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

        final Set<? extends Element> elements = env.getElementsAnnotatedWith(Nav.InjectParams.class);

        for (final Element element : elements) {
            processElement(element);
        }


        return true;
    }


    private void processElement(final Element element) {
        if (!element.getKind().isClass()) {
            logError("Only classes annotated with @" + Nav.InjectParams.class + " are supported", element);
        }
        NavParamsCreator.Result result = new NavParamsCreator().create(element);


        try {
            final JavaFileObject destSourceFile = processingEnv.getFiler().createSourceFile(
                    result.fullQualifiedName,
                    element);

            write(destSourceFile, result.javaFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void write(final JavaFileObject destSourceFile,
                      final JavaFile contents) throws Exception {


        final Writer writer = destSourceFile.openWriter();

        try {
            contents.writeTo(writer);
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * Logs a warning
     *
     * @param message the message to log
     * @param element the element that is concerned. Can be used in an IDE to link to the corresponding code fragment
     */
    public void logWarning(final String message, final Element element) {
        getEnvironment().getMessager().printMessage(Diagnostic.Kind.WARNING, message, element);
    }

    /**
     * Logs an info
     *
     * @param message the message to log
     * @param element the element that is concerned. Can be used in an IDE to link to the corresponding code fragment
     */
    public void logInfo(final String message, final Element element) {
        getEnvironment().getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }

    /**
     * Logs an info
     *
     * @param message the message to log
     */
    public void logInfo(final String message) {
        getEnvironment().getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    /**
     * Logs an error
     *
     * @param message the message to log
     */
    public void logError(final String message) {
        getEnvironment().getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    /**
     * Logs an error
     *
     * @param message the message to log
     * @param element the element that is concerned. Can be used in an IDE to link to the corresponding code fragment
     */
    public void logError(final String message, final Element element) {
        getEnvironment().getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
