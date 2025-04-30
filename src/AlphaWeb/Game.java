/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author malai
 */
public class Game {
    private ArrayList<String> wordsToFind;
    private ArrayList<String> foundWords;
    private int timer;
    private int score;
    private Scanner scanner;
    private int hintPenalty = 5;
    private int maxHints = 3; 
    private int hintsUsed = 0;
    private int wrongAttempts = 0;
    private Hangman hangman;
    
    // --- Constructor ---
    public Game(ArrayList<String> words) {
        this.wordsToFind = words;
        this.foundWords = new ArrayList<>();
        this.score = 0;
        this.scanner = new Scanner(System.in);
        this.hangman = new Hangman();

        if (words.size() <= 5) {
            this.timer = 60; // 1 minute
        } else if (words.size() <= 10) {
            this.timer = 90; // 1.5 minutes
        } else {
            this.timer= 120; // 2 minutes
        }
    }
    
    public void startGame() {
        System.out.println("\n🎮 Game Started!");
        System.out.println("🕑 Time Limit: " + timer + " seconds");
        System.out.println("Find the following words:");

        for (String word : wordsToFind) {
            System.out.print(word.charAt(0) + " _ _ _ "); 
            System.out.println("(Word length: " + word.length() + ")");
        }

        // Start timer (simple simulation)
        long startTime = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTime) / 1000 < timer && foundWords.size() < wordsToFind.size()) {
            long elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            long remainigTime = timer - elapsedTime;
            System.out.println("\nTime left: "+remainigTime+"seconds");
            
            System.out.println("\nOptions: ");
            System.out.println("1. Enter a word: ");
            System.out.println("2. Take a hint(-5 points) ");
            System.out.println("3. Exit Game");
            System.out.print("Your choice: ");
            
            String choice = scanner.nextLine();
            
            if(choice.equals("1")){
                System.out.println("Enter a word you found: ");
                String input = scanner.nextLine().toUpperCase();
                
                if (wordsToFind.contains(input) && !foundWords.contains(input)) {
                    foundWords.add(input);
                    score += 10;
                    System.out.println("✅ Correct! Score: " + score);
                } else {
                    wrongAttempts++;
                    System.out.println("❌ Incorrect or already found. Try again!");
                    hangman.drawHangman(wrongAttempts);
                    
                    if(wrongAttempts >= 6){
                        System.out.println("\nYou lost the game!");
                        break;
                    }
                }
                
            }else if(choice.equals("2")){
                takeHint();
                
            }else if(choice.equals("3")){
                System.out.println("Exiting Game");
                endGame();
                return;
                
            }else{
                System.out.println("Invalid choice.");
            }
        }
        
        endGame();

    }
    
    private void takeHint() {
        if (hintsUsed >= maxHints) {
            System.out.println("🚫 No hints left!");
            return;
        }

        for (String word : wordsToFind) {
            if (!foundWords.contains(word)) {
                hintsUsed++;
                score -= hintPenalty;
                System.out.println("💡 Hint: The word starts with '" + word.charAt(0) + "' and is " + word.length() + " letters long.");
                System.out.println("New Score: " + score);
                return;
            }
        }
    }
    
    private void endGame() {
        System.out.println("\n⏰ Time's up or all words found!");
        System.out.println("🏆 Final Score: " + score);
        System.out.println("📦 Words Found: " + foundWords);
        
        Result result = new Result(score);
        result.showResult();
    }
}

