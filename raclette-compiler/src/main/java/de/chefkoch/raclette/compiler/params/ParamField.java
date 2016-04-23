package de.chefkoch.raclette.compiler.params;

import javax.lang.model.type.TypeMirror;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ParamField {

    public final String name;
    public final TypeMirror type;

    public final boolean isSerializable;
    public final boolean isParcelable;


    public static ParamField asSerializable(String name, TypeMirror type) {
        return new ParamField(name, type, true, false);
    }

    public static ParamField asParceable(String name, TypeMirror type) {
        return new ParamField(name, type, false, true);
    }

    public static ParamField of(String name, TypeMirror type) {
        return new ParamField(name, type, false, false);
    }

    ParamField(String name, TypeMirror type, boolean isSerializable, boolean isParcelable) {
        this.name = name;
        this.type = type;
        this.isSerializable = isSerializable;
        this.isParcelable = isParcelable;
    }
}
