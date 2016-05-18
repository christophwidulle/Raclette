package de.chefkoch.raclette.compiler.route;

import com.squareup.javapoet.*;
import de.chefkoch.raclette.compiler.params.ParamsContext;
import de.chefkoch.raclette.routing.Nav;
import de.chefkoch.raclette.routing.NavParams;
import de.chefkoch.raclette.routing.Route;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class RouteExtractor {


    public static class Result {
        public final String routeName;
        public final String packageName;
        public final String className;
        public final JavaFile javaFile;

        public Result(String packageName, String className, JavaFile javaFile, ParamsContext paramsContext, String routeName) {
            this.packageName = packageName;
            this.className = className;
            this.javaFile = javaFile;
            this.routeName = routeName;
        }

        public String getFullQulifiedName() {
            return packageName + "." + className;
        }
    }

    public RouteContext extractRoute(final Element element, Map<String, ParamsContext> paramsContextMap, ProcessingEnvironment processingEnvironment) {
        Nav.Route routeAnnotation = element.getAnnotation(Nav.Route.class);
        if (routeAnnotation != null) {
            return extractRoute(routeAnnotation, element, paramsContextMap, processingEnvironment);
        }
        return null;
    }

    public List<RouteContext> extractDistach(final Element element, Map<String, ParamsContext> paramsContextMap, ProcessingEnvironment processingEnvironment) {

        List<RouteContext> routes = new ArrayList<>();
        Nav.Dispatch dispatchAnnotation = element.getAnnotation(Nav.Dispatch.class);
        if (dispatchAnnotation != null) {
            Nav.Route[] routesAnnotation = dispatchAnnotation.value();
            for (Nav.Route route : routesAnnotation) {
                RouteContext routeContext = extractRoute(route, element, paramsContextMap, processingEnvironment);
                if (routeContext != null) routes.add(routeContext);
            }
        }
        return routes;
    }


    private RouteContext extractRoute(final Nav.Route routeAnnotation, final Element element, Map<String, ParamsContext> paramsContextMap, ProcessingEnvironment processingEnvironment) {

        TypeMirror targetClass = element.asType();

        Route.TargetType targetType = findTargetType(targetClass, processingEnvironment);
        if (routeAnnotation != null && targetType != null) {

            String path = routeAnnotation.value();
            TypeMirror paramsType = getClass(routeAnnotation);
            if (paramsType != null) {
                ParamsContext paramsContext = find(paramsContextMap, paramsType.toString());
                if (paramsContext == null) {
                    paramsContext = ParamsContext.NONE;
                }

                String packageName = getPackage(element);
                String name = guessName(path);


                return new RouteContext(packageName, name, path, targetClass, ClassName.get(packageName, name + "Params"), targetType, paramsContext);
            }
        }
        return null;
    }


    //todo find better way
    private ParamsContext find(Map<String, ParamsContext> paramsContextMap, String type) {
        if (type.contains("<any?>.")) {
            type = type.replace("<any?>.", "");
        }
        for (Map.Entry<String, ParamsContext> entry : paramsContextMap.entrySet()) {
            if (entry.getKey().contains(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private String guessName(String path) {
        String name = capitalize(path);
        if (path.contains("/")) {
            name = "";
            String[] split = path.split("/");
            for (String s : split) {
                if (s != null && !"".equals(s)) {
                    name += capitalize(s);
                }
            }
        }
        return name;
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private TypeMirror getClass(Nav.Route routeAnnotation) {
        try {
            Class<? extends NavParams> aClass = routeAnnotation.navParams();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }


    private Route.TargetType findTargetType(TypeMirror targetClass, ProcessingEnvironment processingEnvironment) {
        if (isActivity(targetClass, processingEnvironment)) {
            return Route.TargetType.Activity;
        } else if (isDialogFragment(targetClass, processingEnvironment)) {
            return Route.TargetType.DialogFragment;
        } else if (isSupportDialogFragment(targetClass, processingEnvironment)) {
            return Route.TargetType.SupportDialogFragment;
        } else return null;
        //todo log it
    }

    private boolean isActivity(TypeMirror targetClass, ProcessingEnvironment processingEnvironment) {
        TypeMirror activityElement = processingEnvironment.getElementUtils().getTypeElement("android.app.Activity").asType();
        return processingEnvironment.getTypeUtils().isAssignable(targetClass, activityElement);
    }

    private boolean isDialogFragment(TypeMirror targetClass, ProcessingEnvironment processingEnvironment) {
        TypeMirror activityElement = processingEnvironment.getElementUtils().getTypeElement("android.app.Fragment").asType();
        return processingEnvironment.getTypeUtils().isAssignable(targetClass, activityElement);
    }

    private boolean isSupportDialogFragment(TypeMirror targetClass, ProcessingEnvironment processingEnvironment) {
        TypeMirror activityElement = processingEnvironment.getElementUtils().getTypeElement("android.support.v4.app.Fragment").asType();
        return processingEnvironment.getTypeUtils().isAssignable(targetClass, activityElement);
    }

    private static String getPackage(Element element) {
        String fullName = element.asType().toString();
        return fullName.substring(0, fullName.lastIndexOf('.'));
    }

    private static String getName(Element element) {
        String simpleName = element.getSimpleName().toString();
        int viewModelIndex = simpleName.indexOf("ViewModel");
        if (viewModelIndex > -1) {
            simpleName = simpleName.substring(0, viewModelIndex);
        }
        return simpleName + "Params";
    }

    private static AnnotationMirror getAnnotationMirror(TypeElement typeElement, String className) {
        for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(className)) {
                return m;
            }
        }
        return null;
    }

    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private TypeElement getAnnotationValueAsType(AnnotationMirror annotationMirror, String key, ProcessingEnvironment processingEnvironment) {
        AnnotationValue annotationValue = getAnnotationValue(annotationMirror, key);
        if (annotationValue == null) {
            return null;
        }
        TypeMirror typeMirror = (TypeMirror) annotationValue.getValue();
        if (typeMirror == null) {
            return null;
        }
        return (TypeElement) processingEnvironment.getTypeUtils().asElement(typeMirror);
    }

}
