package ru.hackaton.liquidator;

public class Glass {
    public static final int THRESHOLD = 5;//percents

    private int requiredCapacity;//percents
    private int maxCapacity;//absolute
    private int currentCapacity;//absolute

    /*
        [0,requiredCapacity-THRESHOLD)
     */
    public static final int STATE_MORE = 0;

    /*
       [requiredCapacity-THRESHOLD, requiredCapacity+THRESHOLD]
    */
    public static final int STATE_OK = 0;

    /*
       (requiredCapacity+THRESHOLD, 100]
    */
    public static final int STATE_TOO_MUCH = 0;

    /*
       (100, inf)
    */
    public static final int STATE_OVERFLOW = 0;


    public static final Object lockObject = new Object();

    public void add(int amount) {
        synchronized (lockObject) {
            currentCapacity += amount;
        }
    }

    public int getPercent() {
        synchronized (lockObject) {
            return currentCapacity / maxCapacity;
        }
    }

    public int getState() {
        synchronized (lockObject) {
            int currentPercents = currentCapacity / maxCapacity;
            if (currentPercents < requiredCapacity - THRESHOLD) {
                return STATE_MORE;
            } else if (currentPercents <= requiredCapacity - THRESHOLD) {
                return STATE_OK;
            } else if (currentPercents < 100) {
                return STATE_TOO_MUCH;
            } else {
                return STATE_OVERFLOW;
            }
        }
    }
}