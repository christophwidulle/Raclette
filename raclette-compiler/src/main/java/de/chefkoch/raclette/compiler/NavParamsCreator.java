package de.chefkoch.raclette.compiler;

import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class NavParamsCreator {


    public static class Result {
        public String fullQualifiedName;
        public JavaFile javaFile;

        public Result(String fullQualifiedName, JavaFile javaFile) {
            this.fullQualifiedName = fullQualifiedName;
            this.javaFile = javaFile;
        }
    }

    public Result create(final Element element) {

        ParamsContext paramsContext = new ParamsExtractor().extract(element);

        String packageName = getPackage(element);
        String className = getName(element);

        List<FieldSpec> fieldSpecs = new ArrayList<>();

        for (ParamField field : paramsContext.getFields()) {

            FieldSpec attribute = FieldSpec.builder(TypeName.get(field.type), field.name)
                    .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                    .build();
            fieldSpecs.add(attribute);
        }

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (FieldSpec fieldSpec : fieldSpecs) {
            builder = builder.addField(fieldSpec);
        }

        TypeSpec helloWorld = builder.build();
        JavaFile javaFile = JavaFile.builder(packageName, helloWorld).build();

        return new Result(packageName + "." + className, javaFile);

    }

    private static String getPackage(Element element) {
        String fullName = element.asType().toString();
        String packageName = fullName.substring(0, fullName.lastIndexOf('.'));
        return packageName;
    }

    private static String getName(Element element) {
        String simpleName = element.getSimpleName().toString();
        return simpleName + "Params";
    }


}
