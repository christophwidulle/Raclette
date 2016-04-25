package de.chefkoch.raclette.compiler.params;

import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ParamsContext {

    public static final ParamsContext NONE = new ParamsContext(null, null, new ArrayList<ParamField>());

    final String viewModelClassName;
    final TypeMirror viewModelType;
    final List<ParamField> fields;

    public ParamsContext(String viewModelClassName, TypeMirror viewModelType, List<ParamField> fields) {
        this.viewModelClassName = viewModelClassName;
        this.viewModelType = viewModelType;
        this.fields = fields;
    }

    public TypeMirror getViewModelType() {
        return viewModelType;
    }

    public String getViewModelClassName() {
        return viewModelClassName;
    }

    public List<ParamField> getFields() {
        return fields;
    }
}
