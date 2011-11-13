package nl.amis.jsf.model;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class EntityValidator implements Validator {
    public EntityValidator() {
        super();
    }

    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
    }
}
