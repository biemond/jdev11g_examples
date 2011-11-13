package nl.amis.model.hr.interfaces;

import java.util.List;

public interface LovService<T> {
    public List<T> queryByJpql(String jpql);
}
