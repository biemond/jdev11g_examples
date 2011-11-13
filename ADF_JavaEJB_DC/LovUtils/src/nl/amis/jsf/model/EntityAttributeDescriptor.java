package nl.amis.jsf.model;

import java.lang.reflect.Field;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.ColumnDescriptor;


import static nl.amis.jsf.model.EntityLovModelUtil.*;


public class EntityAttributeDescriptor<E> extends ColumnDescriptor {
    private Class entityClass;
    private Field field;
    private AttributeDescriptor.ComponentType componentType;
    private List<SelectItem> selectItems;
    private Object[] defaultValues;
    private String valueExpression;

    private boolean required = false;
    private boolean readOnly = false;
    private boolean removable = false;
    private int columnWidth = 0;

    class EntityOperator extends AttributeDescriptor.Operator {
        protected EntityOperatorType type;
        private Object value;
        
        public EntityOperator(EntityOperatorType type) {
            this.type = type;
        }
        
        public String getLabel() {
            String label = getResourceBundle().getString(type.getLabel());
            if (null == label || "".equals(label)) {
                return type.getLabel();
            } else {
                return label;
            }
        }
        
        public Object getValue() {
            if (null == value) {
                value = type.getValue();
            }
            return value;
        }
        
        public void setValue(Object value) {
            this.value = value;
        }
        
        public int getOperandCount() {
            return type.getOperandCount();
        }          
        
        public EntityOperatorType getType() {
            return type;
        }
        
        public String formatJpql(String attrName, List<Object> values) {
            
            Object[] params = new Object[values.size() + 1];
            params[0] = attrName;
            

            if (type.getOperandCount() == 1) {
                if (values.size() > 1) {
                    params[1] = createCommaSeparatedList(values);
                } else {
                    if (values.get(0) instanceof List) {
                        params[1] = createCommaSeparatedList((List)values.get(0));
                    } else {
                        params[1] = values.get(0);
                    }
                }
            } else {
                for (int i = 0; i < values.size(); i++) {
                    params[i+1] = values.get(i);
                }                
            }
            
            
            MessageFormat mf = new MessageFormat(type.getJpql());
            String result = mf.format(params);            
            return result;
        }
        
        private String createCommaSeparatedList(List objects) {
            StringBuilder array = new StringBuilder();
            for (int i = 0; i < objects.size(); i++) {
                Object object = objects.get(i);
                
                if (object instanceof String) {
                    array.append("'");                
                }
                array.append(object);
                if (object instanceof String) {
                    array.append("'");                
                }
                if (i < (objects.size() - 1)) {
                    array.append(",");
                }
            }
            return array.toString();
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof EntityAttributeDescriptor.EntityOperator)) {
                return false;
            }
            final EntityAttributeDescriptor.EntityOperator other = (EntityAttributeDescriptor.EntityOperator)object;
            if (!(type == null ? other.type == null : type.equals(other.type))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            final int PRIME = 37;
            int result = super.hashCode();
            result = PRIME * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }
    }
    
    // Operators for the Number dataType
    private final AttributeDescriptor.Operator DEFAULT_NUMBER_OPERATOR = new EntityOperator(EntityOperatorType.EQUALS_NUMBER);
    private final Set<AttributeDescriptor.Operator> NUMBER_OPERATORS = new LinkedHashSet<AttributeDescriptor.Operator>();  
    {
        NUMBER_OPERATORS.add(new EntityOperator(EntityOperatorType.EQUALS_NUMBER));
        NUMBER_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_EQUALS_NUMBER));
        NUMBER_OPERATORS.add(new EntityOperator(EntityOperatorType.GREATER_THAN_NUMBER));
        NUMBER_OPERATORS.add(new EntityOperator(EntityOperatorType.LESS_THAN_NUMBER));
        NUMBER_OPERATORS.add(new EntityOperator(EntityOperatorType.GREATER_THAN_OR_EQUAL_NUMBER));
        NUMBER_OPERATORS.add(new EntityOperator(EntityOperatorType.LESS_THAN_OR_EQUAL_NUMBER));
        NUMBER_OPERATORS.add(new EntityOperator(EntityOperatorType.BETWEEN_NUMBER));
    }

    private final AttributeDescriptor.Operator DEFAULT_NUMBER_OPERATOR_READ_ONLY = new EntityOperator(EntityOperatorType.EQUALS_NUMBER);
    private final Set<AttributeDescriptor.Operator> NUMBER_OPERATORS_READ_ONLY = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        NUMBER_OPERATORS_READ_ONLY.add(new EntityOperator(EntityOperatorType.EQUALS_NUMBER));
    }

    // Operators for the Date dataType
    private final AttributeDescriptor.Operator DEFAULT_DATE_OPERATOR = new EntityOperator(EntityOperatorType.BETWEEN_DATE);
    private final Set<AttributeDescriptor.Operator> DATE_OPERATORS = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        DATE_OPERATORS.add(new EntityOperator(EntityOperatorType.EQUALS_DATE));
        DATE_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_EQUALS_DATE));
        DATE_OPERATORS.add(new EntityOperator(EntityOperatorType.BETWEEN_DATE));
        DATE_OPERATORS.add(new EntityOperator(EntityOperatorType.GREATER_THAN_DATE));
        DATE_OPERATORS.add(new EntityOperator(EntityOperatorType.GREATER_THAN_OR_EQUAL_DATE));
        DATE_OPERATORS.add(new EntityOperator(EntityOperatorType.LESS_THAN_DATE));
        DATE_OPERATORS.add(new EntityOperator(EntityOperatorType.LESS_THAN_OR_EQUAL_DATE));
    }

    private final AttributeDescriptor.Operator DEFAULT_DATE_OPERATOR_READ_ONLY = new EntityOperator(EntityOperatorType.EQUALS_DATE);
    private final Set<AttributeDescriptor.Operator> DATE_OPERATORS_READ_ONLY = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        DATE_OPERATORS_READ_ONLY.add(new EntityOperator(EntityOperatorType.EQUALS_DATE));
    }

    // Operators for the String dataType
    private final AttributeDescriptor.Operator DEFAULT_STRING_OPERATOR = new EntityOperator(EntityOperatorType.CONTAINS);
    private final Set<AttributeDescriptor.Operator> STRING_OPERATORS = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        STRING_OPERATORS.add(new EntityOperator(EntityOperatorType.EQUALS_STRING));
        STRING_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_EQUALS_STRING));
        STRING_OPERATORS.add(new EntityOperator(EntityOperatorType.STARTS_WITH));
        STRING_OPERATORS.add(new EntityOperator(EntityOperatorType.ENDS_WITH));
        STRING_OPERATORS.add(new EntityOperator(EntityOperatorType.CONTAINS));
        STRING_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_CONTAINS));
    }
    
    private final AttributeDescriptor.Operator DEFAULT_STRING_OPERATOR_READ_ONLY = new EntityOperator(EntityOperatorType.EQUALS_STRING);
    private final Set<AttributeDescriptor.Operator> STRING_OPERATORS_READ_ONLY = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        STRING_OPERATORS_READ_ONLY.add(new EntityOperator(EntityOperatorType.EQUALS_STRING));
    }
    
    // Operators for the String dataType
    private final AttributeDescriptor.Operator DEFAULT_SELECT_ONE_OPERATOR = new EntityOperator(EntityOperatorType.EQUALS_STRING);
    private final Set<AttributeDescriptor.Operator> SELECT_ONE_OPERATORS = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
          SELECT_ONE_OPERATORS.add(new EntityOperator(EntityOperatorType.EQUALS_NUMBER));
          SELECT_ONE_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_EQUALS_NUMBER));
//        SELECT_ONE_OPERATORS.add(new EntityOperator(EntityOperatorType.EQUALS_STRING));
//        SELECT_ONE_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_EQUALS_STRING));
    }

    // Operators for the String dataType
    private final AttributeDescriptor.Operator DEFAULT_SELECT_MANY_OPERATOR = new EntityOperator(EntityOperatorType.IN);
    private final Set<AttributeDescriptor.Operator> SELECT_MANY_OPERATORS = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        SELECT_MANY_OPERATORS.add(new EntityOperator(EntityOperatorType.IN));
        SELECT_MANY_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_IN));
    }
  
    // TODO: hebben we deze eigenlijk wel nodig?
    // Operators for the Boolean dataType
    private final AttributeDescriptor.Operator DEFAULT_BOOLEAN_OPERATOR = new EntityOperator(EntityOperatorType.EQUALS_NUMBER);
    private final Set<AttributeDescriptor.Operator> BOOLEAN_OPERATORS = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        BOOLEAN_OPERATORS.add(new EntityOperator(EntityOperatorType.EQUALS_NUMBER));
        BOOLEAN_OPERATORS.add(new EntityOperator(EntityOperatorType.NOT_EQUALS_NUMBER));
    }    
    
    // Operators for the other dataTypes
    private final AttributeDescriptor.Operator DEFAULT_NO_OPERATOR = new EntityOperator(EntityOperatorType.NO_OPERATOR);
    private final Set<AttributeDescriptor.Operator> NO_OPERATORS = new LinkedHashSet<AttributeDescriptor.Operator>();
    {
        NO_OPERATORS.add(new EntityOperator(EntityOperatorType.NO_OPERATOR));
    }    
    
    public EntityAttributeDescriptor(Class entityClass, Field field) {
        super();
        this.entityClass = entityClass;
        this.field = field;          
    }
    
    public EntityAttributeDescriptor(Class entityClass, String name) throws NoSuchFieldException {
        this(entityClass, entityClass.getDeclaredField(name));
    }
    
    public static EntityAttributeDescriptor getInstance(Class entityClass, String name) throws NoSuchFieldException {
        EntityAttributeDescriptor result = new EntityAttributeDescriptor(entityClass, name);
        return result;
    }

    public AttributeDescriptor.ComponentType getComponentType() {
        if (readOnly) {
            return AttributeDescriptor.ComponentType.selectOneChoice;
        }
        if (null != componentType) {
            return componentType;
        }
        if (EntityLovModelUtil.isDate(getType())) {
            return AttributeDescriptor.ComponentType.inputDate;
        }
        return AttributeDescriptor.ComponentType.inputText;
    }
    
    public void setComponentType(AttributeDescriptor.ComponentType componentType) {
        this.componentType = componentType;
    }
    
    @Override
    public Converter getConverter() {
        if (null != selectItems) {
            return new Converter() {
                public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {
                    if (null != selectItems) {
                        for (SelectItem item : selectItems) {
                            if (item.getLabel().equals(string)) {
                                return item.getValue();
                            }
                        }
                    }
                    return string;
                }
    
                public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
                    if (null != selectItems) {
                        for (SelectItem item : selectItems) {
                            if (item.getValue().equals(object)) {
                                return item.getLabel();
                            }
                        }
                    }
                    return object.toString();
                }
            };
        } else if (isNumericType()){
            return new NumberConverter();
        } else if (isDateType()) {
            return new DateTimeConverter();
        } else { 
            return new Converter() {
                public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {
                    return string;
                }
                public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
                    return object.toString();
                }
            };
        }
    }
  
    @Override
    public boolean hasDefaultConverter() {
        return !(   getComponentType() == AttributeDescriptor.ComponentType.selectManyChoice
                 || getComponentType() == AttributeDescriptor.ComponentType.selectOneChoice
                 || getComponentType() == AttributeDescriptor.ComponentType.selectOneListbox
                 || getComponentType() == AttributeDescriptor.ComponentType.selectOneRadio);
    }   

    public String getDescription() {
        String label = getResourceBundle().getString(getResourceKey("_TOOLTIP"));
        if (null == label || "".equals(label)) {
            return field.getName();
        } else {
            return label;
        }                   
    }
    
    public String getFormat() {
        return null;
    }    

    public String getLabel() {
        String label = getResourceBundle().getString(getResourceKey("_LABEL"));
        if (null == label || "".equals(label)) {
            return field.getName();
        } else {
            return label;
        }                   
    }
    
    private ResourceBundle getResourceBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getResourceBundle(context, "resourceBundle");      
    }
    
    private String getResourceKey(String append) {
        StringBuilder key = new StringBuilder();
        key.append(field.getDeclaringClass().getSimpleName());
        key.append(".");
        key.append(field.getName());
        key.append(append);
        return key.toString();
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }
    
    public int getColumnWidth() {
        return columnWidth;
    }
    
    public int getWidth() {
        return getColumnWidth();
    }

    public String getAlign() {
        if (isNumericType() || isDateType()) {
            return "right";
        } else {
            return "left";
        }
    }    
  
    public int getLength() {
        // TODO: @Length annotatie opzoeken?
        return 0;
    }

    public int getMaximumLength() {
        // TODO: @Length annotatie opzoeken?
        return 0;
    }
    
    Object[] getDefaultValues() {
        return defaultValues;
    }
    
    void setDefaultValues(Object ... defaultValues) {
        this.defaultValues = defaultValues;
    }
    
    String getValueExpression() {
        return valueExpression;
    }
    
    void setValueExpression(String expression) {
        this.valueExpression = expression;
    }
    
    /**
     * Returns the model object that represents the data for the component.
     * 
     * For inputListOfValues/inputComboboxListOfValues, it's a ListOfValuesModel
     * For inputNumberSpinbox, it's a NumberRange object
     * For selectXyz components, it must be List<SelectItem>
     * Undefined for other types
     */
    public Object getModel() {
        if (readOnly) {
            return selectItems;
        }
        if (  getComponentType().equals(AttributeDescriptor.ComponentType.selectOneListbox) ||
              getComponentType().equals(AttributeDescriptor.ComponentType.selectManyChoice) ||
              getComponentType().equals(AttributeDescriptor.ComponentType.selectOneChoice) ||
              getComponentType().equals(AttributeDescriptor.ComponentType.selectOneRadio)) {
            
            return selectItems;
        } else {
            return null;
        }
    }
    
    public void setModel(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public String getName() {
        return field.getName();
    }

    public AttributeDescriptor.Operator getOperatorByType(EntityOperatorType type) {
        System.out.println("base: "+type.toString());
        Set<AttributeDescriptor.Operator> operators = getSupportedOperators();
        for (AttributeDescriptor.Operator operator : operators) {
            System.out.println("compare: "+((EntityOperator)operator).getType().toString());
            if (type.equals(((EntityOperator)operator).getType())) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Operator " + type + " not valid for attribute of type " + getType());
    }

    public Set<AttributeDescriptor.Operator> getSupportedOperators() {
        if (!this.readOnly && getComponentType().equals(AttributeDescriptor.ComponentType.selectManyChoice)) {
            return SELECT_MANY_OPERATORS;
        } else if (  !this.readOnly 
                     &&  (   getComponentType().equals(AttributeDescriptor.ComponentType.selectOneListbox) 
                         ||  getComponentType().equals(AttributeDescriptor.ComponentType.selectOneChoice)
                         ||  getComponentType().equals(AttributeDescriptor.ComponentType.selectOneRadio))) {
            return SELECT_ONE_OPERATORS;
        } else if (getType().equals(Boolean.class)) {
            return BOOLEAN_OPERATORS;
        } else if (EntityLovModelUtil.isNumber(getType())) {
            return this.readOnly ? NUMBER_OPERATORS_READ_ONLY : NUMBER_OPERATORS;
        } else if (getType().equals(String.class)) {
            return this.readOnly ? STRING_OPERATORS_READ_ONLY : STRING_OPERATORS;
        } else if (EntityLovModelUtil.isDate(getType())) {
            return this.readOnly ? DATE_OPERATORS_READ_ONLY : DATE_OPERATORS;
        }
        return NO_OPERATORS;
    }
    
    AttributeDescriptor.Operator getDefaultOperator() {
      if (!this.readOnly && getComponentType().equals(AttributeDescriptor.ComponentType.selectManyChoice)) {
          return DEFAULT_SELECT_MANY_OPERATOR;
      } else if (  !this.readOnly 
                   &&  (   getComponentType().equals(AttributeDescriptor.ComponentType.selectOneListbox) 
                       ||  getComponentType().equals(AttributeDescriptor.ComponentType.selectOneChoice)
                       ||  getComponentType().equals(AttributeDescriptor.ComponentType.selectOneRadio))) {
          return DEFAULT_SELECT_ONE_OPERATOR;
      } else if (getType().equals(Boolean.class)) {
          return DEFAULT_BOOLEAN_OPERATOR;
      } else if (EntityLovModelUtil.isNumber(getType())) {
          return this.readOnly ? DEFAULT_NUMBER_OPERATOR_READ_ONLY : DEFAULT_NUMBER_OPERATOR;
      } else if (getType().equals(String.class)) {
          return this.readOnly ? DEFAULT_STRING_OPERATOR_READ_ONLY : DEFAULT_STRING_OPERATOR;
      } else if (EntityLovModelUtil.isDate(getType())) {
          return this.readOnly ? DEFAULT_DATE_OPERATOR_READ_ONLY : DEFAULT_DATE_OPERATOR;
      }
      return DEFAULT_NO_OPERATOR;
    }


    public boolean isNumericType() {
        return EntityLovModelUtil.isNumber(getType());
    }
    
    public boolean isDateType() {
        return EntityLovModelUtil.isDate(getType());
    }

    public Class getType() {
        return field.getType();
    }

    // TODO: deze methode wordt blijkbaar niet aangeroepen, daardoor blijft
    //       het veld editable, ook al wordt readOnly op true gezet. Uitzoeken
    //       of er een work around is.
    public boolean isReadOnly() {
        return readOnly;
    }
    
    public void setReadOnly(boolean readOnly, Object ... values) {
        if (readOnly) {
            if (null == values) {
                throw new IllegalArgumentException("Value cannot be null if setting attribute to read only.");
            }
            selectItems = new ArrayList<SelectItem>();
            for (Object value : values) {
                selectItems.add(new SelectItem(value, "" + value));
            }
        }
        this.readOnly = readOnly;
    }

    public boolean isRequired() {
        return required || readOnly;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    public void setRemovable(boolean removable) {
        this.removable = removable;
    }
  
    public boolean isRemovable() {
        return removable;
    }
  
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EntityAttributeDescriptor)) {
            return false;
        }
        final EntityAttributeDescriptor other = (EntityAttributeDescriptor)object;
        if (!(entityClass == null ? other.entityClass == null : entityClass.getName().equals(other.entityClass.getName()))) {
            return false;
        }
        if (!(field == null ? other.field == null : field.equals(other.field))) {
            return false;
        }
        return true;
    }
  
    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = super.hashCode();
        result = PRIME * result + ((entityClass == null) ? 0 : entityClass.getName().hashCode());
        result = PRIME * result + ((field == null) ? 0 : field.hashCode());
        return result;
    }
}

