package nl.amis.jpa;

import java.util.ArrayList;

import nl.amis.jpa.entities.Departments;

import org.eclipse.persistence.mappings.structures.ObjectRelationalDataTypeDescriptor;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLCollection;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLrecord;


public class DepartmentUtils {
  public DepartmentUtils() {
  }

  private static String departmentPLSQLRecType = "HR_DEPARTMENTS.department_rec_type";
  private static String departmentPLSQLTabType = "HR_DEPARTMENTS.department_tab_type";

  private static String departmentRecType = "HR_DEPARTMENT_ROW_TYPE";
  private static String departmentTabType = "HR_DEPARTMENT_TYPE";

  private static String departmentIdColumn = "DEPARTMENT_ID";
  private static String departmentNameColumn = "DEPARTMENT_NAME";
  private static String departmentLocationColumn = "LOCATION_ID";
  private static String departmentManagerColumn = "MANAGER_ID";

  private static String departmentIdEntity = "departmentId";
  private static String departmentNameEntity = "departmentName";
  private static String departmentLocationEntity = "locationId";
  private static String departmentManagerEntity= "managerId";


  public static PLSQLrecord departmentRecord() {
    PLSQLrecord record = new PLSQLrecord();
    record.setTypeName(departmentPLSQLRecType);
    record.setCompatibleType(departmentRecType);
    record.setJavaType(Departments.class);
    record.addField(departmentIdColumn, 
                    JDBCTypes.NUMERIC_TYPE, 4,0);
    record.addField(departmentNameColumn, 
                    JDBCTypes.VARCHAR_TYPE, 30);
    record.addField(departmentLocationColumn, 
                    JDBCTypes.NUMERIC_TYPE, 4, 0);
    record.addField(departmentManagerColumn, 
                    JDBCTypes.NUMERIC_TYPE, 6, 0);
    return record;
  }

  public static PLSQLCollection departmentCollection() {
    PLSQLCollection collection = new PLSQLCollection();
    collection.setTypeName(departmentPLSQLTabType);
    collection.setCompatibleType(departmentTabType);
    collection.setJavaType(ArrayList.class);
    
    // add the department PLSQLRecord
    collection.setNestedType(departmentRecord());
    return collection;
  }

  public static ObjectRelationalDataTypeDescriptor departmentDescriptor(){
      
    ObjectRelationalDataTypeDescriptor descriptor =
        new ObjectRelationalDataTypeDescriptor();
  
    descriptor.setJavaClass(Departments.class);
    descriptor.setTableName(departmentPLSQLRecType);
    descriptor.setStructureName(departmentRecType);
    
    descriptor.addFieldOrdering(departmentIdColumn);
    descriptor.addFieldOrdering(departmentNameColumn);
    descriptor.addFieldOrdering(departmentManagerColumn);
    descriptor.addFieldOrdering(departmentLocationColumn);
    
    descriptor.addDirectMapping(departmentIdEntity,
                                departmentIdColumn);
    descriptor.addDirectMapping(departmentNameEntity,
                                departmentNameColumn);
    descriptor.addDirectMapping(departmentManagerEntity, 
                                departmentManagerColumn);
    descriptor.addDirectMapping(departmentLocationEntity, 
                                departmentLocationColumn);
    
    descriptor.addPrimaryKeyFieldName(departmentIdColumn);
    return descriptor;
  }

}
