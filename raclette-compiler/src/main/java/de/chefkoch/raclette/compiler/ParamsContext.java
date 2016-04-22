package de.chefkoch.raclette.compiler;

import java.util.List;

/**
 * Created by christophwidulle on 22.04.16.
 */
public class ParamsContext {

    String forViewModelClass;
    List<ParamField> fields;

    public ParamsContext(String forViewModelClass, List<ParamField> fields) {
        this.forViewModelClass = forViewModelClass;
        this.fields = fields;
    }

    public String getForViewModelClass() {
        return forViewModelClass;
    }

    public List<ParamField> getFields() {
        return fields;
    }
}
