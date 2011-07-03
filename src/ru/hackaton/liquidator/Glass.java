package ru.hackaton.liquidator;

public class Glass {
    public static final int THRESHOLD = 5;//percents of required capacity

    private int requiredCapacity;//percents
    private int maxCapacity;//absolute
    private int currentCapacity;//absolute

    private int maxError;//absolute

    public Glass(int maxCapacity, int requiredCapacity) {
        this.requiredCapacity = requiredCapacity;
        this.maxCapacity = maxCapacity;
        maxError = THRESHOLD * requiredCapacity / 100;
    }

    /*
       [0,requiredCapacity-THRESHOLD)
    */
    public static final int STATE_MORE = 0;

    /*
       [requiredCapacity-THRESHOLD, requiredCapacity+THRESHOLD]
    */
    public static final int STATE_OK = 1;

    /*
       (requiredCapacity+THRESHOLD, 100]
    */
    public static final int STATE_TOO_MUCH = 2;

    /*
       (100, inf)
    */
    public static final int STATE_OVERFLOW = 3;


    public static final Object lockObject = new Object();

    public void add(int amount) {
        synchronized (lockObject) {
            currentCapacity += amount;
        }
    }

    public int getAmount() {
        synchronized (lockObject) {
            return currentCapacity;
        }
    }

    public int getMaxCapacity() {
        synchronized (lockObject) {
            return maxCapacity;
        }
    }

    public int getPercent() {
        synchronized (lockObject) {
            return (int) (100 * 0.63 * currentCapacity / maxCapacity);//0.63 is a hack
        }
    }

    public int getState() {
        synchronized (lockObject) {
            if (currentCapacity < requiredCapacity - maxError) {
                return STATE_MORE;
            } else if (currentCapacity <= requiredCapacity + maxError) {
                return STATE_OK;
            } else if (currentCapacity <= maxCapacity) {
                return STATE_TOO_MUCH;
            } else {
                return STATE_OVERFLOW;
            }
        }
    }
}