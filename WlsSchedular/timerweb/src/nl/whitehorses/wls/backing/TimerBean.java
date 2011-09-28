package nl.whitehorses.wls.backing;

import commonj.timers.Timer;
import commonj.timers.TimerManager;

import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.whitehorses.wls.schedular.Batch1;
import nl.whitehorses.wls.schedular.Batch2;

import javax.faces.context.FacesContext;  
import javax.servlet.ServletContext;

public class TimerBean {


    private InitialContext ic = null;
    private TimerManager tm = null;

    private Timer batchRun1Timer = null; 
    public Boolean batchRun1TimerIsRunning = false;
    private Timer batchRun2Timer = null; 
    public Boolean batchRun2TimerIsRunning = false;


    public TimerBean() {
        try {
            ic = new InitialContext();
            tm = (TimerManager)ic.lookup("java:comp/env/tm/TimerManager");

           FacesContext ctx = FacesContext.getCurrentInstance(); 
           ServletContext servletContext = (ServletContext) ctx.getExternalContext().getContext();
          
           batchRun1Timer = (Timer)servletContext.getAttribute("batch1");
           batchRun2Timer = (Timer)servletContext.getAttribute("batch2");
           batchRun1TimerIsRunning = (Boolean)servletContext.getAttribute("batch1Running");
           batchRun2TimerIsRunning = (Boolean)servletContext.getAttribute("batch2Running");
           System.out.println("init end");

       } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void timerManager(ActionEvent actionEvent) {
        // Add event code here...
        if ( tm.isSuspended() ) {
            tm.resume();        
        } else {
            tm.suspend();
        }
    }

    public void Batch1(ActionEvent actionEvent) {
        // Add event code here...
        if (  batchRun1TimerIsRunning ) { 
              batchRun1Timer.cancel();
              batchRun1TimerIsRunning = false;
        } else {
               batchRun1Timer = tm.schedule(new Batch1(), 0, 10 * 1000);
               batchRun1TimerIsRunning = true;
        }
        
    }

    public void Batch2(ActionEvent actionEvent) {
        // Add event code here...
        if (  batchRun2TimerIsRunning ) { 
              batchRun2Timer.cancel();
              batchRun2TimerIsRunning = false;
        } else {
               batchRun2Timer = tm.schedule(new Batch2(), 0, 10 * 1000);
               batchRun2TimerIsRunning = true;
        }
    }
    
    public String getTmStatus () {
        if ( tm.isSuspended() ) {
           return "TimerManager is stopped";        
        } else {
           return "TimerManager is running";        
        }
    }

    public String getBatch1Status () {
       Long time = batchRun1Timer.getScheduledExecutionTime();
       java.util.Date date = new  java.util.Date(time);
       if ( batchRun1TimerIsRunning ) { 
         return "Batch1 scheduled time "+date.toString();
       } {
         return "Batch1 stopped";
       }
    }
    
    public String getBatch2Status () {
       Long time = batchRun2Timer.getScheduledExecutionTime();
       java.util.Date date = new  java.util.Date(time);
        if ( batchRun2TimerIsRunning ) { 
          return "Batch2 scheduled time "+date.toString();
        } {
          return "Batch2 stopped";
        }    
    }


    public Timer getBatchRun1Timer(){
      return batchRun1Timer;
    }

    public void setBatchRun1Timer(Timer batchRun1Timer ){
      this.batchRun1Timer = batchRun1Timer;
    }

    public Timer getBatchRun2Timer(){
      return batchRun2Timer;
    }

    public void setBatchRun2timer(Timer batchRun2Timer ){
      this.batchRun2Timer = batchRun2Timer;
    }
}
