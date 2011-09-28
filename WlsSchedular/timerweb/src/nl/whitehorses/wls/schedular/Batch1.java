package nl.whitehorses.wls.schedular;

import commonj.timers.*;
import java.io.Serializable;

public class Batch1 implements Serializable, TimerListener, CancelTimerListener {

    public void timerExpired(Timer timer) {
        System.out.println("Batch1 timer expired called on " + timer);
    }
    public  void timerCancel(Timer timer) {
        System.out.println("Batch1 timer cancelled called on " + timer);

    }
}
