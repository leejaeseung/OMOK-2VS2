
public class Time implements Runnable {

    interface OnTimeFlowListener {
        public void onTimeFlow();
    }
    interface OnTimeZeroListener {
        public void onTimeZero();
    }

    OnTimeZeroListener onTimeZeroListener = null;
    OnTimeFlowListener onTimeFlowListener = null;

    private final int START_SEC = 10;
    private boolean running;
    private int sec;
    Thread timeThread = null;
    private String rName = null;


    public Time(String rName) {
        running = true;
        sec = START_SEC;
        timeThread = new Thread(this);
        timeThread.start();
        this.rName = rName;
    }


    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }



    @Override
    public void run() {
        long std = System.currentTimeMillis();
        while(running) {
            try {
                Thread.sleep(10);
                long cur = System.currentTimeMillis();
                if(cur - std >= 1000) {
                    std = cur;
                    --sec;

                    if(sec < 0) {
                        onTimeZeroListener.onTimeZero();
                        sec = -1;
                        Thread.sleep(1000);
                        sec = START_SEC;
                    } else
                        onTimeFlowListener.onTimeFlow();
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

    public void setTimeZeroListener(OnTimeZeroListener onTimeZeroListener) {
        this.onTimeZeroListener = onTimeZeroListener;
    }
    public void setTimeFlowListener(OnTimeFlowListener onTimeFlowListener) {
        this.onTimeFlowListener = onTimeFlowListener;
    }
}
