package nl.amis.view.beans;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.share.logging.ADFLogger;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.JboException;

public class SearchBean {

    private static ADFLogger logger =
        ADFLogger.createADFLogger(SearchBean.class);

    public SearchBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    private String searchString = "Cheap Trick";

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void searchAction(ActionEvent p0) {
        //TODO Auto-generated method stub
        logger.fine("searchAction");
        // get the binding container
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding method = bindings.getOperationBinding("searchITunes");
        method.execute();
        for (Object o : method.getErrors()) {
            JboException ex = (JboException)o;
            String msgText = ex.getMessage();
            String methodName = method.getName();
            logger.severe(ex.getMessage());
            FacesMessage msg =
                new FacesMessage(FacesMessage.SEVERITY_WARN, methodName,
                                 msgText);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        AttributeBinding attr = (AttributeBinding)bindings.getControlBinding("resultCount");  
        logger.fine("Total result: "+attr.getInputValue().toString());  
    }

}
