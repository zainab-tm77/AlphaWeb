package AlphaWeb.core;

public class Timer {
    private int seconds;
    private boolean isRunning;
    private String level;
    private javax.swing.Timer swingTimer;

    public Timer() {
        seconds = 0;
        isRunning = false;
        level = "Easy";
        swingTimer = new javax.swing.Timer(1000, e -> incrementSecond());
    }

    public void start() {
        isRunning = true;
        swingTimer.start();
    }

    public void stop() {
        isRunning = false;
        swingTimer.stop();
    }

    public void reset() {
        seconds = 0;
        isRunning = false;
        swingTimer.stop();
    }

    public void incrementSecond() {
        if (isRunning) {
            seconds++;
            if (level.equals("Master") && seconds > 180) {
                stop();
            }
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}