package de.chefkoch.raclette.compiler;

import de.chefkoch.raclette.routing.Nav;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ParamsExtractor {


    public ParamsContext extract(final Element element) {
        List<ParamField> params = new ArrayList<>();

        for (final Element field : element.getEnclosedElements()) {
            if (field.getKind() != ElementKind.FIELD) {
                continue;
            }

            final Nav.Param paramAnnotation = field.getAnnotation(Nav.Param.class);

            if (paramAnnotation != null) {
                assertReachable(field);

                String paramName = paramAnnotation.value();
                if ("".equals(paramName)) {
                    paramName = field.getSimpleName().toString();
                }
                TypeMirror type = findTypeKind(paramAnnotation);
                if ("java.lang.Object".equals(type.toString())) {
                    type = field.asType();
                }

                ParamField paramField = new ParamField(paramName, type);
                params.add(paramField);
            }
        }
        return new ParamsContext(element.asType().toString(), params);
    }

    private static TypeMirror findTypeKind(Nav.Param param) {
        TypeMirror value = null;
        if (param != null) {
            try {
                param.type();
            } catch (MirroredTypeException mte) {
                value = mte.getTypeMirror();
            }
        }
        return value;
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
