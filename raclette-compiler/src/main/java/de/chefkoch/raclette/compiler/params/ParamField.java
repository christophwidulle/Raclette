package de.chefkoch.raclette.compiler.params;

import de.chefkoch.raclette.compiler.BundleHelper;

import javax.lang.model.type.TypeMirror;

import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ParamField {

    public final String name;
    public final TypeMirror type;

    public final boolean isSerializable;
    public final boolean isParcelable;
    public final boolean isReachable;


    public static ParamField from(String paramName, TypeMirror fieldTypeMirror, List<? extends TypeMirror> supertypes, boolean isReachable) {
        ParamField paramField = null;
        boolean simpleType = BundleHelper.isSimpleType(fieldTypeMirror);
        if (simpleType) {
            paramField = ParamField.of(paramName, fieldTypeMirror, isReachable);
        } else if (BundleHelper.isParceble(supertypes)) {
            paramField = ParamField.asParceable(paramName, fieldTypeMirror, isReachable);
        } else if (BundleHelper.isSerializable(supertypes)) {
            paramField = ParamField.asSerializable(paramName, fieldTypeMirror, isReachable);
        }
        return paramField;
    }

    public static ParamField asSerializable(String name, TypeMirror type, boolean isReachable) {
        return new ParamField(name, type, true, false, isReachable);
    }

    public static ParamField asParceable(String name, TypeMirror type, boolean isReachable) {
        return new ParamField(name, type, false, true, isReachable);
    }

    public static ParamField of(String name, TypeMirror type, boolean isReachable) {
        return new ParamField(name, type, false, false, isReachable);
    }

    ParamField(String name, TypeMirror type, boolean isSerializable, boolean isParcelable, boolean isReachable) {
        this.name = name;
        this.type = type;
        this.isSerializable = isSerializable;
        this.isParcelable = isParcelable;
        this.isReachable = isReachable;
    }
}
