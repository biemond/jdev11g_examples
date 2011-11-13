package nl.amis.jsf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.QueryModel;


public class EntityQueryModel extends QueryModel {
    private QueryDescriptor descriptor;
    Class entityClass;
    private List<AttributeDescriptor> attributes;
    
    public EntityQueryModel(Class entityClass, boolean advanced) {
        super();
        this.entityClass = entityClass;
        descriptor = new EntityQueryDescriptor(this, "A", advanced);
        attributes = new ArrayList<AttributeDescriptor>();
    }

    public QueryDescriptor create(String name, QueryDescriptor queryDescriptor) {
        return null;
    }
    
    public void delete(QueryDescriptor queryDescriptor) {
    }
    
    void addAttribute(AttributeDescriptor attribute) {
        attributes.add(attribute);
    }

    public List<AttributeDescriptor> getAttributes() {
        return attributes;
    }
    
    EntityAttributeDescriptor getAttributeByName(String name) {
        if (null == name) {
            return null;
        }
        for(AttributeDescriptor attr : getAttributes()) {
            if (name.equals(attr.getName())) {
                return (EntityAttributeDescriptor)attr;
            }
        }
        return null;
    }

    public List<QueryDescriptor> getSystemQueries() {
        return null;
    }
    

    public List<QueryDescriptor> getUserQueries() {
        return null;
    }

    public void reset(QueryDescriptor queryDescriptor) {
    }

    public void setCurrentDescriptor(QueryDescriptor queryDescriptor) {
        this.descriptor = queryDescriptor;
    }
    
    public QueryDescriptor getCurrentDescriptor() {
        return descriptor;
    }

    public void update(QueryDescriptor queryDescriptor, Map<String, Object> map) {
    }
}
