/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author malai
 */
public class Hint {

    private ArrayList<String> remainingWords;

    public Hint(ArrayList<String> wordList) {
        this.remainingWords = wordList;
    }

    public void showHint() {
        if (remainingWords.isEmpty()) {
            System.out.println("No more hints available!");
            return;
        }
        
        Random rand = new Random();
        String hintWord = remainingWords.get(rand.nextInt(remainingWords.size()));
        System.out.println("💡 Hint: One word is --> " + hintWord.charAt(0) + "...");
    }
}
