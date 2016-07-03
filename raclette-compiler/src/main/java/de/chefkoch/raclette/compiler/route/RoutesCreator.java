package de.chefkoch.raclette.compiler.route;

import com.squareup.javapoet.*;
import de.chefkoch.raclette.compiler.BundleHelper;
import de.chefkoch.raclette.compiler.ClassNames;
import de.chefkoch.raclette.compiler.params.NavParamsCreator;
import de.chefkoch.raclette.compiler.params.ParamField;

import javax.lang.model.element.Modifier;
import java.util.List;

import static de.chefkoch.raclette.compiler.StringUtil.decapitalize;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class RoutesCreator {


    public JavaFile create(List<RouteCreator.Result> routesCreated) {

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder("Routes")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());

        CodeBlock.Builder staticBlockCode = CodeBlock.builder();

        for (RouteCreator.Result result : routesCreated) {
            String packageName = result.routeContext.getPackageName();
            String className = result.routeContext.getClassName();

            ClassName routeClassName = ClassName.get(packageName, className);

            staticBlockCode = staticBlockCode
                    .addStatement("$T.register($N)",
                            ClassNames.RoutesDict, className);

            typeBuilder.addField(FieldSpec.builder(routeClassName, result.routeContext.getClassName())
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T()", routeClassName).build());

            typeBuilder.addMethod(MethodSpec.methodBuilder(decapitalize(result.routeContext.getName()))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(routeClassName)
                    .addStatement("return $N", className)
                    .build());
        }
        typeBuilder.addStaticBlock(staticBlockCode.build());

        return JavaFile.builder(ClassNames.RoutingPackageName, typeBuilder.build()).build();
    }


}
