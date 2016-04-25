package de.chefkoch.raclette.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class SpecUtil {

    public static AnnotationSpec override(){
        return AnnotationSpec.builder(ClassName.get("java.lang","Override")).build();
    }

}
