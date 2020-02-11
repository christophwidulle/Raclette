package de.chefkoch.raclette.compiler.route;

import com.squareup.javapoet.*;

import de.chefkoch.raclette.compiler.BundleHelper;
import de.chefkoch.raclette.compiler.ClassNames;
import de.chefkoch.raclette.compiler.SpecUtil;
import de.chefkoch.raclette.compiler.StringUtil;
import de.chefkoch.raclette.compiler.params.ParamField;
import de.chefkoch.raclette.routing.Route;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ReturnCreator {

    public static class Result {
        public final RouteContext routeContext;
        public final JavaFile javaFile;
        public final String name;

        public Result(JavaFile javaFile, RouteContext routeContext, String name) {
            this.routeContext = routeContext;
            this.javaFile = javaFile;
            this.name = name;
        }

        public String getFullQulifiedName() {
            return routeContext.getPackageName() + "." + name;
        }
    }


    public List<Result> createAll(RouteContext routeContext, ProcessingEnvironment processingEnvironment) {
        List<Result> results = new ArrayList<>();
        for (RouteContext.Return aReturn : routeContext.getReturns()) {
            Result result = create(aReturn, routeContext, processingEnvironment);
            results.add(result);
        }
        return results;
    }

    Result create(RouteContext.Return aReturn, RouteContext routeContext, ProcessingEnvironment processingEnvironment) {

        String className = StringUtil.capitalize(aReturn.getName()) + "Result";
        TypeName returnType = TypeName.get(aReturn.getKlass());
        TypeMirror returnTypeMirror = aReturn.getKlass();

        ParameterizedTypeName superKlass = ParameterizedTypeName.get(ClassNames.BaseResult, returnType);

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(superKlass)
                .addMethod(createConstructor(returnType))
                .addMethod(createWriteValue(returnTypeMirror, processingEnvironment))
                .addMethod(createAsBundle(returnTypeMirror, className))
                .addMethod(createFrom(returnTypeMirror, className, processingEnvironment));

        TypeSpec type = builder.build();
        JavaFile javaFile = JavaFile.builder(routeContext.getPackageName(), type)
                .addStaticImport(Route.TargetType.class, "*")
                .build();
        return new Result(javaFile, routeContext, className);
    }

    private MethodSpec createFrom(TypeMirror type, String className, ProcessingEnvironment processingEnvironment) {


        List<? extends TypeMirror> supertypes = processingEnvironment.getTypeUtils().directSupertypes(type);

        ParamField paramField = ParamField.from("value", type, supertypes, true);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("from")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.bestGuess(className))
                .addParameter(ClassNames.Bundle, "bundle")
                .addStatement("$T value = ($T) bundle.$N(\"value\")",
                        type,
                        type,
                        BundleHelper.findBundleMethod(BundleHelper.MethodType.Getter, paramField)
                )
                .addStatement("return new $N(value)", className);

        return builder.build();
    }

    private MethodSpec createAsBundle(TypeMirror type, String className) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("asBundle")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassNames.Bundle)
                .addParameter(TypeName.get(type), "value")
                .addStatement("return new $N(value).toBundle()", className);

        return builder.build();
    }

    private MethodSpec createWriteValue(TypeMirror type, ProcessingEnvironment processingEnvironment) {

        List<? extends TypeMirror> supertypes = processingEnvironment.getTypeUtils().directSupertypes(type);

        ParamField paramField = ParamField.from("value", type, supertypes, true);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("writeValue")
                .addAnnotation(SpecUtil.override())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(type), "value")
                .addParameter(ClassNames.Bundle, "bundle")
                .addStatement("bundle.$N(\"value\",value)",
                        BundleHelper.findBundleMethod(BundleHelper.MethodType.Setter, paramField));

        return builder.build();
    }


    private MethodSpec createConstructor(TypeName type) {

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, "value")
                .addStatement("super(value)");
        return builder.build();
    }


}
