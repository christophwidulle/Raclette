package de.chefkoch.raclette.compiler.params;

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
                try {
                    boolean isReachable = isReachable(element, field);
                    String paramName = paramAnnotation.value();
                    if ("".equals(paramName)) {
                        paramName = field.getSimpleName().toString();
                    }

                    ParamField paramField = ParamField.from(paramName, fieldTypeMirror, supertypes, isReachable);

                    if (paramField != null) {
                        params.add(paramField);
                    }
                } catch (Exception e) {
                    //ignore field
                }


            }
        }
        return new ParamsContext(element.asType().toString(), element.asType(), params);
    }


    private static boolean isReachable(Element element, final Element field) {

        for (final Modifier modifier : field.getModifiers()) {
            switch (modifier) {
                case PRIVATE:
                case STATIC:
                case FINAL:
                    return false;

            }
        }
        return true;
    }


}
