package AlphaWeb.core;

public class Score {
    private int score = 0;

    public void setScore(int score) {
        this.score = score;
    }

    public void decrementScore(int amount) {
        score = Math.max(0, score - amount);
    }

    public int getScore() {
        return score;
    }

    public void decreaseScore(int amount) {
        decrementScore(amount); // Alias for consistency
    }

    public void increaseScore(int i) {
        score += i; // Add the increment to the score
    }
}