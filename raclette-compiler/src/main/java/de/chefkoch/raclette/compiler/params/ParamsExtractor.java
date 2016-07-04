package de.chefkoch.raclette.compiler.params;

import de.chefkoch.raclette.compiler.BundleHelper;
import de.chefkoch.raclette.routing.Nav;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ParamsExtractor {


    public ParamsContext extract(final Element element, ProcessingEnvironment processingEnvironment) {
        List<ParamField> params = new ArrayList<>();

        for (final Element field : element.getEnclosedElements()) {
            if (field.getKind() != ElementKind.FIELD) {
                continue;
            }

            TypeMirror fieldTypeMirror = field.asType();
            List<? extends TypeMirror> supertypes = processingEnvironment.getTypeUtils().directSupertypes(fieldTypeMirror);

            final Nav.Param paramAnnotation = field.getAnnotation(Nav.Param.class);

            if (paramAnnotation != null) {
                assertReachable(field);

                String paramName = paramAnnotation.value();
                if ("".equals(paramName)) {
                    paramName = field.getSimpleName().toString();
                }

                ParamField paramField = ParamField.from(paramName, fieldTypeMirror, supertypes);

                if (paramField != null) {
                    params.add(paramField);
                }
            }
        }
        return new ParamsContext(element.asType().toString(), element.asType(), params);
    }


    private static Element assertReachable(final Element field) {

        for (final Modifier modifier : field.getModifiers()) {
            switch (modifier) {
                case PRIVATE:
                    illegalArgument("Field may not be private");
                case STATIC:
                    illegalArgument("Field may not be static");
                case FINAL:
                    illegalArgument("Field may not be final");
            }
        }
        return field;
    }

    private static void illegalArgument(final String msg) throws IllegalArgumentException {
        throw new IllegalArgumentException(msg);
    }


}
