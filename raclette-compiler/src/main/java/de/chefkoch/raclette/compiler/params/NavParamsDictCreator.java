package de.chefkoch.raclette.compiler.params;

import com.squareup.javapoet.*;
import de.chefkoch.raclette.compiler.BundleHelper;
import de.chefkoch.raclette.compiler.ClassNames;
import de.chefkoch.raclette.compiler.SpecUtil;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class NavParamsDictCreator {

    /*

    static {
        NavParams.injectors().register(CharacterViewModel.class, CharacterParams.class);
    }
     */

    public JavaFile create(List<NavParamsCreator.Result> paramsCreated) {

        ClassName dictClassName = ClassName.get(ClassNames.RoutingPackageName, "AllNavParamsDict");
        ClassName navParamsClass = ClassName.get(ClassNames.RoutingPackageName, "NavParams");

        CodeBlock.Builder staticBlockCode = CodeBlock.builder();

        for (NavParamsCreator.Result result : paramsCreated) {
            staticBlockCode = staticBlockCode
                    .addStatement("$T.injectors().register($T.class,$T.class)",
                            navParamsClass,
                            ClassName.get(result.paramsContext.viewModelType),
                            ClassName.get(result.packageName, result.className));
        }

        TypeSpec.Builder builder = TypeSpec.classBuilder("AllNavParamsDict")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
                .addStaticBlock(staticBlockCode.build());

        return JavaFile.builder(ClassNames.RoutingPackageName, builder.build()).build();

    }
}
