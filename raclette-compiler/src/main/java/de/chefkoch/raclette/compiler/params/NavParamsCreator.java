package de.chefkoch.raclette.compiler.params;

import com.squareup.javapoet.*;
import de.chefkoch.raclette.compiler.BundleHelper;
import de.chefkoch.raclette.compiler.ClassNames;
import de.chefkoch.raclette.compiler.SpecUtil;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class NavParamsCreator {


    public static class Result {
        public final String packageName;
        public final String className;
        public final JavaFile javaFile;
        public final ParamsContext paramsContext;

        public Result(String packageName, String className, JavaFile javaFile, ParamsContext paramsContext) {
            this.packageName = packageName;
            this.className = className;
            this.javaFile = javaFile;
            this.paramsContext = paramsContext;
        }


        public String getFullQulifiedName() {
            return packageName + "." + className;
        }
    }

    public Result create(final Element element, ProcessingEnvironment processingEnvironment) {

        ParamsContext paramsContext = new ParamsExtractor().extract(element, processingEnvironment);

        String packageName = getPackage(element);
        String name = getName(element);

        ClassName className = ClassName.get(packageName, name);

        List<FieldSpec> fieldSpecs = new ArrayList<>();

        for (ParamField field : paramsContext.getFields()) {

            FieldSpec attribute = FieldSpec.builder(TypeName.get(field.type), field.name)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            fieldSpecs.add(attribute);
        }


        ClassName injectorName = ClassName.get(ClassNames.RoutingPackageName, "NavParams.Injector");
        TypeName injectorParameterName = ClassName.get(paramsContext.getViewModelType());

        TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get(ClassNames.RoutingPackageName, "NavParams"))
                .addSuperinterface(ParameterizedTypeName.get(injectorName, injectorParameterName));

        for (MethodSpec methodSpec : createBundleConstructor(paramsContext, className)) {
            builder = builder.addMethod(methodSpec);
        }

        for (MethodSpec methodSpec : createEmptyConstructor(className)) {
            builder = builder.addMethod(methodSpec);
        }

        builder = builder.addMethod(createInjectorMethod(paramsContext));

        for (FieldSpec fieldSpec : fieldSpecs) {
            builder = builder.addField(fieldSpec);
        }

        for (MethodSpec methodSpec : createSetter(paramsContext, className)) {
            builder = builder.addMethod(methodSpec);
        }

        for (MethodSpec methodSpec : createGetter(paramsContext, className)) {
            builder = builder.addMethod(methodSpec);
        }

        builder = builder.addMethod(toBundleMethod(paramsContext));

        TypeSpec type = builder.build();
        JavaFile javaFile = JavaFile.builder(packageName, type).build();

        return new Result(packageName, name, javaFile, paramsContext);

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

    private List<MethodSpec> createBundleConstructor(ParamsContext paramsContext, ClassName className) {
        ClassName bundle = ClassName.get("android.os", "Bundle");

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(bundle, "bundle");

        for (ParamField paramField : paramsContext.getFields()) {
            constructorBuilder = addAssignStatement(constructorBuilder, paramField);
        }

        MethodSpec.Builder factoryBuilder = MethodSpec.methodBuilder("from")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(bundle, "bundle")
                .returns(className)
                .addStatement("return new $T(bundle)", className);

        return Arrays.asList(constructorBuilder.build(), factoryBuilder.build());
    }

    private List<MethodSpec> createEmptyConstructor(ClassName className) {

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE);

        MethodSpec.Builder factoryBuilder = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(className)
                .addStatement("return new $T()", className);

        return Arrays.asList(builder.build(), factoryBuilder.build());
    }

    private List<MethodSpec> createSetter(ParamsContext paramsContext, ClassName className) {
        List<MethodSpec> setters = new ArrayList<>();
        for (ParamField field : paramsContext.getFields()) {

            setters.add(MethodSpec.methodBuilder(field.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(field.type), field.name)
                    .returns(className)
                    .addStatement("this.$N = $N", field.name, field.name)
                    .addStatement("return this")
                    .build());


        }
        return setters;
    }

    private List<MethodSpec> createGetter(ParamsContext paramsContext, ClassName className) {
        List<MethodSpec> getter = new ArrayList<>();
        for (ParamField field : paramsContext.getFields()) {

            getter.add(MethodSpec.methodBuilder(field.name)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.get(field.type))
                    .addStatement("return $N", field.name)
                    .build());


        }
        return getter;
    }


    private MethodSpec toBundleMethod(ParamsContext paramsContext) {

        MethodSpec.Builder toBundleMethod = MethodSpec.methodBuilder("toBundle")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(SpecUtil.override())
                .returns(ClassNames.Bundle)
                .addStatement("final $T params = new $T()", ClassNames.Bundle, ClassNames.Bundle);

        for (ParamField paramField : paramsContext.getFields()) {


            if (paramField.isSerializable) {
                toBundleMethod = toBundleMethod.addStatement("params.putSerializable(\"$N\", $N)",
                        paramField.name,
                        paramField.name);
            } else {
                toBundleMethod = toBundleMethod.addStatement("params.$N(\"$N\", $N)",
                        BundleHelper.findBundleMethod(BundleHelper.MethodType.Setter, paramField),
                        paramField.name,
                        paramField.name);
            }
        }
        toBundleMethod.addStatement("return params");
        return toBundleMethod.build();
    }

    private MethodSpec createInjectorMethod(ParamsContext paramsContext) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(SpecUtil.override())
                .returns(TypeName.VOID)
                .addParameter(ClassName.get(paramsContext.getViewModelType()),
                        "viewModel", Modifier.FINAL);

        for (ParamField paramField : paramsContext.getFields()) {
            builder = builder.addStatement("viewModel.$N = this.$N", paramField.name, paramField.name);
        }
        return builder.build();
    }

    private MethodSpec.Builder addAssignStatement(MethodSpec.Builder builder, ParamField paramField) {
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
