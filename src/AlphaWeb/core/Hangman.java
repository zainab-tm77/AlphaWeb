package AlphaWeb.core;

public class Hangman {
    private int partsAdded;

    public Hangman() {
        this.partsAdded = 0;
    }

    public void addPart() {
        partsAdded++;
    }

    public boolean isDead() {
        return partsAdded >= 7;
    }

    public int getPartsAdded() {
        return partsAdded;
    }
}