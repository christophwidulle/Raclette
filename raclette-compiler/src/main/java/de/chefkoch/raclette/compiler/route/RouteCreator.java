package de.chefkoch.raclette.compiler.route;

import com.squareup.javapoet.*;
import de.chefkoch.raclette.compiler.BundleHelper;
import de.chefkoch.raclette.compiler.ClassNames;
import de.chefkoch.raclette.compiler.params.ParamField;
import de.chefkoch.raclette.compiler.params.ParamsContext;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

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

        ClassName navRequestBuilderClassName = ClassName.get(
                routeContext.getPackageName(),
                routeContext.getClassName(),
                "NavRequestBuilder"
        );

        FieldSpec pathField = FieldSpec.builder(String.class, "Path", Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                .initializer("\"$N\"", routeContext.getPath()).build();

        TypeSpec navRequestBuilder = createNavRequestBuilder(routeContext, navRequestBuilderClassName);

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(pathField)
                .superclass(ClassNames.Route)
                .addMethod(createConstructor(routeContext))
                .addMethod(createBuilderMethod(navRequestBuilderClassName))
                .addType(navRequestBuilder);

        TypeSpec type = builder.build();
        JavaFile javaFile = JavaFile.builder(routeContext.getPackageName(), type).build();
        return new Result(javaFile, routeContext);
    }

    private MethodSpec createConstructor(RouteContext routeContext) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super(Path,$T.class)", routeContext.getTargetActivity());
        return builder.build();
    }

    private MethodSpec createBuilderMethod(ClassName navRequestBuilder) {
        return MethodSpec.methodBuilder("with")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(navRequestBuilder)
                .addStatement("return new $T()", navRequestBuilder)
                .build();
    }

    /*
   public static class NavRequestBuilder implements Route.NavRequestBuilder {
        private Bundle params = new Bundle();

        public NavRequestBuilder characterIndex(String recipeId) {
            this.params.putString("characterIndex", recipeId);
            return this;
        }

        @Override
        public NavRequest build() {
            return new NavRequest(Path, params);
        }
    }
     */
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
            setterBuilderMethod
                    .addStatement("return this");
            typeSpecBuilder.addMethod(setterBuilderMethod.build());
        }

        return typeSpecBuilder.build();

    }

    /*
     public NavRequestBuilder characterIndex(String recipeId) {
            this.params.putString("characterIndex", recipeId);
            return this;
        }
     */
    private MethodSpec.Builder addSetterStatement(MethodSpec.Builder builder, ParamField paramField) {
        if (paramField.isSerializable) {
            return builder.addStatement("this.setSerializable(\"$N\",)",
                    paramField.name,
                    paramField.type.toString(),
                    paramField.name);
        } else {
            return builder.addStatement("this.$N = bundle.$N(\"$N\")",
                    paramField.name,
                    BundleHelper.findBundleMethod(BundleHelper.MethodType.Getter, paramField),
                    paramField.name);
        }
    }


}
