package de.chefkoch.raclette.compiler.route;

import de.chefkoch.raclette.compiler.params.ParamsContext;
import de.chefkoch.raclette.routing.Route;

import javax.lang.model.type.TypeMirror;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class RouteContext {

    private final String packageName;
    private final String name;
    private final String path;
    private final TypeMirror targetClass;
    private final ParamsContext paramsContext;
    private final Route.TargetType targetType;

    RouteContext(String packageName, String name, String path, TypeMirror targetClass, Route.TargetType targetType, ParamsContext paramsContext) {
        this.packageName = packageName;
        this.name = name;
        this.path = path;
        this.targetClass = targetClass;
        this.targetType = targetType;
        this.paramsContext = paramsContext;
    }

    public String getClassName() {
        return name + "$Route";
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public TypeMirror getTargetClass() {
        return targetClass;
    }

    public ParamsContext getParamsContext() {
        return paramsContext;
    }

    public Route.TargetType getTargetType() {
        return targetType;
    }
}
