package nl.amis.jsf.model;

import java.util.ArrayList;
import java.util.List;

import oracle.adf.view.rich.model.ColumnDescriptor;
import oracle.adf.view.rich.model.TableModel;

import org.apache.myfaces.trinidad.model.CollectionModel;


public abstract class EntityTableModel extends TableModel {
    private Class entityClass;
    private CollectionModel collectionModel;
    private List<ColumnDescriptor> columnDescriptors = new ArrayList<ColumnDescriptor>();
    
    public EntityTableModel(Class entityClass) {
        super();
        this.entityClass = entityClass;
    }

    public CollectionModel getCollectionModel() {
        if (null == collectionModel) {
            collectionModel = new EntityCollectionModel(entityClass) {
                    public List getFilteredList() {
                        return EntityTableModel.this.getFilteredList();
                    }

                    public boolean isJpqlChanged() {
                        return EntityTableModel.this.isJpqlChanged();
                    }
                };                
        }
        return collectionModel;
    }

    public List<ColumnDescriptor> getColumnDescriptors() {
        return columnDescriptors;
    }
    
    void addColumDescriptor(ColumnDescriptor descriptor) {
        columnDescriptors.add(descriptor);
    }
    
    int getTotalWidth() {
        int result = 10;
        for (ColumnDescriptor descriptor : columnDescriptors) {
            result += descriptor.getWidth();
        }
        return result;
    }
    
    public abstract List getFilteredList();
    public abstract boolean isJpqlChanged();
}
