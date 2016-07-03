package de.chefkoch.raclette.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Created by christophwidulle on 24.04.16.
 */
public class ClassNames {

    public static final String RoutingPackageName = "de.chefkoch.raclette.routing";

    public static ClassName BaseResult = ClassName.get(RoutingPackageName, "BaseResult");
    public static ClassName RoutesDict = ClassName.get(RoutingPackageName, "RoutesDict");
    public static ClassName Routes = ClassName.get(RoutingPackageName, "Routes");
    public static ClassName Route = ClassName.get(RoutingPackageName, "Route");
    public static ClassName NavRequestBuilder = ClassName.get(RoutingPackageName, "Route.NavRequestBuilder");
    public static ClassName NavRequest = ClassName.get(RoutingPackageName, "NavRequest");
    public static ClassName Bundle = ClassName.get("android.os", "Bundle");
    public static ClassName AutoDetect = ClassName.get("android.os", "Bundle");



}
