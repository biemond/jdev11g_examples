package nl.amis.adf.dc.view.bean;

import nl.amis.jsf.model.EntityLovModel;
import nl.amis.jsf.model.EntityLovModel.Builder.FieldDef;

import nl.amis.jsf.model.EntityOperatorType;
import nl.amis.model.hr.entities.Departments;

import nl.amis.model.hr.service.HRSessionLocal;

import oracle.adf.view.rich.model.ListOfValuesModel;


public class DepartmentLOVBean {
    public DepartmentLOVBean() {
    }

    private ListOfValuesModel lovDepartments;

    public ListOfValuesModel getDepartmentLovModel() {
      if (null == lovDepartments) {
          EntityLovModel.Builder<Departments> builder =
              new EntityLovModel.Builder<Departments>(Departments.class,
                                                      "java:comp/env/ejb/local/HRSessionBean",
                                                       HRSessionLocal.class);
          lovDepartments =
                  builder.add(new FieldDef("departmentId").columnWidth(30).key())
                         .add(new FieldDef("departmentName").columnWidth(100))
                         .add(new FieldDef("locationId").columnWidth(30))
            //             .add(new FieldDef("locationId").singleSelect("locationsFindAll","locationId","city"))
                         .returnKey().build();
      }

      return lovDepartments;
  }


}
