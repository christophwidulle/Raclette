package de.chefkoch.raclette.compiler;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.type.TypeMirror;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ParamField {

    public String name;
    public TypeMirror type;

    public ParamField(String name, TypeMirror type) {
        this.name = name;
        this.type = type;
    }
}
