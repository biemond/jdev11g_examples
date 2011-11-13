package nl.amis.jsf.model;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;

import static nl.amis.jsf.model.EntityLovModelUtil.getKeyFromEntity;

import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;


public abstract class EntityCollectionModel extends CollectionModel {
    private Method keyGetter;
    private Class entityClass;
    
    private int currentRowIndex = -1;
    private Object selectedRowKey = null;
    private List indexCache;
    private Map<Object, Object> keyCache;
    
    public EntityCollectionModel(Class entityClass) {
        this.entityClass = entityClass;
    }
    
    public void setKeyGetter(Method keyGetter) {
        this.keyGetter = keyGetter;
    }
    
    private List getIndexData() {
        if (null == indexCache || isJpqlChanged()) {
            indexCache = getFilteredList();

            if (null != indexCache) {
                keyCache = new HashMap<Object, Object>();
                for (Object object : indexCache) {
                    if (null != object && entityClass.isAssignableFrom(object.getClass())) {
                        keyCache.put(getKeyFromEntity(keyGetter, object), object);
                    }
                }
            }
        }
        return indexCache;        
    }
    
    private Map<Object, Object> getKeyCache() {
        if (null == keyCache) {
            getIndexData() ;
        }
        return keyCache;
    }
    
    public Object getRowKey() {
        return getKeyFromEntity(keyGetter, getRowData());
    }

    public void setRowKey(Object key) {        
        
        if (null == key) {
            return;
        }
        
        if (key instanceof RowKeySet) {
            selectedRowKey = unwrapRowKeySet((RowKeySet)key);
        } else if (key instanceof List) {
            selectedRowKey = getKeyFromEntity(keyGetter, ((List)key).get(0));
        } else if (entityClass.isAssignableFrom(key.getClass())) {
            selectedRowKey = getKeyFromEntity(keyGetter, key);
        } else {
            selectedRowKey = key;
        }
        
        Object object = getKeyCache().get(selectedRowKey);
        rowSelected(getIndexData().indexOf(object));
        
    }
    
    private Object unwrapRowKeySet(RowKeySet rks) {
        if (null == rks) {
            return null;
        }
        Object result = rks.iterator().next();
        return result;
    }
    
    public boolean isRowAvailable() {
        if (null == getIndexData() || currentRowIndex < 0) {
            return false;
        } else {
            return getIndexData().size() > currentRowIndex;
        }
    }

    public int getRowCount() {
        if (null == getIndexData()) {
            return 0;
        } else {
            int size = getIndexData().size();
            return size;
        }
    }

    public Object getRowData() {
        if (null == getIndexData() || currentRowIndex < 0 || currentRowIndex >= getIndexData().size()) {
            return getKeyCache().get(selectedRowKey);            
        } else {
            return getIndexData().get(currentRowIndex);
        }
    }
    
    public Object getRowData(int index) {
        if (index >= 0 && index < getIndexData().size()) {
            return getIndexData().get(index);
        } else {
            return null;
        }
    }
    
    public Object getRowData(Object key) {
        if (key instanceof RowKeySet) {
            key = unwrapRowKeySet((RowKeySet)key);
        } else if (entityClass.isAssignableFrom(key.getClass())) {
            return key;
        }
        if (null != key) {
            return getKeyCache().get(key);
        } else {
            return null;
        }
    }

    public int getRowIndex() {
        return currentRowIndex;
    }

    public void setRowIndex(int i) {
        if (i >= 0 && i < getIndexData().size()) {
            currentRowIndex = i;
        } else {
            currentRowIndex = -1;
        }
        rowSelected(i);
    }

    public Object getWrappedData() {
        return getIndexData();
    }

    public void setWrappedData(Object object) {
        throw new UnsupportedOperationException();
    }
    
    private void rowSelected(int index) {
        if (index >= 0 && index < getRowCount()) {
            DataModelEvent event = new DataModelEvent(this, index, getIndexData().get(index));
            for (DataModelListener listener : getDataModelListeners()) {
                listener.rowSelected(event);
            }
        }
    }
    
    public Object findByToString(String string) {
        int pos = string.indexOf("@");
        if (pos < 0) {
            return null;
        }
        String hashCodeString = string.substring(pos+1);
        int hashCode = Integer.valueOf(hashCodeString, 16);
        
        for (Object object : getIndexData()) {
            if (hashCode == object.hashCode()) {
                return object;
            }
        }
        return null;
    }
    
    public abstract List getFilteredList();
    public abstract boolean isJpqlChanged();
}
