/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;

/**
 *
 * @author malai
 */
public class Hangman {

    private int partsAdded;

    public Hangman() {
        this.partsAdded = 0;
    }

    public void addPart() {
        partsAdded++;
        drawHangman(1);
    }

    public boolean isDead() {
        return partsAdded >= 6; // 6 wrong guesses = complete hangman
    }

    public void drawHangman(int wrongAttempts) {
        switch (wrongAttempts) {
            case 1:
                System.out.println("\n  O");
                break;
            case 2:
                System.out.println("\n  O\n  |");
                break;
            case 3:
                System.out.println("\n  O\n /|");
                break;
            case 4:
                System.out.println("\n  O\n /|\\");
                break;
            case 5:
                System.out.println("\n  O\n /|\\\n /");
                break;
            case 6:
                System.out.println("\n  O\n /|\\\n / \\");
                System.out.println("☠️  Game Over!");
                break;
            default:
                break;
        }
    }
}