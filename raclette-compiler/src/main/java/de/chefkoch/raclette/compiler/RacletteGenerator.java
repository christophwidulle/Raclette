package de.chefkoch.raclette.compiler;

import com.squareup.javapoet.JavaFile;
import de.chefkoch.raclette.compiler.params.NavParamsCreator;
import de.chefkoch.raclette.compiler.params.NavParamsDictCreator;
import de.chefkoch.raclette.compiler.route.*;
import de.chefkoch.raclette.routing.Nav;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.*;

/**
 * ./gradlew --no-daemon -Dorg.gradle.debug=true clean compileDebugJavaWithJavac
 * <p>
 * Created by christophwidulle on 22.04.16.
 */
@SupportedAnnotationTypes({
        "de.chefkoch.raclette.routing.Nav.InjectParams",
        "de.chefkoch.raclette.routing.Nav.Route"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RacletteGenerator extends AbstractProcessor {

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

        try {
            final Set<? extends Element> parameterElements = env.getElementsAnnotatedWith(Nav.InjectParams.class);

            List<NavParamsCreator.Result> paramsCreated = new ArrayList<>();
            for (final Element element : parameterElements) {
                NavParamsCreator.Result result = createParam(element);
                if (result != null) {
                    paramsCreated.add(result);
                }
            }

            createParamsDict(paramsCreated, null);

            List<RouteCreator.Result> routesCreated = new ArrayList<>();

            Set<? extends Element> routeElements = env.getElementsAnnotatedWith(Nav.Route.class);
            for (Element routeElement : routeElements) {
                List<RouteCreator.Result> route = createRoute(routeElement, asMap(paramsCreated));
                if (route != null) {
                    routesCreated.addAll(route);
                }
            }

            for (RouteCreator.Result routeResult : routesCreated) {
                createReturns(routeResult.routeContext);
            }

            routeElements = env.getElementsAnnotatedWith(Nav.Dispatch.class);
            for (Element routeElement : routeElements) {
                List<RouteCreator.Result> route = createRoutesDispatch(routeElement, asMap(paramsCreated));
                if (route != null) {
                    routesCreated.addAll(route);
                }
            }

            createRoutes(routesCreated, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logError(e.getMessage());
        }
        return false;
    }

    private List<ReturnCreator.Result> createReturns(final RouteContext routeContext) {

        List<ReturnCreator.Result> results = new ArrayList<>();
        List<ReturnCreator.Result> extracted = new ReturnCreator().createAll(routeContext);
        for (ReturnCreator.Result result : extracted) {
            if (write(result)) results.add(result);
        }
        return results;
    }

    private List<RouteCreator.Result> createRoute(final Element element, Map<String, NavParamsCreator.Result> resultMap) {
        if (!element.getKind().isClass()) {
            logError("Only classes annotated with @" + Nav.Route.class + " are supported", element);
        }
        List<RouteCreator.Result> results = new ArrayList<>();

        RouteContext routeContext = new RouteExtractor().extractRoute(element, resultMap, environment);
        RouteCreator.Result result = new RouteCreator().create(routeContext);
        if (write(result, element)) results.add(result);
        return results;
    }

    private List<RouteCreator.Result> createRoutesDispatch(final Element element, Map<String, NavParamsCreator.Result> paramsContextMap) {
        if (!element.getKind().isClass()) {
            logError("Only classes annotated with @" + Nav.Dispatch.class + " are supported", element);
        }
        List<RouteCreator.Result> results = new ArrayList<>();

        List<RouteContext> routeContexts = new RouteExtractor().extractDispatch(element, paramsContextMap, environment);
        for (RouteContext routeContext : routeContexts) {
            RouteCreator.Result result = new RouteCreator().create(routeContext);
            if (write(result, element)) results.add(result);


        }
        return results;
    }


    private boolean write(ReturnCreator.Result result) {
        try {
            final JavaFileObject destSourceFile = processingEnv.getFiler().createSourceFile(
                    result.getFullQulifiedName(),
                    (Element) null);

            write(destSourceFile, result.javaFile);
            return true;
        } catch (Exception e) {
            logError(e.getMessage());
        }
        return false;
    }

    private boolean write(RouteCreator.Result result, Element element) {
        try {
            final JavaFileObject destSourceFile = processingEnv.getFiler().createSourceFile(
                    result.getFullQulifiedName(),
                    element);

            write(destSourceFile, result.javaFile);
            return true;
        } catch (Exception e) {
            logError(e.getMessage());
        }
        return false;
    }

    private Map<String, NavParamsCreator.Result> asMap(List<NavParamsCreator.Result> results) {
        Map<String, NavParamsCreator.Result> map = new HashMap<>();
        for (NavParamsCreator.Result result : results) {
            String key = result.viewModelType.toString();
            map.put(key, result);
        }
        return map;
    }

    private NavParamsCreator.Result createParam(final Element element) {
        if (!element.getKind().isClass()) {
            logError("Only classes annotated with @" + Nav.InjectParams.class + " are supported", element);
        }
        NavParamsCreator.Result result = new NavParamsCreator().create(element, environment);
        try {
            final JavaFileObject destSourceFile = processingEnv.getFiler().createSourceFile(
                    result.getFullQulifiedName(),
                    element);

            write(destSourceFile, result.javaFile);

            return result;
        } catch (Exception e) {
            logError(e.getMessage());
        }
        return null;
    }

    private void createParamsDict(final List<NavParamsCreator.Result> paramsCreated, final Element element) {
        if (paramsCreated.size() > 0) {
            JavaFile javaFile = new NavParamsDictCreator().create(paramsCreated);
            final JavaFileObject destSourceFile;
            try {
                destSourceFile = processingEnv.getFiler().createSourceFile(
                        "de.chefkoch.raclette.routing.AllNavParamsDict",
                        element);
                write(destSourceFile, javaFile);

            } catch (Exception e) {
                logError(e.getMessage());
            }
        }
    }

    private void createRoutes(List<RouteCreator.Result> routesCreated, final Element element) {
        if (routesCreated.size() > 0) {
            JavaFile javaFile = new RoutesCreator().create(routesCreated);
            final JavaFileObject destSourceFile;
            try {
                destSourceFile = processingEnv.getFiler().createSourceFile(
                        javaFile.packageName + ".Routes",
                        element);
                write(destSourceFile, javaFile);

            } catch (Exception e) {
                logError(e.getMessage());
            }
        }
    }


    void write(final JavaFileObject destSourceFile,
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
