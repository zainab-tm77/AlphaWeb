package AlphaWeb;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shafia Haider 
 */
public class Score {

    private int score;

    public Score() {
        this.score = 0;
    }

    public void increaseScore() {
        this.score += 10; // Easy score logic, can change based on level
    }

    public void decreaseScore() {
        this.score -= 5;
        if (this.score < 0) {
            this.score = 0;
        }
    }

    public int getScore() {
        return score;
    }
}
