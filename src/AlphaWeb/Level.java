package AlphaWeb;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author malai
 */
public abstract class Level {
    protected String name;
    protected int timerSeconds;
    protected int baseScore;

    public Level(String name, int timerSeconds, int baseScore) {
        this.name = name;
        this.timerSeconds = timerSeconds;
        this.baseScore = baseScore;
    }

    public String getName() {
        return name;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public int getBaseScore() {
        return baseScore;
    }
    

    public abstract int calculateScore(int foundWordsCount);
}