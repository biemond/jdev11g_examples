package nl.amis.jsf.model;

import java.util.ArrayList;
import java.util.List;

import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.Criterion;


public class EntityConjunctionCriterion extends ConjunctionCriterion {
    private List<Criterion> criteria = new ArrayList<Criterion>();
    private Conjunction conjunction = Conjunction.AND;
    
    public EntityConjunctionCriterion() {
        super();
    }

    public ConjunctionCriterion.Conjunction getConjunction() {
        return conjunction;
    }

    public void setConjunction(ConjunctionCriterion.Conjunction conjunction) {
        this.conjunction = conjunction;
    }
  
    public List<Criterion> getCriterionList() {
        return criteria;
    }

    public Object getKey(Criterion criterion) {
        return criteria.indexOf(criterion);
    }

    public Criterion getCriterion(Object object) {
        int index = 0;
        if (object instanceof String) {
            index = Integer.valueOf((String)object);
        } if (object instanceof Number) {
            index = ((Number)object).intValue();
        }
        return criteria.get(index);
    }

    public void addCriterion(Criterion criterion) {
        criteria.add(criterion);
    }
    
    public boolean removeCriterion(Criterion criterion) {
        return criteria.remove(criterion);
    }
    
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EntityConjunctionCriterion)) {
            return false;
        }
        final EntityConjunctionCriterion other = (EntityConjunctionCriterion)object;
        if (!(criteria == null ? other.criteria == null : criteria.equals(other.criteria))) {
            return false;
        }
        if (!(conjunction == null ? other.conjunction == null : conjunction.equals(other.conjunction))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = super.hashCode();
        result = PRIME * result + ((criteria == null) ? 0 : criteria.hashCode());
        result = PRIME * result + ((conjunction == null) ? 0 : conjunction.hashCode());
        return result;
    }
}
