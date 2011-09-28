package nl.whitehorses.wls.sca.spring;

public class LoggerPassThrough implements ILoggerComponent {
    public LoggerPassThrough() {
        super();
    }

    @Override
    public void log(String pComponentName, String pInstanceId,
                    String pMessage)  {
    
       reference.log(pComponentName, pInstanceId, pMessage);
    }

    private ILoggerComponent reference;

    public void setReference(ILoggerComponent reference) {
        this.reference = reference;
    }

    public ILoggerComponent getReference() {
        return reference;
    }

}
