
public class Time implements Runnable {

    interface OnTimeFlowListener {
        void onTimeFlow();
    }

    OnTimeFlowListener onTimeFlowListener = null;
    private Object stdLock;
    private Object secLock;
    long std;

    private final int START_SEC = 15;
    private boolean running;
    private int sec;
    Thread timeThread = null;
    private String rName = null;


    public Time(String rName) {
        running = true;
        stdLock = new Object();
        secLock = new Object();
        sec = START_SEC;
        timeThread = new Thread(this);
        timeThread.start();
        this.rName = rName;
    }

    public int getSec() {
        return sec;
    }

    public void resetTime() {
        synchronized (stdLock) {
            std = System.currentTimeMillis();
        }
        synchronized (secLock) {
            sec = START_SEC;
        }
    }

    @Override
    public void run() {
        std = System.currentTimeMillis();
        while(running) {
            try {
                Thread.sleep(10);
                long cur = System.currentTimeMillis();
                long tmpStd;
                synchronized (stdLock) {
                    tmpStd = std;
                }

                if(cur - tmpStd >= 1000) {
                    synchronized (stdLock) {
                        std = cur;
                    }
                    synchronized (secLock) {
                        --sec;
                    }

                    onTimeFlowListener.onTimeFlow();
                    long tmpSec;
                    synchronized (secLock) {
                        tmpSec = sec;
                    }
                    if(tmpSec < 0) {
                        Thread.sleep(1000);
                        resetTime();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void finish() {
        this.running = false;
        try {
            timeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setTimeFlowListener(OnTimeFlowListener onTimeFlowListener) {
        this.onTimeFlowListener = onTimeFlowListener;
    }
}
