package de.chefkoch.raclette.compiler.params;

import com.squareup.javapoet.*;
import de.chefkoch.raclette.compiler.BundleHelper;
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
public class NavParamsCreator {


    public static class Result {
        public final TypeMirror sourceType;
        public final String fullQualifiedName;
        public final JavaFile javaFile;
        public final ParamsContext paramsContext;

        public Result(TypeMirror sourceType, String fullQualifiedName, JavaFile javaFile, ParamsContext paramsContext) {
            this.sourceType = sourceType;
            this.fullQualifiedName = fullQualifiedName;
            this.javaFile = javaFile;
            this.paramsContext = paramsContext;
        }
    }

    public Result create(final Element element, ProcessingEnvironment processingEnvironment) {

        ParamsContext paramsContext = new ParamsExtractor().extract(element, processingEnvironment);

        String packageName = getPackage(element);
        String className = getName(element);

        List<FieldSpec> fieldSpecs = new ArrayList<>();

        for (ParamField field : paramsContext.getFields()) {

            FieldSpec attribute = FieldSpec.builder(TypeName.get(field.type), field.name)
                    .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                    .build();
            fieldSpecs.add(attribute);
        }

        MethodSpec constructor = createConstructor(paramsContext);

        ClassName injectorName = ClassName.get("de.chefkoch.raclette.routing", "NavParams.Injector");
        TypeName injectorParameterName = ClassName.get(paramsContext.getViewModelType());

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get("de.chefkoch.raclette.routing", "NavParams"))
                .addSuperinterface(ParameterizedTypeName.get(injectorName, injectorParameterName));

        builder.addMethod(constructor);
        builder.addMethod(createInjectorMethod(paramsContext));

        for (FieldSpec fieldSpec : fieldSpecs) {
            builder = builder.addField(fieldSpec);
        }

        TypeSpec type = builder.build();
        JavaFile javaFile = JavaFile.builder(packageName, type).build();

        return new Result(element.asType(), packageName + "." + className, javaFile, paramsContext);

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

    private MethodSpec createConstructor(ParamsContext paramsContext) {
        try {
            ClassName bundle = ClassName.get("android.os", "Bundle");

            MethodSpec.Builder builder = MethodSpec.constructorBuilder().
                    addModifiers(Modifier.PRIVATE)
                    .addParameter(bundle, "bundle");

            for (ParamField paramField : paramsContext.getFields()) {
                builder = addGetterStatement(builder, paramField);
            }

            //
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private MethodSpec createInjectorMethod(ParamsContext paramsContext) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(SpecUtil.override())
                .returns(TypeName.VOID)
                .addParameter(ClassName.get(paramsContext.getViewModelType()),
                        "viewModel"
                        , Modifier.FINAL);

        for (ParamField paramField : paramsContext.getFields()) {
            builder = builder.addStatement("viewModel.$N = this.$N", paramField.name, paramField.name);
        }
        return builder.build();
    }

    private MethodSpec.Builder addGetterStatement(MethodSpec.Builder builder, ParamField paramField) {
        if (paramField.isSerializable) {
            return builder.addStatement("this.$N = ($N) bundle.getSerializable(\"$N\")",
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
