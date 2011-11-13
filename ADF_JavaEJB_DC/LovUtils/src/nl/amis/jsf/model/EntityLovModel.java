package nl.amis.jsf.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;

import javax.naming.NamingException;


import nl.amis.model.hr.interfaces.LovService;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.ListOfValuesModel;
import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.QueryModel;
import oracle.adf.view.rich.model.TableModel;


import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierBinding;

import oracle.binding.BindingContainer;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;

import static nl.amis.jsf.model.EntityLovModelUtil.*;


public class EntityLovModel<E> extends ListOfValuesModel {

    
    private Class entityClass;
    private QueryModel queryModel;
    private TableModel tableModel;
    private Field key;
    private Method keyGetter;
    
    private List<E> cache;
    private int queryHashCode;
    private boolean whereClauseAdded = false;
    
    private Converter converter = null;
    private Validator validator = null;
    
    private LovService<E> lovService;
    private String serviceJndiName;
    private Class serviceClass;
    private boolean advanced = false;
    
    private List<EntityCriterion> hiddenCriteria;
    private boolean returnKey;
    private boolean hasExpression = false;

    /**
     * Type of information to show in a single- or multi select control.
     */
    public enum ShowType {
        /** Show only the domain value. This is typically a numeric value or a short alphanumeric code. */
        SHOW_VALUE("value"),
        /** Show only the description. This is typcially a short, human understandable description of the meaning of the code. */
        SHOW_DESCRIPTION("description"),
        /** Show both the code and the description */
        SHOW_BOTH("both");
        
        private String showType;
        
        private ShowType(String showType) {
            this.showType = showType;    
        }
        
        public String getShowType() {
            return showType;
        }
    }
    
    /**
     * Build a new EntityLovModel. Example usage:
     * <code>
     *   EntityLovModel.Builder(MyEntity.class, "keyField", LAND.NL, "XyzServicesBean#nl.arval.hippos.relatie.services.XyzServices")
     *        .add(new FieldDef("naam").required())
     *        .add(new FieldDef("nawType").singleSelect().domain("NAWT").selectValues("PROS", "SUSP"))
     *        .add(new FieldDef("datLtstMut").operator(EntityOperatorType.GREATER_THAN_DATE)));
     * </code>
     * 
     * @author Bart Kummel
     */
    public static class Builder<E> {        
        private Class entityClass;
        private String serviceJndiName;
        private Class serviceClass;
        private List<FieldDef> queryFields;
        private boolean advanced = false;
        private boolean returnKey = false;
        
        /**
         * FieldDef semi-builder. To keep things simple, this does not implement the full 
         * builder pattern. Just create a new FieldDef by calling the constructor and
         * add optional parameters by calling the appropriate methods.
         */
        public static class FieldDef {
            String name;
            boolean key = false;
            boolean readOnly = false;
            boolean required = false;
            Object[] value;
            String valueExpression;
            boolean singleSelect = false;
            boolean multiSelect = false;
            String domain;
            String domainKeyField;
            String domainValueFields;

            ShowType showType;
            Object[] selectValues;

            EntityOperatorType defaultOperator;
            int columnWidth = 0;
            boolean hidden = false;
            boolean optional = false;
            boolean tableOnly = false;
            
            /**
             * Create a new FieldDef, given the field name.
             * @param name Name of the field. Must correspond to a field of the
             *             entity class the EntityLovModel is created for.
             */
            public FieldDef(String name) {
                this.name = name;
            }
            
            /**
             * Mark this field as a required field. By default, fields are optional.
             * @return
             */
            public FieldDef required() {
                this.required = true;
                return this;
            }
            
            /**
             * Mark this field as the key field. The key field is the value that is
             * displayed in the LOV input field.
             * @return
             */
            public FieldDef key() {
                this.key = true;
                return this;
            }
            
            /**
             * Mark this field as a read only field.
             * @return
             */
            public FieldDef readOnly() {
                this.readOnly = true;
                return this;
            }
            
            /**
             * Mark this criterion as hidden. This means it won't be shown in 
             * the LOV popup and the user can't change the value. Hence, hidden
             * implies readlonly and needs a value.
             * @return
             */
            public FieldDef hidden() {
                this.hidden = true;
                return this;
            }
            
            /**
             * Mark this criterion as optional. That means hte search field is
             * hidden initially, but can be added by the user afterwards, if the
             * LOV popup is in advanced mode.
             * @return
             */
            public FieldDef optional() {
                this.optional = true;
                return this;
            }
            
            /**
             * Mark this as a "display only" attribute. That means it won't be
             * used as criterion in the query, but it will be displayed in the 
             * result table. Useful for descriptions etc.
             * @return
             */
            public FieldDef tableOnly() {
                this.tableOnly = true;
                return this;
            }
            
            /**
             * Set the initial value of the field. In case of multi select fields,
             * this can be an array of values.
             * @param value
             * @return
             */
            public FieldDef value(Object ... value) {
                this.value = value;
                this.valueExpression = null;
                return this;
            }
            
            /**
             * Set a JSF EL expression that will be evaluated to get the value
             * for a field. This is intended to be used with hidden fields.
             * You cannot have both a value and a valueExpression on one field.             *
             * @param expression
             * @return
             */
            public FieldDef valueExpression(String expression) {
                this.valueExpression = expression;
                this.value = null;
                return this;
            }
            
            /**
             * Set the column width of the field as displayed in the table
             * of search results.
             * 
             * @param columnWidth
             * @return
             */
            public FieldDef columnWidth(int columnWidth) {
                this.columnWidth = columnWidth;
                return this;
            }
            
            /**
             * Mark this field as a single select field, using the given domain
             * name to get the available values. The values are by default looked up
             * in the OMSCHR_TABEL table, unless you call {@link #fromOmschrLabel()}.
             * By default, the value AND the label are shown in the select
             * component, unless you set another behavior by calling {@link #showType(ShowType)}.
             * @param domain
             * @return
             */
            public FieldDef singleSelect(String domain, String keyField ,String valueFields) {
                this.singleSelect = true;
                this.multiSelect = false;
                this.domain = domain;
                this.domainKeyField = keyField;
                this.domainValueFields = valueFields;
                this.showType = ShowType.SHOW_BOTH;
                return this;
            }

            /**
             * Mark this field as a multi select field, using the given domain
             * name to get the available values. The values are by default looked up
             * in the OMSCHR_TABEL table, unless you call {@link #fromOmschrLabel()}.
             * By default, the value AND the label are shown in the select
             * component, unless you set another behavior by calling {@link #showType(ShowType)}.
             * @param domain
             * @return
             */
            public FieldDef multiSelect(String domain) {
                this.singleSelect = false;
                this.multiSelect = true;
                this.domain = domain;

                this.showType = ShowType.SHOW_BOTH;
                return this;
            }


            /**
             * Configure what should be shown in a single- or multi select control.
             * @param showType
             * @return
             */
            public FieldDef showType(ShowType showType) {
                this.showType = showType;
                return this;
            }
            
            /**
             * Limit the values available for selection in a select control to the
             * given values. 
             * @param selectValues
             * @return
             */
            public FieldDef selectValues(Object ... selectValues) {
                this.selectValues = selectValues;
                return this;
            }
            
            /**
             * Set a default operator for the field.
             * @param operator
             * @return
             */
            public FieldDef operator(EntityOperatorType operator) {
                this.defaultOperator = operator;
                return this;
            }
        }
        
        /**
         * Create a new builder for EntityLovModel instances. Note that a single builder can 
         * create multiple EnityLovModel instances: everytime {@link #build()} is called, a
         * new instance is created. This way, multiple instances can be created that (paritally)
         * share the same settings.
         * 
         * @param entityClass       Entity class the LOV control will be based on. This means
         *                          the LOV can be used to search for entities of this class. 
         * @param land              Database schema to use.
         * @param serviceJndiName  The JNDI name that can be used to lookup the {@link LovService}
         *                          that should be used to query for entities.
         */
        public Builder(Class entityClass,  String serviceJndiName, Class serviceClass) {
            this.entityClass = entityClass;
            this.serviceJndiName = serviceJndiName;
            this.serviceClass = serviceClass;
            this.queryFields = new ArrayList<FieldDef>();
        }
        
        /**
         * Create a new {@link EntityLovModel} instance.
         * @return
         */
        public EntityLovModel<E> build() {
            int keys = 0;
            for (FieldDef fieldDef : queryFields) {
                if (fieldDef.key) {
                    keys++;
                }
            }
            if (keys < 1) {
                throw new IllegalArgumentException("No key field set for EntityLovModel<" + entityClass.getName() + ">.");
            } else if (keys > 1) {
                throw new IllegalArgumentException("More than one key field set for EntityLovModel<" + entityClass.getName() + ">.");
            }
            EntityLovModel<E> result = new EntityLovModel<E>(entityClass, queryFields,  serviceJndiName, serviceClass, advanced, returnKey);
            return result;
        }
        
        /**
         * Add a field definition. The field definitions determine which field can be 
         * used to search for entities in the LOV control. See {@link FieldDef} for
         * more information. Typcial usage: 
         * <code>
         *   EntityLovModel.Builder(MyEntity.class, LAND.NL, "XyzServicesBean#nl.arval.hippos.relatie.services.XyzServices", XyzServices.class)
         *        .add(new FieldDef("nawnr").key())
         *        .add(new FieldDef("naam").required())
         *        .add(new FieldDef("nawType").singleSelect().domain("NAWT").selectValues("PROS", "SUSP"))
         *        .add(new FieldDef("datLtstMut").operator(EntityOperatorType.GREATER_THAN_DATE)));
         * </code>
         * @param fieldDef
         * @return
         */
        public Builder add(FieldDef fieldDef) {
            queryFields.add(fieldDef);
            return this;
        }

        /**
         * Enable the advanced features in the LOV dialog, such as "add fields".
         * 
         * @return
         */
        public Builder advanced() {
            this.advanced = true;
            return this;
        }
        
        /**
         * The LOV will return the key value, instead of the selected entity object.
         * 
         * @return
         */
        public Builder returnKey() {
            this.returnKey = true;
            return this;
        }
    }
    
    private EntityLovModel(Class entityClass, List<Builder.FieldDef> fieldDefs, String serviceJndiName, Class serviceClass, boolean advanced, boolean returnKey) {
        super();
        System.out.println("<init>("+entityClass+")");
        this.entityClass = entityClass;      

        this.serviceJndiName = serviceJndiName;
        this.serviceClass = serviceClass;
        this.advanced = advanced;
        this.returnKey = returnKey;

        if (!LovService.class.isAssignableFrom(serviceClass)) {
            throw new IllegalArgumentException("Service should implement LovService interface! " + serviceClass);
        }
        initFields(fieldDefs);
    }
    
    private void initFields(List<Builder.FieldDef> fieldDefs) {
        System.out.println("initFields(): " + fieldDefs.size() + " fields to initialize...");
        EntityQueryModel model = (EntityQueryModel)getQueryModel();
        List<Criterion> criteria = model.getCurrentDescriptor().getConjunctionCriterion().getCriterionList();

        for (Builder.FieldDef fieldDef : fieldDefs) {
            addField(fieldDef, criteria);
        }
    }
    
    private EntityCriterion addField(Builder.FieldDef fieldDef, List<Criterion> criteria) {
        Field field;
        EntityCriterion criterion = null;
        
        try {
            field = entityClass.getDeclaredField(fieldDef.name);
            System.out.println("    Trying to add field '" + fieldDef.name + "'...");
            if (EntityLovModelUtil.isFieldAccessible(field, entityClass)) {
                System.out.println("    Field '" + fieldDef.name + " type " +fieldDef.showType+ "' in entity " + entityClass.getName() + " is accessible.");

                if (fieldDef.key) {
                    this.key = field;
                    initKey();
                }

                EntityAttributeDescriptor attribute = EntityAttributeDescriptor.getInstance(entityClass, fieldDef.name);
                
                if (fieldDef.readOnly) {
                    attribute.setReadOnly(fieldDef.readOnly, fieldDef.value);
                }
                attribute.setRequired(fieldDef.required);
                attribute.setColumnWidth(fieldDef.columnWidth);
                if (fieldDef.optional) {
                    attribute.setRemovable(fieldDef.optional);
                    // If there is at least one optional field, start the LOV
                    // dialog in advanced mode by default.
                    this.advanced = true;
                    ((EntityQueryModel)getQueryModel()).getCurrentDescriptor().changeMode(QueryDescriptor.QueryMode.ADVANCED);
                }
                attribute.setDefaultValues(fieldDef.value);
                attribute.setValueExpression(fieldDef.valueExpression);
                if (null != fieldDef.valueExpression) {
                    hasExpression = true;
                }
                
                criterion = new EntityCriterion(attribute);

                if (fieldDef.singleSelect || fieldDef.multiSelect) {
                    setFieldDomain(criterion, fieldDef);
                }
                
                if (null != fieldDef.defaultOperator) {
                    criterion.setOperator(fieldDef.defaultOperator);
                }
                
                // Don't show read only fields in the results table, as they
                // would contain the same value in every row. But if the 
                // column width is set explicitely, show them anyway.
                if ((!(fieldDef.readOnly || fieldDef.hidden) || fieldDef.columnWidth > 0) && fieldDef.columnWidth != -1) {
                    ((EntityTableModel)getTableModel()).addColumDescriptor(attribute);
                }
                
                if (null != criterion) {
                    if (fieldDef.hidden) {
                        if (null == hiddenCriteria) {
                            hiddenCriteria = new ArrayList<EntityCriterion>();                        
                        }
                        hiddenCriteria.add(criterion);
                    } else {
                        if (!fieldDef.tableOnly) {
                            if (!fieldDef.optional || null != fieldDef.value) {
                                criteria.add(criterion);
                            }
                            ((EntityQueryModel)getQueryModel()).addAttribute(attribute);
                        }
                    }
                }                    
            } else {
                System.out.println("Field '" + fieldDef.name + "' in entity " + entityClass.getName() + " is NOT accessible!");
            }
        } catch (NoSuchFieldException e) {
            System.out.println("Field '" + fieldDef.name + "' not found in entity class " + entityClass.getName() + "!");   
        }
        
        return criterion;        
    }
    
    private void initKey() {
        System.out.println("initKey() for key " + key.getClass().getSimpleName() + " " + key.toString());
        if (this.key.getType().equals(Boolean.class) || (this.key.getType().isPrimitive() && this.key.getType().equals(boolean.class))) {
            keyGetter = EntityLovModelUtil.getAccessorMethod("is", this.key.getName(), entityClass);
        }
        if (null == keyGetter) {
            keyGetter = EntityLovModelUtil.getAccessorMethod("get", this.key.getName(), entityClass);
        }
        ((EntityCollectionModel)((EntityTableModel)getTableModel()).getCollectionModel()).setKeyGetter(keyGetter);
//        ((EntityConverter)getConverter()).setKeyGetter(keyGetter);
        System.out.println("    keyGetter = " + keyGetter);  
    }
    
    private void setFieldDomain(EntityCriterion criterion, Builder.FieldDef fieldDef) {
        System.out.println("setFieldDomain("+fieldDef.name+","+fieldDef.domain+","+")");
        EntityAttributeDescriptor attr = (EntityAttributeDescriptor)criterion.getAttribute();
      
      
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();      
        FacesCtrlHierBinding treeData = (FacesCtrlHierBinding)bindings.getControlBinding(fieldDef.domain); 
        if ( treeData == null) {
          throw new IllegalArgumentException("ADF tree binding is null: "+fieldDef.domain);
        }
      
        List<SelectItem> list = new ArrayList<SelectItem>();
        Row[] rows = treeData.getAllRowsInRange();  
        for (Row row : rows) {  
          list.add(new SelectItem( row.getAttribute(fieldDef.domainKeyField), (String)row.getAttribute(fieldDef.domainValueFields)));  
        }  

        System.out.println("showtype: "+fieldDef.showType.getShowType() + " is numeric: "+ attr.isNumericType());
        
        if (fieldDef.multiSelect) {
            attr.setComponentType(AttributeDescriptor.ComponentType.selectManyChoice);
            criterion.setOperator(EntityOperatorType.IN);
            if (null != fieldDef.value) {
                ArrayList<Object> values = new ArrayList<Object>();
                for (Object value : fieldDef.value) {
                    values.add(value);    
                }
                attr.setDefaultValues(values);
            }
        } else {
            attr.setComponentType(AttributeDescriptor.ComponentType.selectOneChoice);
            criterion.setOperator(EntityOperatorType.EQUALS_NUMBER);
        }
        attr.setModel(list);
    }
      
    private EntityCriterion getCriterionByName(String criterionName) {
        if (null == criterionName || "".equals(criterionName)) {
            throw new IllegalArgumentException("Criterion name can't be empty.");
        }
        EntityQueryModel model = (EntityQueryModel)getQueryModel();
        List<Criterion> criteria = model.getCurrentDescriptor().getConjunctionCriterion().getCriterionList();
        for (Criterion criterion : criteria) {
            EntityAttributeDescriptor attr = (EntityAttributeDescriptor)((EntityCriterion)criterion).getAttribute();
            if (criterionName.equals(attr.getName())) {
                return (EntityCriterion)criterion;
            }
        }
        
        throw new IllegalArgumentException("Criterion with name '" + criterionName + "' not found.");
    }

    public Converter getConverter() {
        if (null == converter) {
            //            converter = new EntityConverter((EntityCollectionModel)getTableModel().getCollectionModel(), keyGetter, entityClass, returnKey);
            converter = new Converter() {

                    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {
                        System.out.println("converter getAsObject(" + string + ")");
                        if (null == string || "".equals(string)) {
                            System.out.println("    null");
                            return null;
                        }
                        

                        Object key = null;
                        if (String.class.equals(keyGetter.getReturnType())) {
                            key = string;
                        } else if (EntityLovModelUtil.isNumber(keyGetter.getReturnType())) {
                            Constructor c;
                            try {
                                c = keyGetter.getReturnType().getConstructor(String.class);
                                key = c.newInstance(string);
                            } catch (NoSuchMethodException e) {
                            } catch (InstantiationException e) {
                            } catch (IllegalAccessException e) {
                            } catch (InvocationTargetException e) {
                            }
                        }

                        System.out.println("converter    key = " + logObject(key));
                        Object result;
                        if (returnKey) {
                            result = key;
                        } else {
                            result = ((EntityCollectionModel)getTableModel().getCollectionModel()).getRowData(key);
                        }

                        System.out.println("converter    result = " + logObject(result));
                        return result;
                    }

                    public String getAsString(FacesContext facesContext,
                                              UIComponent uiComponent, 
                                              Object object) {
                      if (object == null)
                          return "";
                      return object.toString();
                   }


                };
        }
        return converter;
    }
    
    public void validate(FacesContext context, UIComponent uiComponent, Object value) {
        getValidator().validate(context, uiComponent, value);
    }
    
    public Validator getValidator() {   
        if (null == validator) {
            if (isNumber(keyGetter.getReturnType())) {
                validator = new LongRangeValidator(Long.MAX_VALUE);                
            } else {
                validator = new EntityValidator();
            }
        }
        return validator;
    }
    
    public QueryDescriptor getQueryDescriptor() {
        return ((EntityQueryModel)getQueryModel()).getCurrentDescriptor();
    }

    public QueryModel getQueryModel() {
        if (null == queryModel) {
            queryModel = new EntityQueryModel(entityClass, advanced);
        }
        return queryModel;
    }

    public TableModel getTableModel() {
        if (null == tableModel) {
            tableModel = new EntityTableModel(entityClass) {
                    public List getFilteredList() {
                        return EntityLovModel.this.getFilteredList();
                    }

                    public boolean isJpqlChanged() {
                        return EntityLovModel.this.isJpqlChanged();
                    }
                };
        }
        return tableModel;
    }
    
    private boolean isJpqlChanged() {
        boolean result = cache == null || hasExpression || queryHashCode != getQueryDescriptor().getConjunctionCriterion().hashCode();
        return result;
    } 
    
    List<E> getFilteredList() {
        System.out.println("getFilteredList()");
        if (isJpqlChanged()) {
            cache = getByJpql(getJpql());
            queryHashCode = getQueryDescriptor().getConjunctionCriterion().hashCode();
        }
        return cache;
    }
    
    private String getJpql() {
        whereClauseAdded = false;
        StringBuilder jpql = new StringBuilder();
        jpql.append("select o from ").append(entityClass.getSimpleName());
        jpql.append(" o");
        if (null != hiddenCriteria) {
            for (EntityCriterion c : hiddenCriteria) {
                appendCriterion(c, jpql, ConjunctionCriterion.Conjunction.AND);
            }
        }
        for (Criterion c : getQueryDescriptor().getConjunctionCriterion().getCriterionList()) {
            EntityCriterion criterion = (EntityCriterion)c;
            appendCriterion(criterion, jpql, getQueryDescriptor().getConjunctionCriterion().getConjunction());
        }
        System.out.println("getJpql(): " + jpql.toString());
        return jpql.toString();        
    }
    
    private void appendCriterion(EntityCriterion criterion, StringBuilder jpql, ConjunctionCriterion.Conjunction conjunction) {
        boolean nullValue = false;
        for (Object o : criterion.getValues()) {
            if (null == o) {
                nullValue = true;
                break;
            }
            if (o instanceof String && "".equals(o)) {
                nullValue = true;
                break;
            }
        }
        
        if (!nullValue) { 
            StringBuilder attrName = new StringBuilder();
            attrName.append("o.").append(criterion.getAttribute().getName());
            String fragment = ((EntityAttributeDescriptor.EntityOperator)criterion.getOperator()).formatJpql(attrName.toString(), criterion.getValues());

            if (null != fragment && !"".equals(fragment)) {            
                System.out.println("    conjunction = " + conjunction);
                if (!whereClauseAdded) {
                    jpql.append(" where ");
                    whereClauseAdded = true;
                } else  if (ConjunctionCriterion.Conjunction.AND.equals(conjunction)) {
                    jpql.append(" and ");
                } else if (ConjunctionCriterion.Conjunction.OR.equals(conjunction)) {
                    jpql.append(" or ");
                } 
            
                jpql.append(fragment);
            }
        }
    }
    
    private LovService<E> getLovService() {
        if (null == lovService) {
            try {
                lovService = (LovService<E>)EJBUtils.lookup( serviceJndiName);
            } catch (NamingException e) {
                System.out.println("LovService not found! " + e.getMessage());
            }
        }
        return lovService;
    }    
  
    public List<E> getByJpql(String jpql) {
        return getLovService().queryByJpql(jpql);
    }

    public List<? extends E> getItems() {
        System.out.println("getItems()");        
        return getFilteredList();
    }

    public List<? extends E> getRecentItems() {
        System.out.println("getRecentItems()");
        return null;
    }

    public boolean isAutoCompleteEnabled() {
        System.out.println("isAutoCompleteEnabled()");        
        return false;
    }

    public void performQuery(QueryDescriptor queryDescriptor) {
        System.out.println("performQuery(" + queryDescriptor  + ")");
    }

    public List autoCompleteValue(Object object) {
        System.out.println("autoCompleteValue(" + logObject(object) + ")");
        if (EntityLovModelUtil.isNumber(key.getType())) {
            getCriterionByName(key.getName()).setOperator(EntityOperatorType.EQUALS_NUMBER); 
            Constructor method;
            try {
                String string = "" + object;
                string = string.replaceAll("[^0-9]", "");
                method = key.getType().getConstructor(String.class);
                object = method.newInstance(string);
            } catch (NoSuchMethodException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        } else if (String.class.isAssignableFrom(key.getType())) {
            getCriterionByName(key.getName()).setOperator(EntityOperatorType.STARTS_WITH); 
        }
        cache = null;
        getCriterionByName(key.getName()).setValue(object);
        List result = getFilteredList();
        if (null != result && result.size() == 1) {
            RowKeySet rks = new RowKeySetImpl();
            rks.add(object);
            valueSelected(rks);
        }
        return result;
    }

    public void valueSelected(Object object) {
        System.out.println("valueSelected(" + logObject(object) +")");
        cache = null;
        getTableModel().getCollectionModel().setRowKey(object);
    }

    @Override
    public Object getValueFromSelection(Object object) {
        System.out.println("getValueFromSelection("+logObject(object) + ")");
        if (object instanceof List) {
            object = ((List)object).get(0);
        }
        
        if (entityClass.isAssignableFrom(object.getClass())) {
            object = getKeyFromEntity(keyGetter, object);
        }
        if (returnKey) {
            return getKeyFromEntity(keyGetter, ((EntityCollectionModel)getTableModel().getCollectionModel()).getRowData(object));
        } else {
            return ((EntityCollectionModel)getTableModel().getCollectionModel()).getRowData(object);  
        }
        
    }
    
    public int getTotalWidth() {
        return ((EntityTableModel)getTableModel()).getTotalWidth();
    }
}