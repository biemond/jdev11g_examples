package nl.amis.jsf.model;

import java.util.HashMap;
import java.util.Map;

import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.QueryDescriptor;



public class EntityQueryDescriptor<E> extends QueryDescriptor {
    private Map<String, Object> uiHints;
    private EntityConjunctionCriterion cc;
    private String name;
    private EntityQueryModel parentModel;
    
    public EntityQueryDescriptor(EntityQueryModel parentModel, String name, boolean advanced) {
        super();
        this.parentModel = parentModel;
        this.uiHints = new HashMap<String, Object>();
        this.cc = new EntityConjunctionCriterion();
        cc.setConjunction(ConjunctionCriterion.Conjunction.AND);
        this.name = name;
        
        uiHints = new HashMap<String, Object>();
        uiHints.put(QueryDescriptor.UIHINT_AUTO_EXECUTE, Boolean.TRUE);
        uiHints.put(QueryDescriptor.UIHINT_DEFAULT, Boolean.TRUE);
        uiHints.put(QueryDescriptor.UIHINT_IMMUTABLE, Boolean.FALSE);
        if (advanced) {
            uiHints.put(QueryDescriptor.UIHINT_MODE, QueryMode.ADVANCED);
        } else {
            uiHints.put(QueryDescriptor.UIHINT_MODE, QueryMode.BASIC);
        }
        uiHints.put(QueryDescriptor.UIHINT_NAME, name);
        uiHints.put(QueryDescriptor.UIHINT_SAVE_RESULTS_LAYOUT, Boolean.TRUE);
        uiHints.put(QueryDescriptor.UIHINT_SHOW_IN_LIST, Boolean.TRUE);
    }
    
    /**
     * Copy constructor that creates a clone of the given descriptor, with a
     * new name, as given in the name attribute.
     * 
     * @param name       new name for the QueryDescriptor to be created
     * @param descriptor new QueryDescriptor will use all values (except name)
     *                   from the given one.
     */
    public EntityQueryDescriptor(EntityQueryModel parentModel, String name, EntityQueryDescriptor descriptor, boolean advanced) {
        this(parentModel, name, advanced);
    }
    
    public void addCriterion(String name) {
        EntityAttributeDescriptor ead = parentModel.getAttributeByName(name);
        if (null == ead) {
            try {            
                ead = EntityAttributeDescriptor.getInstance(parentModel.entityClass, name);
            } catch (NoSuchFieldException e) {
            }
        }
        cc.addCriterion(new EntityCriterion(ead));        
    }

    public void changeMode(QueryDescriptor.QueryMode queryMode) {
        uiHints.put(QueryDescriptor.UIHINT_MODE, queryMode);        
    }

    public ConjunctionCriterion getConjunctionCriterion() {
        return cc;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getUIHints() {
        return uiHints;
    }

    public void removeCriterion(Criterion criterion) {
        cc.removeCriterion(criterion);
    }

    public AttributeCriterion getCurrentCriterion() {
        return (AttributeCriterion)cc.getCriterion(0);
    }

    public void setCurrentCriterion(AttributeCriterion attributeCriterion) {
    }
}
