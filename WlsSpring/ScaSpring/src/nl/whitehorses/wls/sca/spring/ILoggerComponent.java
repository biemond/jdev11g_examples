package nl.whitehorses.wls.sca.spring;

public interface ILoggerComponent
{
   
    /**
     * Log a message, including the originating component, its instance id and
     * a message.
     * @param pComponentName the name of the component that sends this log msg
     * @param pInstanceId the instanceId of the component instance
     * @param pMessage the message to be logged
     */
    public void log (String pComponentName,
                     String pInstanceId, String pMessage);
}