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


    public static ParamField from(String paramName, TypeMirror fieldTypeMirror, List<? extends TypeMirror> supertypes) {
        ParamField paramField = null;
        boolean simpleType = BundleHelper.isSimpleType(fieldTypeMirror);
        if (simpleType) {
            paramField = ParamField.of(paramName, fieldTypeMirror);
        } else if (BundleHelper.isParceble(supertypes)) {
            paramField = ParamField.asParceable(paramName, fieldTypeMirror);
        } else if (BundleHelper.isSerializable(supertypes)) {
            paramField = ParamField.asSerializable(paramName, fieldTypeMirror);
        }
        return paramField;
    }

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
