package nl.amis.jsf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.AttributeDescriptor;



public class EntityCriterion extends AttributeCriterion {
    private AttributeDescriptor.Operator operator;
    private EntityAttributeDescriptor descriptor;
    private Map<String, AttributeDescriptor.Operator> operators;
    private List<Object> values;

    public EntityCriterion() {
        super();        
    }
    
    public EntityCriterion(AttributeDescriptor descriptor) {
        this.descriptor = (EntityAttributeDescriptor)descriptor;
    }

    public AttributeDescriptor getAttribute() {
        return descriptor;
    }

    public AttributeDescriptor.Operator getOperator() {
        if (null == operator) {
            if (null != descriptor) {
                operator = descriptor.getDefaultOperator();
            }
        }
        return operator;
    }

    public void setOperator(AttributeDescriptor.Operator operator) {  
        this.operator = operator;
    }
    
    public void setOperator(EntityOperatorType type) {
        this.operator = descriptor.getOperatorByType(type);
    }

    public Map<String, AttributeDescriptor.Operator> getOperators() {
        if (null == operators) {
            operators = new HashMap<String, AttributeDescriptor.Operator>();
            Set<AttributeDescriptor.Operator> os = descriptor.getSupportedOperators();
            for (AttributeDescriptor.Operator o : os) {
                operators.put(o.getLabel(), o);
            }
        }
        return operators;
    }

    public List<Object> getValues() {
        if (null != descriptor.getValueExpression()) {
            Object value = resolveExpression(descriptor.getValueExpression());
            if (value instanceof List) {
                values = (List)value;
            } else {
                values = new ArrayList<Object>();
                values.add(value);
            }
        }
        
        if (null == values) {
            values = new ArrayList<Object>();
      
            Object[] defaultValues = descriptor.getDefaultValues();
            
            if (null != defaultValues && defaultValues.length > 0) {
                values.addAll(Arrays.asList(descriptor.getDefaultValues()));            
            } else if (null != getOperator()) {
                for (int i = 0; i < getOperator().getOperandCount(); i++) {
                    values.add(getOperator().getValue());
                }
            }
        }
        return values;
    }
    
    private static Object resolveExpression(String expression) {
  
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp =elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }    

    public void setValue(Object value) {
        this.values = new ArrayList<Object>();
        this.values.add(value);
    }

    public boolean isRemovable() {
        return descriptor.isRemovable();
    }
    
    public boolean isReadOnly() {
        return descriptor.isReadOnly();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EntityCriterion)) {
            return false;
        }
        final EntityCriterion other = (EntityCriterion)object;
        if (!(operator == null ? other.operator == null : operator.equals(other.operator))) {
            return false;
        }
        if (!(descriptor == null ? other.descriptor == null : descriptor.equals(other.descriptor))) {
            return false;
        }
        if (!(values == null ? other.values == null : values.equals(other.values))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = super.hashCode();
        result = PRIME * result + ((operator == null) ? 0 : operator.hashCode());
        result = PRIME * result + ((descriptor == null) ? 0 : descriptor.hashCode());
        result = PRIME * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

}
