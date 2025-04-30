/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
/**
 *
 * @author malai
 */
public class Timer {

    private int seconds;
    private boolean timeUp;

    public Timer() {
        // Default timer, you can later change based on level
        this.seconds = 120; // 2 minutes
        this.timeUp = false;
    }

    public void startTimer() {
        // In real game, you would start a real clock
        System.out.println("🕒 Timer started: " + seconds + " seconds remaining.");
    }

    public void stopTimer() {
        timeUp = true;
        System.out.println("🛑 Timer stopped.");
    }

    public boolean isTimeUp() {
        return timeUp;
    }
}