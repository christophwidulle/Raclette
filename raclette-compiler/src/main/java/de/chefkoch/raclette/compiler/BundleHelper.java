package de.chefkoch.raclette.compiler;

import de.chefkoch.raclette.compiler.params.ParamField;

import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class BundleHelper {


    static Map<String, TypeMapping> types = new HashMap<>();

    public enum MethodType {
        Getter, Setter
    }


    public static String findBundleMethod(MethodType methodType, ParamField paramField) {
        String name = paramField.type.toString();
        String method = null;
        TypeMapping typeMapping = types.get(name);
        if (typeMapping != null) {
            method = typeMapping.bundleMethodName;
        } else if (paramField.isParcelable) {
            method = "Parcelable";
        }
        return methodType == MethodType.Getter ? "get" + method : "put" + method;
    }


    static {
        add("java.lang.String", "String");

        add("java.lang.Integer", "Int");
        add("int", "Int");

        add("java.lang.Boolean", "Boolean");
        add("boolean", "Boolean");

        add("java.lang.Byte", "Byte");
        add("byte", "Byte");

        add("java.lang.Float", "Float");
        add("float", "Float");

        add("java.lang.Long", "Long");
        add("long", "Long");

        add("java.lang.Double", "Double");
        add("double", "Double");

        add("java.lang.Character", "Char");
        add("char", "Char");
    }

    public static boolean isSimpleType(TypeMirror typeMirror) {
        return types.containsKey(typeMirror.toString());
    }

    static void add(String typeName, String bundleMethodName) {
        add(new TypeMapping(typeName, bundleMethodName));
    }

    static void add(TypeMapping typeMapping) {
        types.put(typeMapping.typeName, typeMapping);
    }

    static class TypeMapping {
        String typeName;
        String bundleMethodName;

        public TypeMapping(String typeName, String bundleMethodName) {
            this.typeName = typeName;
            this.bundleMethodName = bundleMethodName;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getBundleMethodName() {
            return bundleMethodName;
        }
    }


}


