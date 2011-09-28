package nl.whitehorses.wls.schedular;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import commonj.timers.*;

/**
 * TimerServlet demonstrates a simple use of commonj timers
 */
public class TimerServlet extends HttpServlet {

    
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        System.out.println("timer servlet is initialized  ");
        try {
            InitialContext ic = new InitialContext();
            TimerManager tm = (TimerManager)ic.lookup("java:comp/env/tm/TimerManager");

            Timer batchRun1Timer = null; 
            Boolean batchRun1TimerIsRunning = false;
            Timer batchRun2Timer = null; 
            Boolean batchRun2TimerIsRunning = false;

            // Execute timer every 30 seconds starting immediately
            batchRun1Timer = tm.schedule(new Batch1(), 0, 30 * 1000);
            batchRun1TimerIsRunning = true;

            batchRun2Timer = tm.schedule(new Batch2(), 0, 30 * 1000);
            batchRun2TimerIsRunning = true;

            config.getServletContext().setAttribute("batch1",batchRun1Timer);
            config.getServletContext().setAttribute("batch2",batchRun2Timer);
            config.getServletContext().setAttribute("batch1Running",batchRun1TimerIsRunning);
            config.getServletContext().setAttribute("batch2Running",batchRun2TimerIsRunning);
 
        } catch (NamingException ne) {
            ne.printStackTrace();
        }
    }

    public void service(HttpServletRequest req,  HttpServletResponse res) throws IOException {
       res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<h4>Timer servlet is working!</h4>");
    }
}
