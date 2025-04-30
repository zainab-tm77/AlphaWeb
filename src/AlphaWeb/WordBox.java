/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;  
import java.util.ArrayList; 

/**
 *
 * @author malai
 */
public class WordBox {

    private ArrayList<String> foundWords;

    public WordBox() {
        this.foundWords = new ArrayList<>();
    }

    public void addWord(String word) {
        foundWords.add(word);
        System.out.println("📦 Word added to WordBox: " + word);
    }

    public void displayWords() {
        System.out.println("Words found so far:");
        for (String word : foundWords) {
            System.out.println("- " + word);
        }
    }
}