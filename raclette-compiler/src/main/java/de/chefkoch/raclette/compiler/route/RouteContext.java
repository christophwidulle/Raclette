package de.chefkoch.raclette.compiler.route;

import com.squareup.javapoet.TypeName;
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
    private final TypeName parameterClass;
    private final ParamsContext paramsContext;
    private final Route.TargetType targetType;

    RouteContext(String packageName, String name, String path, TypeMirror targetClass, TypeName parameterClass, Route.TargetType targetType, ParamsContext paramsContext) {
        this.packageName = packageName;
        this.name = name;
        this.path = path;
        this.targetClass = targetClass;
        this.parameterClass = parameterClass;
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

    public TypeName getParameterClass() {
        return parameterClass;
    }
}
