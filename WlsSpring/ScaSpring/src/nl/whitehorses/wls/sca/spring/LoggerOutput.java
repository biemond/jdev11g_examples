package nl.whitehorses.wls.sca.spring;

public class LoggerOutput {
    public LoggerOutput() {
        super();
    }
    
    public void logToConsole(String pComponentName, String pInstanceId,  String pMessage)
    {
        StringBuffer logBuffer = new StringBuffer ();
        logBuffer.append("[").append(pComponentName).append("] [Instance: ").
            append(pInstanceId).append("] ").append(pMessage);
        
        System.out.println(logBuffer.toString());
    }
}
