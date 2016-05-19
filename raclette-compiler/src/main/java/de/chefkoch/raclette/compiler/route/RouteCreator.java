package de.chefkoch.raclette.compiler.route;

import com.squareup.javapoet.*;
import de.chefkoch.raclette.compiler.BundleHelper;
import de.chefkoch.raclette.compiler.ClassNames;
import de.chefkoch.raclette.compiler.params.ParamField;
import de.chefkoch.raclette.routing.Route;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class RouteCreator {


    public static class Result {
        public final RouteContext routeContext;
        public final JavaFile javaFile;

        public Result(JavaFile javaFile, RouteContext routeContext) {
            this.routeContext = routeContext;
            this.javaFile = javaFile;
        }

        public String getFullQulifiedName() {
            return routeContext.getPackageName() + "." + routeContext.getClassName();
        }
    }

    public Result create(RouteContext routeContext) {

        String className = routeContext.getClassName();
        String name = routeContext.getName();


        ParameterizedTypeName superKlass = ParameterizedTypeName.get(ClassNames.Route, routeContext.getParameterClass());


        FieldSpec pathField = FieldSpec.builder(String.class, "Path", Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                .initializer("\"$N\"", routeContext.getPath()).build();

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(pathField)
                .superclass(superKlass)
                .addMethod(createConstructor(routeContext));
                //.addMethod(createBuilderMethod(navRequestBuilderClassName));
                //.addType(navRequestBuilder);

        TypeSpec type = builder.build();
        JavaFile javaFile = JavaFile.builder(routeContext.getPackageName(), type)
                .addStaticImport(Route.TargetType.class, "*")
                .build();
        return new Result(javaFile, routeContext);
    }

    private MethodSpec createConstructor(RouteContext routeContext) {

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super(Path, $T.class, $N)", routeContext.getTargetClass(), routeContext.getTargetType().toString());
        return builder.build();
    }


    /*
    private TypeSpec createNavRequestBuilder(RouteContext routeContext, ClassName navRequestBuilderClassName) {

        FieldSpec bundleField = FieldSpec.builder(ClassNames.Bundle, "params")
                .initializer("new $T()", ClassNames.Bundle)
                .build();


        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("NavRequestBuilder")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addSuperinterface(ClassNames.NavRequestBuilder)
                .addField(bundleField)
                .addMethod(MethodSpec.methodBuilder("build")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ClassNames.NavRequest)
                        .addStatement("return new $T(Path,params)", ClassNames.NavRequest)
                        .build());


        for (ParamField paramField : routeContext.getParamsContext().getFields()) {
            MethodSpec.Builder setterBuilderMethod = MethodSpec.methodBuilder(paramField.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(paramField.type), paramField.name)
                    .returns(navRequestBuilderClassName);

            if (paramField.isSerializable) {
                setterBuilderMethod = setterBuilderMethod.addStatement("params.putSerializable(\"$N\", $N)",
                        paramField.name,
                        paramField.name);
            } else {
                setterBuilderMethod = setterBuilderMethod.addStatement("params.$N(\"$N\", $N)",
                        BundleHelper.findBundleMethod(BundleHelper.MethodType.Setter, paramField),
                        paramField.name,
                        paramField.name);
            }
            setterBuilderMethod.addStatement("return this");
            typeSpecBuilder.addMethod(setterBuilderMethod.build());
        }

        return typeSpecBuilder.build();

    }
    */




}
