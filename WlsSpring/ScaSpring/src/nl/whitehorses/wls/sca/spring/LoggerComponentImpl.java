package nl.whitehorses.wls.sca.spring;


public class LoggerComponentImpl implements ILoggerComponent {
    public LoggerComponentImpl() {
        super();
    }

    @Override
    public void log(String pComponentName, String pInstanceId,
                    String pMessage)  {
       output.logToConsole(pComponentName, pInstanceId, pMessage);
    }

    private LoggerOutput output;

    public void setOutput(LoggerOutput output) {
        this.output = output;
    }

    public LoggerOutput getOutput() {
        return output;
    }
}
