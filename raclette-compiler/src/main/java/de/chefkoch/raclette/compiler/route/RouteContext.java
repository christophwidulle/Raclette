package de.chefkoch.raclette.compiler.route;

import de.chefkoch.raclette.compiler.params.ParamsContext;

import javax.lang.model.type.TypeMirror;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class RouteContext {

    private final String packageName;
    private final String name;
    private final String path;
    private final TypeMirror targetActivity;
    private final ParamsContext paramsContext;

    RouteContext(String packageName, String name, String path, TypeMirror targetActivity, ParamsContext paramsContext) {
        this.packageName = packageName;
        this.name = name;
        this.path = path;
        this.targetActivity = targetActivity;
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

    public TypeMirror getTargetActivity() {
        return targetActivity;
    }

    public ParamsContext getParamsContext() {
        return paramsContext;
    }
}
